package eu.chargetime.ocpp.jsonserverimplementation.server;

import eu.chargetime.ocpp.JSONServer;
import eu.chargetime.ocpp.ServerEvents;
import eu.chargetime.ocpp.jsonserverimplementation.config.ApplicationConfiguration;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Slf4j
@Component
@AllArgsConstructor
public class JsonServerImpl {

    private static final Logger logger = LoggerFactory.getLogger(JsonServerImpl.class);

    private final ServerEvents serverEvents;
    private final JSONServer server;
    private final ApplicationConfiguration applicationConfiguration;

    @PostConstruct
    public void startServer() throws Exception {

        logger.info(applicationConfiguration.getServerAddress() + ":" + applicationConfiguration.getServerPort() + " is going to be running...");

        server.open(applicationConfiguration.getServerAddress(), applicationConfiguration.getServerPort(), serverEvents);

        logger.info("Server is up now");

    }
}
