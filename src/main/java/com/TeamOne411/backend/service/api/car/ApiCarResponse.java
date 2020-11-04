package com.TeamOne411.backend.service.api.car;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ApiCarResponse implements Serializable {

    private int Count;
    private String Message;
    private String SearchCriteria;
    private ApiCar[] Results;

    public int getCount() {
        return Count;
    }

    public void setCount(int count) {
        Count = count;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public String getSearchCriteria() {
        return SearchCriteria;
    }

    public void setSearchCriteria(String searchCriteria) {
        SearchCriteria = searchCriteria;
    }

    public ApiCar[] getResults() {
        return Results;
    }

    public void setResults(ApiCar[] results) {
        Results = results;
    }
}
