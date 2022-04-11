package eu.chargetime.ocpp.jsonserverimplementation.config;

import eu.chargetime.ocpp.feature.profile.ClientCoreEventHandler;
import eu.chargetime.ocpp.feature.profile.ClientCoreProfile;
import eu.chargetime.ocpp.model.core.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
@Slf4j
@RequiredArgsConstructor
public class ClientCoreProfileConfig {

    private static final Logger logger = LoggerFactory.getLogger(ClientCoreProfileConfig.class);

    //private final EVService evService;
    //private final Random rnd = new Random();

    @Bean
    public ClientCoreEventHandler getClientCoreEventHandler() {

        logger.info("getClientCoreEventHandler is called");

        return new ClientCoreEventHandler() {

            @Override
            public ChangeAvailabilityConfirmation handleChangeAvailabilityRequest(ChangeAvailabilityRequest changeAvailabilityRequest) {
                return null;
            }

            @Override
            public GetConfigurationConfirmation handleGetConfigurationRequest(GetConfigurationRequest getConfigurationRequest) {
                System.out.println("handleGetConfigurationRequest !");
                System.out.println(getConfigurationRequest);
                // ... handle event

                GetConfigurationConfirmation getConfigurationConfirmation = new GetConfigurationConfirmation();

                logger.info("GET CONFIGURATION CONFIRMATION = {}", getConfigurationConfirmation);
                return getConfigurationConfirmation; // returning null means unsupported feature
            }

            @Override
            public ChangeConfigurationConfirmation handleChangeConfigurationRequest(ChangeConfigurationRequest changeConfigurationRequest) {
                return null;
            }

            @Override
            public ClearCacheConfirmation handleClearCacheRequest(ClearCacheRequest clearCacheRequest) {
                return null;
            }

            @Override
            public DataTransferConfirmation handleDataTransferRequest(DataTransferRequest dataTransferRequest) {
                return null;
            }

            @Override
            public RemoteStartTransactionConfirmation handleRemoteStartTransactionRequest(RemoteStartTransactionRequest remoteStartTransactionRequest) {
                return null;
            }

            @Override
            public RemoteStopTransactionConfirmation handleRemoteStopTransactionRequest(RemoteStopTransactionRequest remoteStopTransactionRequest) {
                return null;
            }

            @Override
            public ResetConfirmation handleResetRequest(ResetRequest resetRequest) {
                System.out.println("handleResetRequest !");
                System.out.println(resetRequest);
                // ... handle event

                ResetConfirmation resetConfirmation = new ResetConfirmation(ResetStatus.Accepted);

                logger.info("RESET CONFIRMATION = {}", resetConfirmation);
                return resetConfirmation; // returning null means unsupported feature
            }

            @Override
            public UnlockConnectorConfirmation handleUnlockConnectorRequest(UnlockConnectorRequest unlockConnectorRequest) {
                return null;
            }

        };
    }

    @Bean
    public ClientCoreProfile createClientCore(ClientCoreEventHandler clientCoreEventHandler) {
        return new ClientCoreProfile(clientCoreEventHandler);
    }
}
