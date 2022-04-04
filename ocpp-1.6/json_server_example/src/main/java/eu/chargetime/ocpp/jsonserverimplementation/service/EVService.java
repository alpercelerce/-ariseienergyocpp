package eu.chargetime.ocpp.jsonserverimplementation.service;

import eu.chargetime.ocpp.model.core.BootNotificationRequest;
import eu.chargetime.ocpp.model.core.MeterValuesRequest;
import eu.chargetime.ocpp.model.core.StartTransactionRequest;
import eu.chargetime.ocpp.model.core.StatusNotificationRequest;
import eu.chargetime.ocpp.model.core.StopTransactionRequest;

import java.util.UUID;

public interface EVService {
    void startConnection(String sessionId, String identifier);

    void stopConnection(String sessionId);

    void heartBeat(String sessionId, boolean validate);

    void bootNotification(UUID sessionIndex, BootNotificationRequest request);

    void chargingStatus(UUID sessionIndex, StatusNotificationRequest request);

    void meterValues(UUID sessionIndex, MeterValuesRequest request);

    void startTransaction(UUID sessionIndex, StartTransactionRequest request);

    void stopTransaction(UUID sessionIndex, StopTransactionRequest request);

    void activate();

    void deactivate();
}
