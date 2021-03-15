package com.safetynet.api.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.safetynet.api.entity.FireStation;
import com.safetynet.api.exception.EntityNotFound;
import com.safetynet.api.model.*;
import com.safetynet.api.model.delete.DeleteFireStationModel;
import com.safetynet.api.model.rest.RestFireStationModel;
import com.safetynet.api.repository.FireStationRepository;
import com.safetynet.api.util.Views;
import org.springframework.web.bind.annotation.*;

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
     * <p>
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
     * <p>
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
     * <p>
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
     * <p>
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

    /**
     * Create a fireStation and link it with a fireStation
     * <p>
     * POST /fireStation
     * Json body
     * <p>
     * All parameters are required @see RestFireStationModel
     */
    @PostMapping("/firestation")
    @JsonView(Views.RestResultModel.class)
    RestResultModel addFireStation(@RequestBody RestFireStationModel model) {
        // check required parameters (station)
        if (model.getStation() == null) {
            return RestResultModel.buildError("POST", "fireStation", "Station Number should not be null");
        }

        // Search if the fireStation already exists
        FireStation existing = this.fireStationRepo.findOneByStation(model.getStation());

        // if no fireStation already exists and the fireStation
        if (existing == null) {
            try {
                // build the fireStation from the model
                FireStation toAdd = FireStation.fromModel(model);

                // add it to the repository
                boolean success = this.fireStationRepo.add(toAdd);
                if (success) {
                    return RestResultModel.buildSuccess("POST", "fireStation");
                } else {
                    return RestResultModel.buildError("POST", "fireStation", "Error when adding");
                }
            } catch (Exception e) {
                // Catch NullPointerExceptions (if a property is null)
                return RestResultModel.buildError("POST", "fireStation", e.getMessage());
            }
        } else {
            // if the fireStation already exists
            return RestResultModel.buildError("POST", "fireStation", "The station number should be unique");
        }
    }

    /**
     * Update a fireStation by it's firstName and lastName
     * <p>
     * PUT /fireStation
     * Json body
     * <p>
     * Two required parameters : firstName, lastName
     * All others are the same as for POST but they are optional
     */
    @PutMapping("/firestation")
    @JsonView(Views.RestResultModel.class)
    RestResultModel deleteFireStation(@RequestBody RestFireStationModel model) {
        // Check required parameters (station)
        if (model.getStation() == null) {
            return RestResultModel.buildError("PUT", "fireStation", "Station Number should not be null");
        }

        // if no change throw an error
        if (model.isEmpty()) {
            return RestResultModel.buildError("PUT", "fireStation", "Fail : no change");
        }

        // Retrieve the existing fireStation by it's name
        FireStation existing = this.fireStationRepo.findOneByStation(model.getStation());

        // Check that the fireStation already exist
        if (existing != null) {
            try {
                // Build FireStation entity from Model
                FireStation toUpdate = FireStation.fromModelAndDefault(model, existing);

                boolean success = fireStationRepo.update(toUpdate);

                if (success) {
                    return RestResultModel.buildSuccess("PUT", "fireStation");
                } else {
                    return RestResultModel.buildError("PUT", "fireStation", "Error when updating");
                }
            } catch (Exception e) {
                // Throw an error
                return RestResultModel.buildError("PUT", "fireStation", e.getMessage());
            }
        } else {
            // else Throw an error
            return RestResultModel.buildError("PUT", "fireStation", "The combination of firstName and lastName was not found");
        }
    }

    /**
     * Update a fireStation by it's firstName and lastName
     * <p>
     * PUT /fireStation
     * Json body
     * <p>
     * Two required parameters : firstName, lastName
     * All others are the same as for POST but they are optional
     */
    @DeleteMapping("/firestation")
    @JsonView(Views.RestResultModel.class)
    RestResultModel deleteFireStation(@RequestBody DeleteFireStationModel model) {
        // Check required parameters (station)
        if (model.getStation() == null) {
            return RestResultModel.buildError("DELETE", "fireStation", "Station Number should not be null");
        }

        // fetch the fireStation
        FireStation existing = fireStationRepo.findOneByStation(model.getStation());
        if (existing != null) {
            // if the fireStation was found
            boolean success = fireStationRepo.remove(existing);
            if (success) {
                return RestResultModel.buildSuccess("DELETE", "fireStation");
            } else {
                return RestResultModel.buildError("DELETE", "fireStation", "Error when removing");
            }
        } else {
            // the fireStation was not found : error
            return RestResultModel.buildError("DELETE", "fireStation", "The fireStation does not exists");
        }
    }
}
