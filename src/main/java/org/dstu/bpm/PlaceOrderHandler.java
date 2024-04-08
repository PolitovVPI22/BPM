package org.dstu.bpm;

import org.camunda.bpm.client.spring.annotation.ExternalTaskSubscription;
import org.camunda.bpm.client.task.ExternalTask;
import org.camunda.bpm.client.task.ExternalTaskHandler;
import org.camunda.bpm.client.task.ExternalTaskService;
import org.donstu.client.Order;
import org.donstu.client.OrderService;
import org.donstu.client.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;


@Component
@ExternalTaskSubscription("place_order")
public class PlaceOrderHandler implements ExternalTaskHandler {
    OrderService orderService;

    @Autowired
    private PlaceOrderHandler(OrderService orderService) {
        this.orderService = orderService;
    }

    @Override
    public void execute(ExternalTask externalTask, ExternalTaskService externalTaskService) {
        List<Product> products = externalTask.getVariable("products");
        String name = externalTask.getVariable("name");
        String productIdsString = externalTask.getVariable("productIds");

        List<Integer> productIds = Arrays
                .stream(productIdsString.split(","))
                .map(Integer::parseInt).toList();

        List<Product> foundedProducts = products.stream().filter(p -> {
            for (Integer id : productIds) {
                if (p.getProductId() == id) {
                    return true;
                }
            }
            return false;
        }).toList();
        if (foundedProducts.isEmpty()) {
            externalTaskService.handleBpmnError(externalTask, "400", "Products not found");
            return;
        }

        Order order = orderService.placeOrder(name, foundedProducts);

        Map<String, Object> result = new HashMap<>();
        result.put("order", order);

        externalTaskService.complete(externalTask, result);
    }
}
