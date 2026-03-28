package com.algaworks.algasensors.temperature.monitoring.api.controller;

import com.algaworks.algasensors.temperature.monitoring.api.model.SensorMonitoringOutput;
import com.algaworks.algasensors.temperature.monitoring.domain.model.SensorId;
import com.algaworks.algasensors.temperature.monitoring.domain.model.SensorMonitoring;
import com.algaworks.algasensors.temperature.monitoring.domain.repository.SensorMonitoringRepository;
import io.hypersistence.tsid.TSID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/api/sensors/{sensorId}/monitoring")
@RequiredArgsConstructor
public class SensorMonitoringController {
    private final SensorMonitoringRepository sensorMonitoringRepository;

    @GetMapping
    public SensorMonitoringOutput getDetail(@PathVariable TSID sensorId){
        SensorMonitoring sensor = findByIdOrDefault(sensorId);

        return SensorMonitoringOutput.builder()
                .id(sensor.getId().getValue())
                .enabled(sensor.getEnabled())
                .lastTemperature(sensor.getLastTemperature())
                .updateAt(sensor.getUpdateAt())
                .build();
    }

    @PutMapping("/enable")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void enable(@PathVariable TSID sensorId){
        SensorMonitoring sensor = findByIdOrDefault(sensorId);

        sensor.setEnabled(true);
        sensorMonitoringRepository.save(sensor);
    }

    @DeleteMapping("/enable")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void disabled(@PathVariable TSID sensorId){
        SensorMonitoring sensor = findByIdOrDefault(sensorId);

        sensor.setEnabled(false);
        sensorMonitoringRepository.save(sensor);
    }

    private SensorMonitoring findByIdOrDefault(TSID sensorId) {
        return sensorMonitoringRepository.findById(new SensorId(sensorId))
                .orElse(SensorMonitoring.builder()
                        .id(new SensorId(sensorId))
                        .enabled(false)
                        .lastTemperature(null)
                        .updateAt(null)
                        .build());
    }
}
