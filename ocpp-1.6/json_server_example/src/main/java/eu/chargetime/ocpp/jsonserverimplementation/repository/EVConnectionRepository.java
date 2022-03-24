package eu.chargetime.ocpp.jsonserverimplementation.repository;

import eu.chargetime.ocpp.jsonserverimplementation.entity.EVConnection;
import eu.chargetime.ocpp.jsonserverimplementation.type.ConnectionStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EVConnectionRepository extends JpaRepository<EVConnection, Long> {
    List<EVConnection> findAllBySessionId(String sessionId);
    List<EVConnection> findAllBySessionIdAndConnectionStatus(String sessionId, ConnectionStatus connectionStatus);
}
