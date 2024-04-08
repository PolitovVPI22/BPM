package org.donstu;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.donstu.domain.Delivery;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class DeliveryService {

    private final HttpClient client = new DefaultHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();

    public List<Delivery> listDeliveries() {
        HttpGet request = new HttpGet("http://localhost:8091/deliveries/list");
        return executeRequest(request, new TypeReference<List<Delivery>>() {});
    }

    public Delivery getDeliveryById(String id) {
        HttpGet request = new HttpGet("http://localhost:8091/deliveries/" + id);
        return executeRequest(request, new TypeReference<Delivery>() {});
    }

    public Delivery addDelivery(Delivery delivery) {
        try {
            String json = mapper.writeValueAsString(delivery);
            HttpPost request = new HttpPost("http://localhost:8091/deliveries/create");
            StringEntity entity = new StringEntity(json, ContentType.APPLICATION_JSON);
            request.setEntity(entity);
            return executeRequest(request, new TypeReference<Delivery>() {});
        } catch (IOException e) {
            throw new RuntimeException("Error while converting Delivery object to JSON", e);
        }
    }

    private <T> T executeRequest(HttpUriRequest request, TypeReference<T> type) {
        HttpResponse response;
        try {
            response = client.execute(request);
            BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            StringBuilder jsonResponse = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                jsonResponse.append(line);
            }
            return mapper.readValue(jsonResponse.toString(), type);
        } catch (IOException e) {
            throw new RuntimeException("Error executing request", e);
        }
    }
}
