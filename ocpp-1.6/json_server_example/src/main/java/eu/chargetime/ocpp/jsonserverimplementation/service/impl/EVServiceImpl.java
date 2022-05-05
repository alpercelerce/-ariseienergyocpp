package eu.chargetime.ocpp.jsonserverimplementation.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import eu.chargetime.ocpp.JSONServer;
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
import eu.chargetime.ocpp.model.Confirmation;
import eu.chargetime.ocpp.model.core.*;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.support.AbstractBeanFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.atomic.AtomicReference;

@Service
@Slf4j
public class EVServiceImpl implements EVService {
    private final EVConnectionRepository evConnectionRepository;
    private final EVHeartBeatRepository evHeartBeatRepository;
    private final EVChargePointConfigurationRepository evChargePointConfigurationRepository;
    private final EVChargingStatusRepository evChargingStatusRepository;
    private final EVMeterValuesRepository evMeterValuesRepository;
    private final EVTransactionRepository evTransactionRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private JSONServer jsonServer;
    private final AbstractBeanFactory abstractBeanFactory;

    public EVServiceImpl(EVConnectionRepository evConnectionRepository, EVHeartBeatRepository evHeartBeatRepository, EVChargePointConfigurationRepository evChargePointConfigurationRepository, EVChargingStatusRepository evChargingStatusRepository, EVMeterValuesRepository evMeterValuesRepository, EVTransactionRepository evTransactionRepository, AbstractBeanFactory beanFactory) {
        this.evConnectionRepository = evConnectionRepository;
        this.evHeartBeatRepository = evHeartBeatRepository;
        this.evChargePointConfigurationRepository = evChargePointConfigurationRepository;
        this.evChargingStatusRepository = evChargingStatusRepository;
        this.evMeterValuesRepository = evMeterValuesRepository;
        this.evTransactionRepository = evTransactionRepository;
        this.abstractBeanFactory = beanFactory;
    }

    @Override
    public void startConnection(String sessionId, String identifier) {
        EVConnection connection = evConnectionRepository.findByIdentifier(identifier);

        if (Objects.isNull(connection)) {
            connection = new EVConnection();
            connection.setCreateTime(LocalDateTime.now());
            connection.setIdentifier(identifier);
            connection.setCount(1);
        }

        connection.setUpdateTime(LocalDateTime.now());
        connection.setConnectionStatus(ConnectionStatus.OPEN);
        connection.setSessionId(sessionId);
        connection.setCount(connection.getCount() + 1);

        evConnectionRepository.save(connection);
    }

    @Override
    public void stopConnection(String sessionId) {
        List<EVConnection> connections = evConnectionRepository.findAllBySessionIdAndConnectionStatus(sessionId, ConnectionStatus.OPEN);

        connections.forEach(c -> {
            c.setConnectionStatus(ConnectionStatus.CLOSED);
            long activeMinute = ChronoUnit.MINUTES.between(LocalDateTime.now(), c.getUpdateTime());
            c.setActiveMinute(c.getActiveMinute() + activeMinute);
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
                if (Objects.nonNull(bySessionId.getStatus()) && bySessionId.getStatus().equals(ChargePointStatus.Charging) && !request.getStatus().equals(ChargePointStatus.Charging)) {
                    long activeMinute = ChronoUnit.MINUTES.between(LocalDateTime.now(), bySessionId.getLastUpdateTime());
                    List<EVChargePointConfiguration> chargePoints = evChargePointConfigurationRepository.findAllBySessionId(sessions.get(0).getId());
                    EVChargePointConfiguration chargePoint = chargePoints.get(0);

                    chargePoint.setChargingMinutes(chargePoint.getChargingMinutes() + activeMinute);

                    evChargePointConfigurationRepository.save(chargePoint);
                } else if (Objects.nonNull(bySessionId.getStatus()) && bySessionId.getStatus().equals(ChargePointStatus.Preparing) && !request.getStatus().equals(ChargePointStatus.Preparing)) {
                    long activeMinute = ChronoUnit.MINUTES.between(LocalDateTime.now(), bySessionId.getLastUpdateTime());
                    List<EVChargePointConfiguration> chargePoints = evChargePointConfigurationRepository.findAllBySessionId(sessions.get(0).getId());
                    EVChargePointConfiguration chargePoint = chargePoints.get(0);

                    chargePoint.setPreparingMinutes(chargePoint.getPreparingMinutes() + activeMinute);

                    evChargePointConfigurationRepository.save(chargePoint);
                }
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

    @SneakyThrows
    @Override
    public ResetConfirmation reset(String sessionID) {
        this.jsonServer = (JSONServer) abstractBeanFactory.getBean("jsonServer");
        ResetRequest resetRequest = new ResetRequest(ResetType.Hard);

        CompletionStage<Confirmation> send = jsonServer.send(UUID.fromString(sessionID), resetRequest);
        Confirmation join = send.toCompletableFuture().join();

        return (ResetConfirmation) join;
    }

    @SneakyThrows
    @Override
    public GetConfigurationConfirmation getConfiguration(String sessionID) {
        this.jsonServer = (JSONServer) abstractBeanFactory.getBean("jsonServer");
        GetConfigurationRequest getConfigurationRequest = new GetConfigurationRequest();

        CompletionStage<Confirmation> send = jsonServer.send(UUID.fromString(sessionID), getConfigurationRequest);
        Confirmation join = send.toCompletableFuture().join();

        return (GetConfigurationConfirmation) join;
    }
}
