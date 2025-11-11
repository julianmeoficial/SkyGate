package com.skygate.backend.service.flight;

import com.skygate.backend.model.entity.Aircraft;
import com.skygate.backend.model.entity.Flight;
import com.skygate.backend.model.enums.AircraftType;
import com.skygate.backend.repository.AircraftRepository;
import com.skygate.backend.service.automata.AutomataService;
import com.skygate.backend.exception.AircraftNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.Random;
import java.util.concurrent.CompletableFuture;

@Service
public class FlightDetectionService {

    private static final Logger logger = LoggerFactory.getLogger(FlightDetectionService.class);

    private final FlightService flightService;
    private final AircraftRepository aircraftRepository;
    private final AutomataService automataService;
    private final Random random;

    public FlightDetectionService(
            FlightService flightService,
            AircraftRepository aircraftRepository,
            @Lazy AutomataService automataService) {
        this.flightService = flightService;
        this.aircraftRepository = aircraftRepository;
        this.automataService = automataService;
        this.random = new Random();
    }

    @Transactional
    public Flight detectFlight(String flightNumber, String aircraftModel, String origin, String destination, String airline) {
        logger.info("Detecting new flight: {} with aircraft model: {}", flightNumber, aircraftModel);

        Aircraft aircraft = aircraftRepository.findByModel(aircraftModel)
                .orElseThrow(() -> new AircraftNotFoundException(aircraftModel, true));

        Flight flight = new Flight();
        flight.setFlightNumber(flightNumber);
        flight.setAircraft(aircraft);
        flight.setOrigin(origin);
        flight.setDestination(destination);
        flight.setAirline(airline);
        flight.setScheduledArrival(LocalDateTime.now().plusMinutes(30));
        flight.setScheduledDeparture(LocalDateTime.now().plusHours(2));

        Flight createdFlight = flightService.createFlight(flight);

        automataService.processFlightDetection(createdFlight);

        logger.info("Flight detected and processed: {}", createdFlight.getFlightNumber());
        return createdFlight;
    }

    @Transactional
    public Flight detectFlightByAircraftType(String flightNumber, AircraftType aircraftType, String origin, String destination, String airline) {
        logger.info("Detecting flight: {} with aircraft type: {}", flightNumber, aircraftType);

        Aircraft aircraft = aircraftRepository.findByAircraftType(aircraftType).stream()
                .findFirst()
                .orElseThrow(() -> new AircraftNotFoundException("No aircraft found for type: " + aircraftType));

        return detectFlight(flightNumber, aircraft.getModel(), origin, destination, airline);
    }

    @Transactional
    public Flight simulateFlightDetection() {
        String flightNumber = generateRandomFlightNumber();
        AircraftType randomType = getRandomAircraftType();
        String origin = generateRandomCity();
        String destination = generateRandomCity();
        String airline = generateRandomAirline();

        logger.info("Simulating flight detection: {} - Type: {}", flightNumber, randomType);

        return detectFlightByAircraftType(flightNumber, randomType, origin, destination, airline);
    }

    @Async
    @Transactional
    public CompletableFuture<Flight> simulateFlightDetectionAsync() {
        logger.info("Starting async flight detection simulation");
        Flight flight = simulateFlightDetection();
        return CompletableFuture.completedFuture(flight);
    }

    @Transactional
    public void simulateFlightArrival(Long flightId) {
        logger.info("Simulating arrival for flight ID: {}", flightId);
        Flight flight = flightService.getFlightById(flightId);
        automataService.processAircraftArrival(flight);
    }

    @Transactional
    public void simulateFlightDeparture(Long flightId) {
        logger.info("Simulating departure for flight ID: {}", flightId);
        Flight flight = flightService.getFlightById(flightId);
        automataService.processAircraftDeparture(flight);
    }

    private String generateRandomFlightNumber() {
        String[] airlines = {"AV", "LA", "CM", "AA", "DL", "UA", "BA"};
        String airline = airlines[random.nextInt(airlines.length)];
        int number = 100 + random.nextInt(900);
        return airline + number;
    }

    private AircraftType getRandomAircraftType() {
        AircraftType[] types = {AircraftType.NARROW_BODY, AircraftType.WIDE_BODY, AircraftType.JUMBO};
        int[] weights = {60, 30, 10};

        int totalWeight = 0;
        for (int weight : weights) {
            totalWeight += weight;
        }

        int randomValue = random.nextInt(totalWeight);
        int currentWeight = 0;

        for (int i = 0; i < types.length; i++) {
            currentWeight += weights[i];
            if (randomValue < currentWeight) {
                return types[i];
            }
        }

        return AircraftType.NARROW_BODY;
    }

    private String generateRandomCity() {
        String[] cities = {"Bogota", "Istambul", "Doha", "Dubai", "Medellin", "Paris", "Cali", "Cartagena", "Miami", "New York",
                "Madrid", "Barcelona", "Mexico City", "Lima", "Buenos Aires", "Sao Paulo"};
        return cities[random.nextInt(cities.length)];
    }

    private String generateRandomAirline() {
        String[] airlines = {"Qatar Airways", "Emirates", "Turkish Airlines","Air France", "Avianca", "LATAM", "Copa Airlines", "American Airlines",
                "Delta", "United", "Iberia", "Aeromexico"};
        return airlines[random.nextInt(airlines.length)];
    }
}
