package com.example.triptracker.service;

import com.example.triptracker.client.DBClient;
import com.example.triptracker.model.Booking;
import com.example.triptracker.model.Itinerary;
import com.example.triptracker.model.Ticket;
import com.mongodb.client.ChangeStreamIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.FindOneAndReplaceOptions;
import com.mongodb.client.model.ReturnDocument;
import com.mongodb.client.model.changestream.ChangeStreamDocument;
import com.mongodb.client.model.changestream.FullDocument;
import org.bson.conversions.Bson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Comparator;

/**
 *
 **/
@Service
public class TicketService implements Runnable {
    private DBClient dbClient;

    @Autowired
    public TicketService(DBClient dbClient) {
        this.dbClient = dbClient;
    }
    public void streamTickets() {
        ChangeStreamIterable<Itinerary> changeStream = dbClient.getItineraryCollection().watch().fullDocument(FullDocument.UPDATE_LOOKUP);

        for (ChangeStreamDocument<Itinerary> itineraryChangeStreamDocument : changeStream) {
            Ticket ticket = new Ticket();
            ticket.setPnr(itineraryChangeStreamDocument.getFullDocument().getPassengerNameRecord());
            Booking oldestBooking = itineraryChangeStreamDocument.getFullDocument().getBookings().stream().min(Comparator.comparing(Booking::getBookedOn)).get();
            Booking latestBooking = itineraryChangeStreamDocument.getFullDocument().getBookings().stream().max(Comparator.comparing(Booking::getBookedOn)).get();
            ticket.setOldestPrice(oldestBooking.getPrice());
            ticket.setLatestPrice(latestBooking.getPrice());
            ticket.setSavings(oldestBooking.getPrice() - latestBooking.getPrice());
            System.out.println(String.format("Updated %s", upsertTicket(ticket)));
        }
    }

    private Ticket upsertTicket(Ticket ticket) {
        MongoCollection<Ticket> tickets = this.dbClient.getMongoClient()
                .getDatabase("ticket_tracker").getCollection("tickets", Ticket.class);
        Bson filter = Filters.eq("pnr", ticket.getPnr());
        FindOneAndReplaceOptions findOneAndReplaceOptions = new FindOneAndReplaceOptions().upsert(true).returnDocument(ReturnDocument.AFTER);
        return tickets.findOneAndReplace(filter, ticket, findOneAndReplaceOptions);

    }

    @PostConstruct
    public void init() {
//        MongoDatabase db = this.dbClient.getMongoClient().getDatabase("ticket_tracker");
//        db.getCollection("tickets").drop();
    }

    @Override
    public void run() {
        streamTickets();
    }
}
