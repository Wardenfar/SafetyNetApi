package com.safetynet.api.util;

public class Views {

    public interface PersonFirstName {
    }

    public interface PersonLastName {
    }

    public interface PersonPhone {
    }

    public interface PersonAge {
    }

    public interface PersonEmail {
    }

    public interface PersonAddress {
    }

    public interface PersonFireStation {
    }

    public interface PersonMedicalRecord {
    }

    public interface FireStationProperties {
    }

    public interface FireStationRelations {
    }

    public interface MedicalRecordProperties {
    }

    public interface MedicalRecordRelations {
    }

    public interface FireStationModel extends PersonFirstName, PersonLastName, PersonAddress, PersonPhone, PersonAge {
    }

    public interface ChildAlertModel extends PersonFirstName, PersonLastName, PersonAge {
    }

    public interface FireModel extends FireStationProperties, PersonFirstName, PersonLastName, PersonPhone, PersonAge, PersonMedicalRecord, MedicalRecordProperties {
    }

    public interface PersonInfoModel extends PersonFirstName, PersonLastName, PersonAddress, PersonAge, PersonEmail, PersonMedicalRecord, MedicalRecordProperties {
    }

    public interface FloodStationsModel extends FireStationProperties, PersonFirstName, PersonLastName, PersonPhone, PersonAge, PersonMedicalRecord, MedicalRecordProperties {
    }
}
