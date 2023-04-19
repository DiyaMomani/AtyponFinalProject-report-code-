package com.example.demo.Building;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Store {
    private String Id;
    private String area;
    private String height;
    @JsonProperty("Id")
    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public Store(String Id,String area, String height) {
        this.area = area;
        this.height = height;
        this.Id=Id;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }
}
