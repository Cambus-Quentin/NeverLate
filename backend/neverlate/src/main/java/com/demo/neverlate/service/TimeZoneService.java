package com.demo.neverlate.service;

import com.demo.neverlate.model.TimeZone;
import com.demo.neverlate.repository.TimeZoneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TimeZoneService {

    @Autowired
    private TimeZoneRepository timeZoneRepository;

    public List<TimeZone> findAll() {
        return timeZoneRepository.findAll();
    }

    public TimeZone findById(Long id) {
        return timeZoneRepository.findById(id).orElseThrow(() -> new RuntimeException("Time zone not found"));
    }

    public TimeZone save(TimeZone timeZone) {
        return timeZoneRepository.save(timeZone);
    }

    public void deleteById(Long id) {
        timeZoneRepository.deleteById(id);
    }
}
