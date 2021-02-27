package com.safetynet.api.util;

public class Views {

    public interface BasicPerson {
    }

    public interface FireStationModel extends BasicPerson {
    }

    public interface ChildAlertModel extends BasicPerson {
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
