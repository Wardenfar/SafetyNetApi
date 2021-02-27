package com.safetynet.api.repository;

import com.safetynet.api.entity.FireStation;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class FireStationRepository {

    private Set<FireStation> fireStations = new HashSet<>();

    public void add(FireStation fireStation) {
        fireStations.add(fireStation);
    }

    public int count() {
        return fireStations.size();
    }

    public Set<FireStation> findAll() {
        return Collections.unmodifiableSet(fireStations);
    }

    public FireStation findOneByStation(String station) {
        return fireStations.stream()
                .filter(fs -> fs.getStation().equals(station))
                .findFirst()
                .orElse(null);
    }

    public FireStation findOneByAddress(String address) {
        return fireStations.stream()
                .filter(fs -> fs.getAddress().equals(address))
                .findFirst()
                .orElse(null);
    }
}
