package com.company.backend.service;

import com.company.backend.model.entity.Order;
import com.company.backend.model.entity.User;
import com.company.backend.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class OrderSecurityService {

    private final OrderRepository userRepository;

    public OrderSecurityService(OrderRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean isAllowedToDelete(long orderId, User loggedInUser) {
        Optional<Order> optionalOrder = userRepository.findById(orderId);
        if(optionalOrder.isPresent()) {
            Order inDB = optionalOrder.get();
            return inDB.getUser().getId() == loggedInUser.getId();
        }
        return false;
    }
}
