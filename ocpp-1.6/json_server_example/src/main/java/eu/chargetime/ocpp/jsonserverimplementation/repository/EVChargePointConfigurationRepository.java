package eu.chargetime.ocpp.jsonserverimplementation.repository;

import eu.chargetime.ocpp.jsonserverimplementation.entity.EVChargePointConfiguration;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EVChargePointConfigurationRepository extends JpaRepository<EVChargePointConfiguration, Long> {
    EVChargePointConfiguration findByChargeBoxSerialNumber(String chargeBoxSerialNumber);
}
