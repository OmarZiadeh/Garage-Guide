package com.TeamOne411.backend.service.api.car;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ApiCarService {
    private RestTemplate restTemplate = new RestTemplate();
    private String baseUri = "https://vpic.nhtsa.dot.gov/api/";


    public List<String> getAllMakes() throws URISyntaxException {

        URI fullUri = new URI(baseUri + "vehicles/GetMakesForVehicleType/car?format=json");
        ApiCarResponse carsResponse = restTemplate.getForObject(fullUri, ApiCarResponse.class);

        List<ApiCar> cars = carsResponse.getResults();

        return cars.stream().map(c->c.getMakeName()).collect(Collectors.toList());
    }
}
