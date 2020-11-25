package com.TeamOne411.backend.service.api.car;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ApiVehicleResponse implements Serializable {

    @JsonProperty("Count")
    private int count;
    @JsonProperty("Message")
    private String message;
    @JsonProperty("SearchCriteria")
    private String searchCriteria;
    @JsonProperty("Results")
    private List<ApiVehicle> results;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSearchCriteria() {
        return searchCriteria;
    }

    public void setSearchCriteria(String searchCriteria) {
        this.searchCriteria = searchCriteria;
    }

    public List<ApiVehicle> getResults() {
        return results;
    }

    public void setResults(List<ApiVehicle> results) {
        this.results = results;
    }
}
