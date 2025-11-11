package com.skygate.backend.repository;

import com.skygate.backend.model.entity.Aircraft;
import com.skygate.backend.model.enums.AircraftType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface AircraftRepository extends JpaRepository<Aircraft, Long> {

    Optional<Aircraft> findByModel(String model);

    List<Aircraft> findByAircraftType(AircraftType aircraftType);

    List<Aircraft> findByManufacturer(String manufacturer);

    List<Aircraft> findByAircraftTypeAndManufacturer(AircraftType aircraftType, String manufacturer);

    @Query("SELECT a FROM Aircraft a WHERE LOWER(a.model) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR LOWER(a.manufacturer) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<Aircraft> searchByModelOrManufacturer(@Param("searchTerm") String searchTerm);

    @Query("SELECT a FROM Aircraft a WHERE a.maxPassengers >= :minPassengers AND a.maxPassengers <= :maxPassengers")
    List<Aircraft> findByPassengerCapacityRange(@Param("minPassengers") Integer minPassengers, @Param("maxPassengers") Integer maxPassengers);

    @Query("SELECT a FROM Aircraft a WHERE a.wingspan >= :minWingspan")
    List<Aircraft> findByMinWingspan(@Param("minWingspan") Double minWingspan);

    @Query("SELECT COUNT(a) FROM Aircraft a WHERE a.aircraftType = :aircraftType")
    long countByAircraftType(@Param("aircraftType") AircraftType aircraftType);

    boolean existsByModel(String model);

    @Query("SELECT DISTINCT a.manufacturer FROM Aircraft a ORDER BY a.manufacturer")
    List<String> findAllManufacturers();

    @Query("SELECT a FROM Aircraft a LEFT JOIN FETCH a.flights WHERE a.id = :aircraftId")
    Optional<Aircraft> findByIdWithFlights(@Param("aircraftId") Long aircraftId);

    @Query("SELECT a FROM Aircraft a WHERE a.aircraftType = :aircraftType ORDER BY a.maxPassengers DESC")
    List<Aircraft> findByTypeOrderByCapacityDesc(@Param("aircraftType") AircraftType aircraftType);
}
