package com.TeamOne411.backend.service.api.car;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;

@Service
public class ApiCarService {
    private RestTemplate restTemplate = new RestTemplate();
    private String baseUri = "https://vpic.nhtsa.dot.gov/api/";


    public List<String> getAllMakes() throws URISyntaxException {

        URI fullUri = new URI(baseUri + "vehicles/GetAllMakes?format=json");
        ApiCarResponse carsResponse = restTemplate.getForObject(fullUri, ApiCarResponse.class);

        List<ApiCar> cars = Arrays.asList(carsResponse.getResults());

        return (List<String>) cars.stream().map(c->c.getMake_Name());


    }
}
