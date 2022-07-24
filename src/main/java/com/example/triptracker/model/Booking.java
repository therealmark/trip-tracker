package com.example.triptracker.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 *
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Booking {
    private Date bookedOn;
    private String departingAirport;
    private String arrivingAirport;
    private Integer price;

    public Booking setBookedOn(Date bookedOn) {
        this.bookedOn = bookedOn;
        return this;
    }

    public Booking setDepartingAirport(String departingAirport) {
        this.departingAirport = departingAirport;
        return this;
    }

    public Booking setArrivingAirport(String arrivingAirport) {
        this.arrivingAirport = arrivingAirport;
        return this;
    }

    public Booking setPrice(Integer price) {
        this.price = price;
        return this;
    }
}
