package com.demo.neverlate.controller;

import com.demo.neverlate.model.TimeZone;
import com.demo.neverlate.service.TimeZoneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/timezones")
public class TimeZoneController {

    @Autowired
    private TimeZoneService timeZoneService;

    @GetMapping
    public List<TimeZone> getAllTimeZones() {
        return timeZoneService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<TimeZone> getTimeZoneById(@PathVariable Long id) {
        return ResponseEntity.ok(timeZoneService.findById(id));
    }

    @PostMapping
    public TimeZone createTimeZone(@RequestBody TimeZone timeZone) {
        return timeZoneService.save(timeZone);
    }

    @PutMapping("/{id}")
    public TimeZone updateTimeZone(@PathVariable Long id, @RequestBody TimeZone updatedTimeZone) {
        TimeZone timeZone = timeZoneService.findById(id);
        timeZone.setName(updatedTimeZone.getName());
        timeZone.setCity(updatedTimeZone.getCity());
        timeZone.setOffset(updatedTimeZone.getOffset());
        return timeZoneService.save(timeZone);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTimeZone(@PathVariable Long id) {
        timeZoneService.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
