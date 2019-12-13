package com.example.transactionConsumer.functions;

import com.example.transactionConsumer.domains.Order;
import com.example.transactionConsumer.service.TransactionService;
import com.example.transactionConsumer.util.ResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class TransactionProcessor implements Function<Order, ResponseDTO> {
    @Autowired
    TransactionService transactionService;

    public ResponseDTO apply(Order order) {
        return transactionService.registerMessage(order);
    }

}
