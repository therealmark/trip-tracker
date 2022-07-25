package com.example.triptracker;

import com.example.triptracker.service.BookingService;
import com.example.triptracker.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PreDestroy;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootApplication
public class TripTrackerApplication implements CommandLineRunner {
    @Autowired
    private TicketService ticketService;
    @Autowired
    private BookingService bookingService;
    private static ExecutorService executorService;

    public static void main(String[] args) {
        SpringApplication.run(TripTrackerApplication.class, args);
    }

    @Override
    public void run(String... args)  {
        executorService = Executors.newCachedThreadPool();
        executorService.submit(ticketService);
        executorService.submit(bookingService);
    }
    @PreDestroy
    public void shutdown() {
        executorService.shutdown();
    }
}