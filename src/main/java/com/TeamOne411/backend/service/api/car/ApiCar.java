package com.TeamOne411.backend.service.api.car;

import com.fasterxml.jackson.annotation.JsonAlias;

import java.io.Serializable;

//@JsonIgnoreProperties(ignoreUnknown = true)
public class ApiCar implements Serializable {

    @JsonAlias({"Make_ID", "MakeId"})
    private int makeId;
    @JsonAlias({"Make_Name", "MakeName"})
    private String makeName;
    @JsonAlias({"Model_ID", "ModelId"})
    private int modelId;
    @JsonAlias({"Model_Name", "ModelName"})
    private String modelName;

    public int getMakeId() {
        return makeId;
    }

    public void setMakeId(int makeId) {
        this.makeId = makeId;
    }

    public String getMakeName() {
        return makeName;
    }

    public void setMakeName(String makeName) {
        this.makeName = makeName;
    }

    public int getModelId() {
        return modelId;
    }

    public void setModelId(int modelId) {
        this.modelId = modelId;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }


}
