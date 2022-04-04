package eu.chargetime.ocpp.jsonserverimplementation.repository;

import eu.chargetime.ocpp.jsonserverimplementation.entity.EVMeterValues;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EVMeterValuesRepository extends JpaRepository<EVMeterValues, Long> {
    EVMeterValues findBySessionId(Long sessionId);
}
