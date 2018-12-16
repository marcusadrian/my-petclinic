package org.adrian.mypetclinic;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PetclinicApplication {

    public static final String DEV_PROFILE = "dev";
    public static final String NOT_DEV_PROFILE = "!" + DEV_PROFILE;

    public static void main(String[] args) {
        SpringApplication.run(PetclinicApplication.class, args);
    }
}
