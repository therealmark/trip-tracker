package com.example.triptracker.client;

import com.example.triptracker.model.Itinerary;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import lombok.Data;
import org.bson.BsonValue;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import static com.mongodb.client.model.Filters.eq;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

/**
 *
 **/
@Component
@Data
public class DBClient {
    private MongoClient mongoClient;
    private MongoDatabase database;
    private MongoClientSettings clientSettings;
    private CodecRegistry pojoCodecRegistry = fromProviders(PojoCodecProvider.builder().automatic(true).build());;
    private CodecRegistry codecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(), pojoCodecRegistry);
    @Value("${mongodb.uri}")
    private String uri;
    @Value("${mongodb.database}")
    private String databaseName;

    @PostConstruct
    public void init() {
        clientSettings = MongoClientSettings.builder()
                .applyConnectionString(new ConnectionString(uri))
                .codecRegistry(codecRegistry)
                .build();
        mongoClient = MongoClients.create(clientSettings);
        database = mongoClient.getDatabase(databaseName);
    }

    @PreDestroy
    public void shutdown() {
        mongoClient.close();
    }

    public MongoCollection<Itinerary> getItineraryCollection() {
        return database.getCollection("itineraries", Itinerary.class);
    }

    public Itinerary insertItinerary(Itinerary itinerary) {
        BsonValue insertedId = getItineraryCollection().insertOne(itinerary).getInsertedId();
        return getItineraryCollection().find(eq("_id", insertedId)).first();
    }

    public String insertTicket(Itinerary itinerary) {
        return getTicketCollection().insertOne(itinerary).getInsertedId().asString().getValue();
    }

    private MongoCollection<Itinerary> getTicketCollection() {
        return database.getCollection("tickets", Itinerary.class);
    }

    public Itinerary findItineraryById(String id) {
        return getItineraryCollection().find(eq(new ObjectId(id))).first();
    }
}
