package eu.chargetime.ocpp.jsonserverimplementation.service.impl;

import eu.chargetime.ocpp.jsonserverimplementation.entity.EVChargePointConfiguration;
import eu.chargetime.ocpp.jsonserverimplementation.entity.EVConnection;
import eu.chargetime.ocpp.jsonserverimplementation.entity.EVHeartbeat;
import eu.chargetime.ocpp.jsonserverimplementation.repository.EVChargePointConfigurationRepository;
import eu.chargetime.ocpp.jsonserverimplementation.repository.EVConnectionRepository;
import eu.chargetime.ocpp.jsonserverimplementation.repository.EVHeartBeatRepository;
import eu.chargetime.ocpp.jsonserverimplementation.service.EVService;
import eu.chargetime.ocpp.jsonserverimplementation.type.ConnectionStatus;
import eu.chargetime.ocpp.model.core.BootNotificationRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EVServiceImpl implements EVService {
    private final EVConnectionRepository evConnectionRepository;
    private final EVHeartBeatRepository evHeartBeatRepository;
    private final EVChargePointConfigurationRepository evChargePointConfigurationRepository;

    @Override
    public void startConnection(String sessionId) {
        EVConnection evConnection = new EVConnection();
        evConnection.setConnectionStatus(ConnectionStatus.OPEN);
        evConnection.setSessionId(sessionId);
        evConnection.setCreateTime(LocalDateTime.now());

        evConnectionRepository.save(evConnection);
    }

    @Override
    public void stopConnection(String sessionId) {
        List<EVConnection> connections = evConnectionRepository.findAllBySessionIdAndConnectionStatus(sessionId, ConnectionStatus.OPEN);

        connections.forEach(c -> {
            c.setConnectionStatus(ConnectionStatus.CLOSED);
            c.setUpdateTime(LocalDateTime.now());
        });

        evConnectionRepository.saveAll(connections);
    }

    @Override
    public void heartBeat(String sessionId, boolean validate) {
        List<EVConnection> sessions = evConnectionRepository.findAllBySessionId(sessionId);

        if (!CollectionUtils.isEmpty(sessions)) {
            EVHeartbeat evHeartbeat = new EVHeartbeat();
            evHeartbeat.setSession(sessions.get(0));
            evHeartbeat.setCreateTime(LocalDateTime.now());
            evHeartbeat.setValidate(validate);

            evHeartBeatRepository.save(evHeartbeat);
        }
    }

    @Override
    public void bootNotification(UUID sessionIndex, BootNotificationRequest request) {
        List<EVConnection> sessions = evConnectionRepository.findAllBySessionId(sessionIndex.toString());

        if (!CollectionUtils.isEmpty(sessions)) {
            EVChargePointConfiguration chargePointConfiguration = new EVChargePointConfiguration();
            chargePointConfiguration.setSession(sessions.get(0));
            chargePointConfiguration.setCreateTime(LocalDateTime.now());
            chargePointConfiguration.setChargePointModel(request.getChargePointModel());
            chargePointConfiguration.setChargePointVendor(request.getChargePointVendor());
            chargePointConfiguration.setChargeBoxSerialNumber(request.getChargePointSerialNumber());
            chargePointConfiguration.setChargePointSerialNumber(request.getChargePointSerialNumber());
            chargePointConfiguration.setFirmwareVersion(request.getFirmwareVersion());
            chargePointConfiguration.setIccid(request.getIccid());
            chargePointConfiguration.setImsi(request.getImsi());
            chargePointConfiguration.setMeterType(request.getMeterType());
            chargePointConfiguration.setMeterSerialNumber(request.getMeterSerialNumber());

            evChargePointConfigurationRepository.save(chargePointConfiguration);
        }
    }
}
