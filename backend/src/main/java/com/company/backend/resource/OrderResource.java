package com.company.backend.resource;

import com.company.backend.model.entity.Order;
import com.company.backend.model.entity.User;
import com.company.backend.service.OrderService;
import com.company.backend.shared.CurrentUser;
import com.company.backend.shared.GenericResponse;
import com.company.backend.model.vm.OrderVM;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/1.0")
public class OrderResource {

    @Autowired
    OrderService orderService;

    @PostMapping("/orders")
    OrderVM createOrder(@Valid @RequestBody Order order, @CurrentUser User user) {
        return new OrderVM(orderService.save(user, order));
    }

    @GetMapping("/orders")
    Page<OrderVM> getAllOrders(Pageable pageable) {
        return orderService.getAllOrder(pageable).map(OrderVM::new);
    }

    @GetMapping("/users/{username}/orders")
    Page<OrderVM> getOrdersOfUser(@PathVariable String username, Pageable pageable) {
        return orderService.getOrderOfUser(username, pageable).map(OrderVM::new);

    }


    @DeleteMapping("/orders/{id:[0-9]+}")
    @PreAuthorize("@orderSecurityService.isAllowedToDelete(#id, principal)")
    GenericResponse deleteOrder(@PathVariable long id) {
        orderService.deleteOrder(id);
        return new GenericResponse("Order is removed");
    }


}
