package com.safetynet.api.repository;

import com.safetynet.api.entity.FireStation;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * The repository implementation for FireStation Entity
 */
public class FireStationRepository extends AbstractRepository<FireStation> {

    // List of fireStations in this repository
    private Set<FireStation> fireStations = new HashSet<>();

    /**
     * Add a FireStation
     *
     * @param fireStation toAdd
     */
    @Override
    public void add(FireStation fireStation) {
        fireStations.add(fireStation);
    }

    /**
     * Count the number of fireStations
     */
    @Override
    public int count() {
        return fireStations.size();
    }

    /**
     * Clear the repo
     */
    @Override
    public void clear() {
        fireStations.clear();
    }

    /**
     * Update an entity
     */
    @Override
    public void update(FireStation entity) {

    }

    /**
     * Remove an entity
     */
    @Override
    public void remove(FireStation entity) {

    }

    /**
     * find all FireStations
     */
    public Set<FireStation> findAll() {
        return Collections.unmodifiableSet(fireStations);
    }

    /**
     * return one FireStation by stationNumber
     */
    public FireStation findOneByStation(String station) {
        return fireStations.stream()
                .filter(fs -> fs.getStation().equals(station))
                .findFirst()
                .orElse(null);
    }

    /**
     * return one FireStation by address
     */
    public FireStation findOneByAddress(String address) {
        return fireStations.stream()
                .filter(fs -> fs.getAddress().equals(address))
                .findFirst()
                .orElse(null);
    }
}
