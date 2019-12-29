package com.company.backend.service;

import com.company.backend.model.entity.Order;
import com.company.backend.model.entity.User;
import com.company.backend.repository.OrderRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;


@Service
public class OrderService {

    private final OrderRepository orderRepository;

    private final UserService userService;


    public OrderService(OrderRepository orderRepository, UserService userService) {
        this.orderRepository = orderRepository;
        this.userService = userService;

    }

    public Order save(User user, Order order) {
        order.setTimestamp(new Date());
        order.setUser(user);

        return orderRepository.save(order);
    }

    public Page<Order> getAllOrder(Pageable pageable) {
        return orderRepository.findAll(pageable);
    }

    public Page<Order> getOrderOfUser(String username, Pageable pageable) {
        User inDB = userService.getByUsername(username);
        return orderRepository.findByUser(inDB, pageable);
    }

    public void deleteOrder(long id) {
        orderRepository.deleteById(id);
    }




}