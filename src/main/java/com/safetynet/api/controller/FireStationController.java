package com.safetynet.api.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.safetynet.api.entity.FireStation;
import com.safetynet.api.exception.EntityNotFound;
import com.safetynet.api.model.FireModel;
import com.safetynet.api.model.FireStationModel;
import com.safetynet.api.model.FloodStationsModel;
import com.safetynet.api.model.PhoneAlertModel;
import com.safetynet.api.repository.FireStationRepository;
import com.safetynet.api.util.Views;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * RestController for routes related with the FireStation entity
 */
@RestController
public class FireStationController {

    FireStationRepository fireStationRepo;

    /**
     * Constructor
     */
    public FireStationController(FireStationRepository fireStationRepo) {
        this.fireStationRepo = fireStationRepo;
    }

    /**
     * GET /firestation?stationNumber=?
     *
     * Return information about this fireStation
     */
    @GetMapping("/firestation")
    @JsonView(Views.FireStationModel.class)
    FireStationModel fireStation(@RequestParam String stationNumber) throws EntityNotFound {
        // find the fireStation by its station number
        FireStation fireStation = this.fireStationRepo.findOneByStation(stationNumber);
        if (fireStation == null) { // if no fireStation found, then throw and error
            throw new EntityNotFound("FireStation not found by station number : " + stationNumber);
        }
        // else build and return the model
        return FireStationModel.build(fireStation);
    }

    /**
     * GET /phoneAlert?stationNumber=?
     *
     * Return all person's phone linked to this fireStation
     */
    @GetMapping("/phoneAlert")
    PhoneAlertModel phoneAlert(@RequestParam String stationNumber) throws EntityNotFound {
        // find the fireStation by its station number
        FireStation fireStation = this.fireStationRepo.findOneByStation(stationNumber);
        if (fireStation == null) { // if no fireStation found, then throw and error
            throw new EntityNotFound("FireStation not found by station number : " + stationNumber);
        }
        // else build and return the model
        return PhoneAlertModel.build(fireStation.getPersons());
    }

    /**
     * GET /fire?address=?
     *
     * Return all information about the fireStation and the persons of this address
     */
    @GetMapping("/fire")
    @JsonView(Views.FireModel.class)
    FireModel fire(@RequestParam String address) throws EntityNotFound {
        // find the fireStation by its station number
        FireStation fireStation = this.fireStationRepo.findOneByAddress(address);
        if (fireStation == null) { // if no fireStation found, then throw and error
            throw new EntityNotFound("FireStation not found by address : " + address);
        }
        // else build and return the model
        return FireModel.build(fireStation);
    }

    /**
     * GET /flood/stations?station=?,?,?
     *
     * Return information about all asked stations
     */
    @GetMapping("/flood/stations")
    @JsonView(Views.FloodStationsModel.class)
    FloodStationsModel floodStations(@RequestParam List<String> stations) throws EntityNotFound {
        Set<FireStation> fireStations = new HashSet<>();
        // for each parameter
        for (String station : stations) {
            // find the station
            FireStation fireStation = this.fireStationRepo.findOneByStation(station);
            if (fireStation == null) { // if one fireStation is missing : throw an error
                throw new EntityNotFound("FireStation not found by station number : " + station);
            } else {
                // else add it to the list
                fireStations.add(fireStation);
            }
        }
        // build and return the model from the list
        return FloodStationsModel.build(fireStations);
    }
}
