package com.algaworks.algasensors.temperature.monitoring.domain.service;

import com.algaworks.algasensors.temperature.monitoring.api.model.TemperatureLogOutput;
import com.algaworks.algasensors.temperature.monitoring.domain.model.SensorId;
import com.algaworks.algasensors.temperature.monitoring.domain.model.SensorMonitoring;
import com.algaworks.algasensors.temperature.monitoring.domain.model.TemperatureLog;
import com.algaworks.algasensors.temperature.monitoring.domain.model.TemperatureLogId;
import com.algaworks.algasensors.temperature.monitoring.domain.repository.SensorMonitoringRepository;
import com.algaworks.algasensors.temperature.monitoring.domain.repository.TemperatureLogRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class TemperatureMonitoringService {

    private final TemperatureLogRepository temperatureLogRepository;
    private final SensorMonitoringRepository sensorMonitoringRepository;

    @Transactional
    public void processTemperatureReading(TemperatureLogOutput temperatureLogOutput) {
        sensorMonitoringRepository.findById(new SensorId(temperatureLogOutput.getSensorID()))
                .ifPresentOrElse(sensorMonitoring -> handleSensorMonitoring(temperatureLogOutput, sensorMonitoring),
                        this::logIgnoredTemperature
                );
    }

    private void logIgnoredTemperature() {
        log.info("Ignoring temperature reading");
    }

    private void handleSensorMonitoring(TemperatureLogOutput temperatureLogOutput, SensorMonitoring sensor) {
        if(sensor.isEnabled()){
            sensor.setLastTemperature(temperatureLogOutput.getValue());
            sensor.setUpdateAt(OffsetDateTime.now());
            sensorMonitoringRepository.save(sensor);

            TemperatureLog temperatureLog = TemperatureLog.builder()
                    .id(new TemperatureLogId(temperatureLogOutput.getId()))
                    .sensorId(new SensorId(temperatureLogOutput.getSensorID()))
                    .value(temperatureLogOutput.getValue())
                    .registeredAt(OffsetDateTime.now())
                    .build();
            temperatureLogRepository.save(temperatureLog);
            log.info("Temperature Monitoring has been updated");
        } else {
            logIgnoredTemperature();
        }
    }

}
