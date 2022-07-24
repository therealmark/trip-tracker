package com.example.triptracker.service;

import com.example.triptracker.client.DBClient;
import com.example.triptracker.model.Itinerary;
import com.mongodb.client.MongoCollection;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;

/**
 *
 **/
@Service
@DependsOn(value = "DBClient")
@Getter
public class ItineraryService {

    private DBClient dbClient;

    @Autowired
    public ItineraryService(DBClient dbClient) {
        this.dbClient = dbClient;
    }

    public Itinerary createItinerary(Itinerary itinerary) {
        return dbClient.insertItinerary(itinerary);
    }

    public Itinerary findItineraryById(String id) {
        return dbClient.findItineraryById(id);
    }

    public MongoCollection<Itinerary> getItineraryCollection() {
        return dbClient.getItineraryCollection();
    }

}
