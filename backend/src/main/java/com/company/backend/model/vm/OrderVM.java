package com.company.backend.model.vm;

import com.company.backend.model.entity.Order;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OrderVM {

    private long id;

    private String content;

    private long date;

    private UserVM user;


    public OrderVM(Order order) {
        this.setId(order.getId());
        this.setContent(order.getContent());
        this.setDate(order.getTimestamp().getTime());
        this.setUser(new UserVM(order.getUser()));

    }

}
