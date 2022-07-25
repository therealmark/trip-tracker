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
    private String airline;
}
