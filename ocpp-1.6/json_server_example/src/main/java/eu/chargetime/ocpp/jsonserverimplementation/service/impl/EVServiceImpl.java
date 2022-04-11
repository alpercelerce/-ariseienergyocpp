package eu.chargetime.ocpp.jsonserverimplementation.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import eu.chargetime.ocpp.jsonserverimplementation.entity.EVChargePointConfiguration;
import eu.chargetime.ocpp.jsonserverimplementation.entity.EVChargingStatus;
import eu.chargetime.ocpp.jsonserverimplementation.entity.EVConnection;
import eu.chargetime.ocpp.jsonserverimplementation.entity.EVHeartbeat;
import eu.chargetime.ocpp.jsonserverimplementation.entity.EVMeterValues;
import eu.chargetime.ocpp.jsonserverimplementation.entity.EVTransaction;
import eu.chargetime.ocpp.jsonserverimplementation.repository.EVChargePointConfigurationRepository;
import eu.chargetime.ocpp.jsonserverimplementation.repository.EVChargingStatusRepository;
import eu.chargetime.ocpp.jsonserverimplementation.repository.EVConnectionRepository;
import eu.chargetime.ocpp.jsonserverimplementation.repository.EVHeartBeatRepository;
import eu.chargetime.ocpp.jsonserverimplementation.repository.EVMeterValuesRepository;
import eu.chargetime.ocpp.jsonserverimplementation.repository.EVTransactionRepository;
import eu.chargetime.ocpp.jsonserverimplementation.service.EVService;
import eu.chargetime.ocpp.jsonserverimplementation.type.ConnectionStatus;
import eu.chargetime.ocpp.jsonserverimplementation.type.TransactionType;
import eu.chargetime.ocpp.model.core.*;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class EVServiceImpl implements EVService {
    private final EVConnectionRepository evConnectionRepository;
    private final EVHeartBeatRepository evHeartBeatRepository;
    private final EVChargePointConfigurationRepository evChargePointConfigurationRepository;
    private final EVChargingStatusRepository evChargingStatusRepository;
    private final EVMeterValuesRepository evMeterValuesRepository;
    private final EVTransactionRepository evTransactionRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void startConnection(String sessionId, String identifier) {
        EVConnection connection = evConnectionRepository.findByIdentifier(identifier);

        if (Objects.isNull(connection)) {
            connection = new EVConnection();
            connection.setConnectionStatus(ConnectionStatus.OPEN);
            connection.setSessionId(sessionId);
            connection.setCreateTime(LocalDateTime.now());
            connection.setIdentifier(identifier);
        } else {
            connection.setConnectionStatus(ConnectionStatus.OPEN);
        }

        evConnectionRepository.save(connection);
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
            EVChargePointConfiguration evChargePoint = evChargePointConfigurationRepository.findByChargeBoxSerialNumber(request.getChargePointSerialNumber());

            if (Objects.isNull(evChargePoint)) {
                evChargePoint = new EVChargePointConfiguration();
                evChargePoint.setSession(sessions.get(0));
                evChargePoint.setCreateTime(LocalDateTime.now());
                evChargePoint.setChargePointModel(request.getChargePointModel());
                evChargePoint.setChargePointVendor(request.getChargePointVendor());
                evChargePoint.setChargeBoxSerialNumber(request.getChargePointSerialNumber());
                evChargePoint.setChargePointSerialNumber(request.getChargePointSerialNumber());
                evChargePoint.setFirmwareVersion(request.getFirmwareVersion());
                evChargePoint.setIccid(request.getIccid());
                evChargePoint.setImsi(request.getImsi());
                evChargePoint.setMeterType(request.getMeterType());
                evChargePoint.setMeterSerialNumber(request.getMeterSerialNumber());
            } else  {
                evChargePoint.setFirmwareVersion(request.getFirmwareVersion());
                evChargePoint.setIccid(request.getIccid());
                evChargePoint.setImsi(request.getImsi());
                evChargePoint.setMeterType(request.getMeterType());
            }

            evChargePointConfigurationRepository.save(evChargePoint);
        }
    }

    @Override
    public void chargingStatus(UUID sessionIndex, StatusNotificationRequest request) {
        log.warn("CONNECTOR ID IGNORED FOR NOW (REQUEST CONNECTOR ID is {})", request.getConnectorId());
        List<EVConnection> sessions = evConnectionRepository.findAllBySessionId(sessionIndex.toString());

        if (!CollectionUtils.isEmpty(sessions)) {
            EVChargingStatus bySessionId = evChargingStatusRepository.findBySessionId(sessions.get(0).getId());

            if (Objects.isNull(bySessionId)) {
                bySessionId = new EVChargingStatus();
                bySessionId.setCreateTime(LocalDateTime.now());
                bySessionId.setLastUpdateTime(LocalDateTime.now());
                bySessionId.setSession(sessions.get(0));
            } else {
                bySessionId.setLastUpdateTime(LocalDateTime.now());
            }

            bySessionId.setStatus(request.getStatus());
            bySessionId.setErrorCode(request.getErrorCode());

            evChargingStatusRepository.save(bySessionId);
        }
    }

    @SneakyThrows
    @Override
    public void meterValues(UUID sessionIndex, MeterValuesRequest request) {
        log.warn("CONNECTOR ID IGNORED FOR NOW (REQUEST CONNECTOR ID is {})", request.getConnectorId());
        List<EVConnection> sessions = evConnectionRepository.findAllBySessionId(sessionIndex.toString());

        if (!CollectionUtils.isEmpty(sessions)) {
            EVMeterValues bySessionId = evMeterValuesRepository.findBySessionId(sessions.get(0).getId());

            if (Objects.isNull(bySessionId)) {
                bySessionId = new EVMeterValues();
                bySessionId.setSession(sessions.get(0));
                bySessionId.setCreateTime(LocalDateTime.now());
                bySessionId.setLastUpdateTime(LocalDateTime.now());
            } else {
                bySessionId.setLastUpdateTime(LocalDateTime.now());
            }

            String jsonString = objectMapper.writeValueAsString(request.getMeterValue());
            bySessionId.setMeterValuesJSON(jsonString);

            evMeterValuesRepository.save(bySessionId);
        }
    }

    @Override
    public void startTransaction(UUID sessionIndex, StartTransactionRequest request) {
        List<EVConnection> sessions = evConnectionRepository.findAllBySessionId(sessionIndex.toString());

        if (!CollectionUtils.isEmpty(sessions)) {
            EVTransaction evTransaction = new EVTransaction();
            evTransaction.setSession(sessions.get(0));
            evTransaction.setCreateTime(LocalDateTime.now());
            evTransaction.setConnectorId(request.getConnectorId());
            evTransaction.setIdTag(request.getIdTag());
            evTransaction.setMeterStart(request.getMeterStart());
            evTransaction.setReservationId(request.getReservationId());
            evTransaction.setType(TransactionType.START);

            evTransactionRepository.save(evTransaction);
        }
    }

    @SneakyThrows
    @Override
    public void stopTransaction(UUID sessionIndex, StopTransactionRequest request) {
        List<EVConnection> sessions = evConnectionRepository.findAllBySessionId(sessionIndex.toString());

        if (!CollectionUtils.isEmpty(sessions)) {
            EVTransaction evTransaction = new EVTransaction();
            evTransaction.setSession(sessions.get(0));
            evTransaction.setCreateTime(LocalDateTime.now());
            evTransaction.setIdTag(request.getIdTag());
            evTransaction.setMeterStop(request.getMeterStop());
            evTransaction.setTransactionId(request.getTransactionId());
            evTransaction.setReason(request.getReason());
            evTransaction.setType(TransactionType.STOP);

            String transactionDataJsonStr = objectMapper.writeValueAsString(request.getTransactionData());
            evTransaction.setData(transactionDataJsonStr);

            evTransactionRepository.save(evTransaction);
        }
    }

    @Override
    public void activate() {

        // TODO: jsonServer
    }

    @Override
    public void deactivate() {
        // TODO: jsonServer
    }

    @Override
    public ResetConfirmation reset() {
        return null;
    }

    @Override
    public GetConfigurationConfirmation getConfiguration() {
        return null;
    }
}
