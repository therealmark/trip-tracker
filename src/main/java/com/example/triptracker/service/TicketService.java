package com.example.triptracker.service;

import com.example.triptracker.client.DBClient;
import com.example.triptracker.model.Ticket;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.*;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 **/
@Service
public class TicketService {

    private DBClient dbClient;

    @Autowired
    public TicketService(DBClient dbClient) {
        this.dbClient = dbClient;
    }

    public Ticket upsertTicket(Ticket ticket) {
        MongoCollection<Ticket> tickets = this.dbClient.getMongoClient()
                .getDatabase("ticket_tracker").getCollection("tickets", Ticket.class);
        Bson filter = Filters.eq("pnr", ticket.getPnr());
        FindOneAndReplaceOptions findOneAndReplaceOptions = new FindOneAndReplaceOptions().upsert(true).returnDocument(ReturnDocument.AFTER);
        return tickets.findOneAndReplace(filter, ticket, findOneAndReplaceOptions);

    }
}
