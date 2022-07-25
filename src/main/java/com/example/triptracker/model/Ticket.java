package com.example.triptracker.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;

/**
 *
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Ticket {
    private ObjectId id;
    private String pnr;
    private Integer latestPrice;
    private Integer oldestPrice;
    private Integer savings;

}
