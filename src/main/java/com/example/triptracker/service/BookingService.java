package com.example.triptracker.service;

import com.example.triptracker.client.DBClient;
import com.example.triptracker.model.Booking;
import com.example.triptracker.model.Itinerary;
import com.mongodb.client.ChangeStreamIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.CreateCollectionOptions;
import com.mongodb.client.model.TimeSeriesOptions;
import com.mongodb.client.model.changestream.ChangeStreamDocument;
import com.mongodb.client.model.changestream.FullDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 *
 **/
@Service
public class BookingService implements Runnable {

    private DBClient dbClient;

    @Autowired
    public BookingService(DBClient dbClient) {
        this.dbClient = dbClient;
    }

    private void streamBookings() {
        ChangeStreamIterable<Itinerary> changeStream = dbClient.getItineraryCollection().watch().fullDocument(FullDocument.UPDATE_LOOKUP);
        for (ChangeStreamDocument<Itinerary> itineraryChangeStreamDocument : changeStream) {
            insertBookings(itineraryChangeStreamDocument.getFullDocument().getBookings());
        }
    }

    private void insertBookings(List<Booking> allBookings) {
        MongoCollection<Booking> bookings = this.dbClient.getMongoClient()
                .getDatabase("ticket_tracker").getCollection("bookings", Booking.class);
        bookings.insertMany(allBookings);
    }

    @Override
    public void run() {
        streamBookings();
    }

    @PostConstruct
    public void init() {
        MongoDatabase db = this.dbClient.getMongoClient().getDatabase("ticket_tracker");
        db.getCollection("bookings").drop();
        TimeSeriesOptions timeSeriesOptions = new TimeSeriesOptions("bookedOn");
        CreateCollectionOptions collectionOptions = new CreateCollectionOptions().timeSeriesOptions(timeSeriesOptions);
        db.createCollection("bookings", collectionOptions);

    }
}
