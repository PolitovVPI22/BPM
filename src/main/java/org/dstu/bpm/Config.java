package org.dstu.bpm;

import org.donstu.DeliveryService;
import org.donstu.client.OrderService;
import org.donstu.client.OrderService_Service;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.xml.namespace.QName;
import java.net.MalformedURLException;
import java.net.URL;

@Configuration
public class Config {

    private static final QName FQDN = new QName("https://donstu.org/orders", "OrderService");

    private static URL getWsdlUrl(String urlStr) {
        URL url;
        try {
            url = new URL(urlStr);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        return url;
    }

    @Bean
    public OrderService getOrderService() {
        URL wsdlUrl = getWsdlUrl("http://127.0.0.1:8090/orders?wsdl");
        OrderService_Service svc = new OrderService_Service(wsdlUrl, FQDN);
        return svc.getOrderPort();
    }

    @Bean
    public DeliveryService getDeliveryService() {
        return new DeliveryService();
    }

}
