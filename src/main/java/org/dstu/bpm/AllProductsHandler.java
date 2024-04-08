package org.dstu.bpm;

import org.camunda.bpm.client.spring.annotation.ExternalTaskSubscription;
import org.camunda.bpm.client.task.ExternalTask;
import org.camunda.bpm.client.task.ExternalTaskHandler;
import org.camunda.bpm.client.task.ExternalTaskService;
import org.donstu.client.OrderService;
import org.donstu.client.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Component
@ExternalTaskSubscription("all_products")
public class AllProductsHandler implements ExternalTaskHandler {

    OrderService orderService;

    @Autowired
    private AllProductsHandler(OrderService orderService) {
        this.orderService = orderService;
    }

    @Override
    public void execute(ExternalTask externalTask, ExternalTaskService externalTaskService) {
        Map<String, Object> result = new HashMap<>();
        List<Product> products = orderService.getAllProducts();
        result.put("products", products);
        externalTaskService.complete(externalTask, result);
    }
}
