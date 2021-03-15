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
    public boolean add(FireStation fireStation) {
        fireStations.add(fireStation);
        return true;
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
    public boolean update(FireStation entity) {
        // Remove previous
        FireStation prev = findOneByStation(entity.getStation());
        fireStations.remove(prev);

        // Set persons
        entity.setPersons(prev.getPersons());

        // Add the updated Entity
        fireStations.add(entity);
        return add(entity);
    }

    /**
     * Remove an entity
     */
    @Override
    public boolean remove(FireStation entity) {
        if(entity.getPersons().size() > 0){
            return false;
        }
        fireStations.remove(entity);
        return true;
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
