package com.example.triptracker;

import com.example.triptracker.model.Booking;
import com.example.triptracker.model.Itinerary;
import com.example.triptracker.model.Ticket;
import com.example.triptracker.service.ItineraryService;
import com.example.triptracker.service.TicketService;
import com.mongodb.client.ChangeStreamIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.changestream.ChangeStreamDocument;
import com.mongodb.client.model.changestream.FullDocument;
import org.bson.conversions.Bson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Comparator;
import java.util.List;

@SpringBootApplication
public class TripTrackerApplication implements CommandLineRunner {

    @Autowired
    private ItineraryService itineraryService;
    @Autowired
    TicketService ticketService;

    public static void main(String[] args) {
        SpringApplication.run(TripTrackerApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

        MongoCollection<Itinerary> itineraries = itineraryService.getItineraryCollection();
        List<Bson> pipeline = ;

        ChangeStreamIterable<Itinerary> changeStream = itineraries.watch(pipeline).fullDocument(FullDocument.UPDATE_LOOKUP);

        for (ChangeStreamDocument<Itinerary> itineraryChangeStreamDocument : changeStream) {
            Ticket ticket = new Ticket();
            ticket.setPnr(itineraryChangeStreamDocument.getFullDocument().getPassengerNameRecord());
            Booking latestBooking = itineraryChangeStreamDocument.getFullDocument().getBookings().stream().min(Comparator.comparing(Booking::getBookedOn)).get();
            ticket.setPrice(latestBooking.getPrice());
            ticket.setBookedOnDate(latestBooking.getBookedOn());
            System.out.println(String.format("Updated %s", ticketService.upsertTicket(ticket)));
        }
    }
}
