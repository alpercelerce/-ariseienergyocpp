package eu.chargetime.ocpp.jsonserverimplementation.repository;

import eu.chargetime.ocpp.jsonserverimplementation.entity.EVTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EVTransactionRepository extends JpaRepository<EVTransaction, Long> {
    List<EVTransaction> findBySessionId(Long sessionId);
}
