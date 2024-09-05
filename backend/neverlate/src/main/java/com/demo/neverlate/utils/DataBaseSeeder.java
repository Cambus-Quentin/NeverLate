package com.demo.neverlate.utils;

import com.demo.neverlate.model.TimeZone;
import com.demo.neverlate.repository.TimeZoneRepository;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import jakarta.inject.Inject;

@Singleton
@Startup
public class DatabaseSeeder {

    @Inject
    private TimeZoneRepository timeZoneRepository;

    @PostConstruct
    public void seedDatabase() {
        TimeZone tz1 = new TimeZone();
        tz1.setName("Eastern Standard Time");
        tz1.setCity("New York");
        tz1.setOffset("-05:00");
        timeZoneRepository.save(tz1);

        TimeZone tz2 = new TimeZone();
        tz2.setName("Central European Time");
        tz2.setCity("Berlin");
        tz2.setOffset("+01:00");
        timeZoneRepository.save(tz2);
    }
}
