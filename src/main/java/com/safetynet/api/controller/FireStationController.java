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

@RestController
public class FireStationController {

    FireStationRepository fireStationRepo;

    public FireStationController(FireStationRepository fireStationRepo) {
        this.fireStationRepo = fireStationRepo;
    }

    @GetMapping("/firestation")
    @JsonView(Views.FireStationModel.class)
    FireStationModel fireStation(@RequestParam String stationNumber) throws EntityNotFound {
        FireStation fireStation = this.fireStationRepo.findOneByStation(stationNumber);
        if (fireStation == null) {
            throw new EntityNotFound("FireStation not found by station number : " + stationNumber);
        }
        return FireStationModel.build(fireStation);
    }

    @GetMapping("/phoneAlert")
    PhoneAlertModel phoneAlert(@RequestParam String stationNumber) throws EntityNotFound {
        FireStation fireStation = this.fireStationRepo.findOneByStation(stationNumber);
        if (fireStation == null) {
            throw new EntityNotFound("FireStation not found by station number : " + stationNumber);
        }
        return PhoneAlertModel.build(fireStation.getPersons());
    }

    @GetMapping("/fire")
    @JsonView(Views.FireModel.class)
    FireModel fire(@RequestParam String address) throws EntityNotFound {
        FireStation fireStation = this.fireStationRepo.findOneByAddress(address);
        if (fireStation == null) {
            throw new EntityNotFound("FireStation not found by address : " + address);
        }
        return FireModel.build(fireStation);
    }


    @GetMapping("/flood/stations")
    @JsonView(Views.FloodStationsModel.class)
    FloodStationsModel floodStations(@RequestParam List<String> stations) throws EntityNotFound {
        Set<FireStation> fireStations = new HashSet<>();
        for (String station : stations) {
            FireStation fireStation = this.fireStationRepo.findOneByStation(station);
            if (fireStation == null) {
                throw new EntityNotFound("FireStation not found by station number : " + station);
            } else {
                fireStations.add(fireStation);
            }
        }
        return FloodStationsModel.build(fireStations);
    }
}
