package com.safetynet.api.util;

public class Views {
    public interface FireStationModel {
    }

    public interface Public {
    }

    public interface Person {
    }

    public interface FireStation {
    }

    public interface MedicalRecord {
    }

    public interface PublicAndPerson extends Public, Person {
    }

    public interface PublicAndFireStation extends Public, FireStation {
    }
}
