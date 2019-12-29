package com.company.backend;

import com.company.backend.error.ApiError;
import com.company.backend.model.entity.Order;
import com.company.backend.repository.OrderRepository;
import com.company.backend.service.OrderService;
import com.company.backend.model.entity.User;
import com.company.backend.repository.UserRepository;
import com.company.backend.service.UserService;
import com.company.backend.model.vm.OrderVM;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.support.BasicAuthenticationInterceptor;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class OrderResourceTest {
    private static final String API_1_0_ORDERS = "/api/1.0/orders";

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    TestRestTemplate testRestTemplate;

    @Autowired
    UserService userService;

    @Autowired
    OrderService orderService;

    @PersistenceUnit
    private EntityManagerFactory entityManagerFactory;

    @Before
    public void cleanup()  {
        orderRepository.deleteAll();
        userRepository.deleteAll();

        testRestTemplate.getRestTemplate().getInterceptors().clear();
    }

    @Test
    public void postOrder_whenOrderValidAndUserIsAuthorized_receiveOk() {
        userService.save(TestUtil.createValidUser());
        authenticate("test-user");
        Order order = TestUtil.createValidOrder();
        ResponseEntity<Object> response = postOrder(order, Object.class);
        assertEquals(HttpStatus.OK,response.getStatusCode());
    }


    @Test
    public void postOrder_whenOrderIsValidAndUserIsUnauthorized_receiveUnauthorized() {
        Order order = TestUtil.createValidOrder();
        ResponseEntity<Object> response = postOrder(order, Object.class);
        assertEquals(HttpStatus.UNAUTHORIZED,response.getStatusCode());
    }

   @Test
    public void postOrder_whenOrderIsValidAndUserIsUnauthorized_receiveApiError() {
        Order order = TestUtil.createValidOrder();
        ResponseEntity<ApiError> response = postOrder(order, ApiError.class);
       assertEquals(HttpStatus.UNAUTHORIZED.value(),response.getBody().getStatus());
    }

    @Test
    public void postOrder_whenOrderIsValidAndUserIsAuthorized_orderSavedToDatabase() {
        userService.save(TestUtil.createValidUser("user1"));
        authenticate("user1");
        Order order = TestUtil.createValidOrder();
        postOrder(order, Object.class);

        assertEquals(1,orderRepository.count());
    }

    @Test
    public void postOrder_whenOrderIsValidAndUserIsAuthorized_orderSavedToDatabaseWithTimestamp() {
        userService.save(TestUtil.createValidUser("user1"));
        authenticate("user1");
        Order order = TestUtil.createValidOrder();
        postOrder(order, Object.class);

        Order inDB = orderRepository.findAll().get(0);

        assertNotNull(inDB.getTimestamp());
    }

    @Test
    public void postOrder_whenOrderContentNullAndUserIsAuthorized_receiveBadRequest() {
        userService.save(TestUtil.createValidUser("user1"));
        authenticate("user1");
        Order order = new Order();
        ResponseEntity<Object> response = postOrder(order, Object.class);
        assertEquals(HttpStatus.BAD_REQUEST,response.getStatusCode());
    }

    @Test
    public void postOrder_whenOrderContentLessThan10CharactersAndUserIsAuthorized_receiveBadRequest() {
        userService.save(TestUtil.createValidUser("user1"));
        authenticate("user1");
        Order order = new Order();
        order.setContent("123456789");
        ResponseEntity<Object> response = postOrder(order, Object.class);
        assertEquals(HttpStatus.BAD_REQUEST,response.getStatusCode());
    }

    @Test
    public void postOrder_whenOrderIsValidAndUserIsAuthorized_OrderCanBeAccessedFromUserEntity() {
        User user = userService.save(TestUtil.createValidUser("user1"));
        authenticate("user1");
        Order order = TestUtil.createValidOrder();
        postOrder(order, Object.class);

        EntityManager entityManager = entityManagerFactory.createEntityManager();

        User inDBUser = entityManager.find(User.class, user.getId());
        assertEquals(1,inDBUser.getOrders().size());

    }

    @Test
    public void getOrders_whenThereAreNoOrder_receivePageWithZeroItems() {
        ResponseEntity<TestPage<Object>> response = getOrders(new ParameterizedTypeReference<TestPage<Object>>() {});
        assertEquals(0,response.getBody().getTotalElements());
    }

    @Test
    public void getOrders_whenThereAreOrders_receivePageWithItems() {
        User user = userService.save(TestUtil.createValidUser("user1"));
        orderService.save(user, TestUtil.createValidOrder());
        orderService.save(user, TestUtil.createValidOrder());
        orderService.save(user, TestUtil.createValidOrder());

        ResponseEntity<TestPage<Object>> response = getOrders(new ParameterizedTypeReference<TestPage<Object>>() {});
        assertEquals(3,response.getBody().getTotalElements());
    }

    @Test
    public void getOrderOfUser_whenUserExistWithOrder_receivePageWithOrderVM() {
        User user = userService.save(TestUtil.createValidUser("user1"));
        orderService.save(user, TestUtil.createValidOrder());

        ResponseEntity<TestPage<OrderVM>> response = getOrderOfUser("user1", new ParameterizedTypeReference<TestPage<OrderVM>>() {});
        OrderVM orderVM = response.getBody().getContent().get(0);
        assertEquals("user1",orderVM.getUser().getUsername());
    }


    @Test
    public void deleteOrder_whenUserIsAuthorized_orderRemovedFromDatabase() {
        User user = userService.save(TestUtil.createValidUser("user1"));
        authenticate("user1");
        Order order = orderService.save(user, TestUtil.createValidOrder());

        EntityManager entityManager = entityManagerFactory.createEntityManager();

        deleteOrder(order.getId(), Object.class);
        User userInDB = entityManager.find(User.class, user.getId());

        assertEquals(0,userInDB.getOrders().size());

    }


    public <T> ResponseEntity<T> getOrderOfUser(String username, ParameterizedTypeReference<T> responseType){
        String path = "/api/1.0/users/" + username + "/orders";
        return testRestTemplate.exchange(path, HttpMethod.GET, null, responseType);
    }

    public <T> ResponseEntity<T> getOrders(ParameterizedTypeReference<T> responseType){
        return testRestTemplate.exchange(API_1_0_ORDERS, HttpMethod.GET, null, responseType);
    }

    private <T> ResponseEntity<T> postOrder(Order order, Class<T> responseType) {
        return testRestTemplate.postForEntity(API_1_0_ORDERS, order, responseType);
    }

    public <T> ResponseEntity<T> deleteOrder(long orderId, Class<T> responseType){
        return testRestTemplate.exchange(API_1_0_ORDERS + "/" + orderId, HttpMethod.DELETE, null, responseType);
    }



    private void authenticate(String username) {
        testRestTemplate.getRestTemplate()
                .getInterceptors().add(new BasicAuthenticationInterceptor(username, "Java1988"));
    }


}

