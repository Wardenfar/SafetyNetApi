package com.safetynet.api.repository;

import com.safetynet.api.entity.FireStation;
import org.tinylog.Logger;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * The repository implementation for FireStation Entity
 */
public class FireStationRepository extends AbstractRepository<FireStation> {

    // List of fireStations in this repository
    private Set<FireStation> fireStations = new HashSet<>();

    /**
     * Add a FireStation
     *
     * @param entity toAdd
     */
    @Override
    public boolean add(FireStation entity) {
        Logger.debug("Adding FireStation : " + entity.toString());
        fireStations.add(entity);
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
        Logger.debug("Updating FireStation : " + entity.toString());

        // Remove previous
        FireStation prev = findOneByAddress(entity.getAddress());
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
        Logger.debug("Removing FireStation : " + entity.toString());

        if(entity.getPersons().size() > 0){
            Logger.error("Could not remove FireStation with one or more Persons");
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
    public List<FireStation> findAllByStation(String station) {
        return fireStations.stream()
                .filter(fs -> fs.getStation().equals(station))
                .collect(Collectors.toList());
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
