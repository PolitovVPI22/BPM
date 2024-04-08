package org.dstu.bpm;

import org.camunda.bpm.client.spring.annotation.ExternalTaskSubscription;
import org.camunda.bpm.client.task.ExternalTask;
import org.camunda.bpm.client.task.ExternalTaskHandler;
import org.camunda.bpm.client.task.ExternalTaskService;
import org.donstu.DeliveryService;
import org.donstu.client.Order;
import org.donstu.domain.Delivery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;


@Component
@ExternalTaskSubscription("create_delivery")
public class CreateDeliveryHandler implements ExternalTaskHandler {

    DeliveryService deliveryService;

    @Autowired
    private CreateDeliveryHandler(DeliveryService deliveryService) {
        this.deliveryService = deliveryService;
    }

    @Override
    public void execute(ExternalTask externalTask, ExternalTaskService externalTaskService) {
        Map<String, Object> result = new HashMap<>();
        Order order = externalTask.getVariable("order");

        Delivery delivery = new Delivery();
        delivery.setDeliveryAddress(externalTask.getVariable("deliveryAddress"));
        delivery.setOrderId(order.getOrderId());

        result.put("delivery", deliveryService.addDelivery(delivery));
        externalTaskService.complete(externalTask, result);
    }
}
