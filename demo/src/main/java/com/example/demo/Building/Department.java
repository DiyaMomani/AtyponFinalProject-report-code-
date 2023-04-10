package com.example.demo.Building;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Department {
    private String Id;
    private String roomNumber;
    private String bathroomNumber;
    @JsonProperty("Id")
    public String getId() {
        return Id;
    }

    public void setId(String Id) {
        this.Id = Id;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public String getBathroomNumber() {
        return bathroomNumber;
    }

    public void setBathroomNumber(String bathroomNumber) {
        this.bathroomNumber = bathroomNumber;
    }


    public Department(String departmentId, String roomNumber, String bathroomNumber) {
        this.Id = departmentId;
        this.roomNumber = roomNumber;
        this.bathroomNumber = bathroomNumber;
    }


}
