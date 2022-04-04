package eu.chargetime.ocpp.jsonserverimplementation.repository;

import eu.chargetime.ocpp.jsonserverimplementation.entity.EVChargingStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EVChargingStatusRepository extends JpaRepository<EVChargingStatus, Long> {
    EVChargingStatus findBySessionId(Long sessionId);
}
