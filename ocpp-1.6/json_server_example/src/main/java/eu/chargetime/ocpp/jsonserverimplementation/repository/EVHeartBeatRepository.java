package eu.chargetime.ocpp.jsonserverimplementation.repository;

import eu.chargetime.ocpp.jsonserverimplementation.entity.EVHeartbeat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EVHeartBeatRepository extends JpaRepository<EVHeartbeat, Long> {
}
