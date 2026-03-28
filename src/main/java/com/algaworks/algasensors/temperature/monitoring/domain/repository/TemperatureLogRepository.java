package com.algaworks.algasensors.temperature.monitoring.domain.repository;

import com.algaworks.algasensors.temperature.monitoring.domain.model.SensorId;
import com.algaworks.algasensors.temperature.monitoring.domain.model.TemperatureLog;
import com.algaworks.algasensors.temperature.monitoring.domain.model.TemperatureLogId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TemperatureLogRepository extends JpaRepository<TemperatureLog, TemperatureLogId> {
    List<TemperatureLog> SensorId(SensorId sensorId);

    Page<TemperatureLog> findAllBySensorId(SensorId sensorId, Pageable pageable);
}
