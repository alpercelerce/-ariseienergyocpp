package eu.chargetime.ocpp.jsonserverimplementation.config;

import eu.chargetime.ocpp.feature.profile.ServerCoreEventHandler;
import eu.chargetime.ocpp.feature.profile.ServerCoreProfile;
import eu.chargetime.ocpp.jsonserverimplementation.service.EVService;
import eu.chargetime.ocpp.model.core.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.ZonedDateTime;
import java.util.Random;
import java.util.UUID;

@Configuration
@Getter
@Slf4j
@RequiredArgsConstructor
public class ServerCoreProfileConfig {

    private static final Logger logger = LoggerFactory.getLogger(ServerCoreProfileConfig.class);

    private final EVService evService;
    private final Random rnd = new Random();

    @Bean
    public ServerCoreEventHandler getCoreEventHandler() {

        logger.info("getCoreEventHandler is called");

        return new ServerCoreEventHandler() {
            @Override
            public AuthorizeConfirmation handleAuthorizeRequest(UUID sessionIndex, AuthorizeRequest request) {

                System.out.println(request);
                // ... handle event
                IdTagInfo idTagInfo = new IdTagInfo();
                idTagInfo.setExpiryDate(ZonedDateTime.now());
                idTagInfo.setParentIdTag("test");
                idTagInfo.setStatus(AuthorizationStatus.Accepted);

                return new AuthorizeConfirmation(idTagInfo);
            }

            @Override
            public BootNotificationConfirmation handleBootNotificationRequest(UUID sessionIndex, BootNotificationRequest request) {

                System.out.println(request);
                // ... handle event
                evService.bootNotification(sessionIndex, request);

                return new BootNotificationConfirmation(ZonedDateTime.now(), 3000, RegistrationStatus.Accepted); // returning null means unsupported feature
            }

            @Override
            public DataTransferConfirmation handleDataTransferRequest(UUID sessionIndex, DataTransferRequest request) {

                System.out.println(request);
                // ... handle event

                return null; // returning null means unsupported feature
            }

            @Override
            public HeartbeatConfirmation handleHeartbeatRequest(UUID sessionIndex, HeartbeatRequest request) {

                logger.info("handleHeartbeatRequest has came - sessionIndex:" + sessionIndex + " and request:" + request);

                System.out.println(request);
                // ... handle event
                evService.heartBeat(sessionIndex.toString(), request.validate());

                return  new HeartbeatConfirmation(ZonedDateTime.now()); // returning null means unsupported feature
            }

            @Override
            public MeterValuesConfirmation handleMeterValuesRequest(UUID sessionIndex, MeterValuesRequest request) {

                System.out.println(request);
                // ... handle event
                evService.meterValues(sessionIndex, request);

                return new MeterValuesConfirmation(); // returning null means unsupported feature
            }

            @Override
            public StartTransactionConfirmation handleStartTransactionRequest(UUID sessionIndex, StartTransactionRequest request) {

                System.out.println("Handle start transaction !");
                System.out.println(request);
                // ... handle event

                IdTagInfo idTagInfo = new IdTagInfo(AuthorizationStatus.Accepted); // TODO: for now
                idTagInfo.setParentIdTag("TEST");
                idTagInfo.setExpiryDate(ZonedDateTime.now());
                int generatedTransactionId = rnd.nextInt();

                logger.info("GENERATED TRANSACTION ID is {} FOR START TRANSACTION REQUEST", generatedTransactionId);
                StartTransactionConfirmation startTransactionConfirmation = new StartTransactionConfirmation(idTagInfo, generatedTransactionId);
                logger.info("START TRANSACTION CONFIRMATION = {}", startTransactionConfirmation);
                evService.startTransaction(sessionIndex, request);

                return startTransactionConfirmation; // returning null means unsupported feature
            }

            @Override
            public StatusNotificationConfirmation handleStatusNotificationRequest(UUID sessionIndex, StatusNotificationRequest request) {

                logger.info("handleStatusNotificationRequest has came - sessionIndex:" + sessionIndex + " and request:" + request);
                logger.info("Connector ID is " + request.getConnectorId());
                logger.info("Charging Status is " + request.getStatus());
                logger.info("Error Code is " + request.getErrorCode());

                System.out.println(request);
                // ... handle event
                evService.chargingStatus(sessionIndex, request);

                return new StatusNotificationConfirmation(); // returning null means unsupported feature
            }

            @Override
            public StopTransactionConfirmation handleStopTransactionRequest(UUID sessionIndex, StopTransactionRequest request) {

                System.out.println("Handle stop transaction !");
                System.out.println(request);
                // ... handle event

                IdTagInfo idTagInfo = new IdTagInfo(AuthorizationStatus.Accepted); // TODO: for now
                idTagInfo.setParentIdTag("TEST");
                idTagInfo.setExpiryDate(ZonedDateTime.now());

                StopTransactionConfirmation stopTransactionConfirmation = new StopTransactionConfirmation();
                stopTransactionConfirmation.setIdTagInfo(idTagInfo);

                evService.stopTransaction(sessionIndex, request);
                logger.info("STOP TRANSACTION CONFIRMATION = {}", stopTransactionConfirmation);
                return stopTransactionConfirmation; // returning null means unsupported feature
            }
        };
    }

    @Bean
    public ServerCoreProfile createCore(ServerCoreEventHandler serverCoreEventHandler) {
        return new ServerCoreProfile(serverCoreEventHandler);
    }
}
