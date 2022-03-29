package eu.chargetime.ocpp.jsonserverimplementation.service;

import eu.chargetime.ocpp.model.core.BootNotificationRequest;

import java.util.UUID;

public interface EVService {
    void startConnection(String sessionId, String identifier);

    void stopConnection(String sessionId);

    void heartBeat(String sessionId, boolean validate);

    void bootNotification(UUID sessionIndex, BootNotificationRequest request);
}
