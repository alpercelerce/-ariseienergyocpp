package eu.chargetime.ocpp.jsonserverimplementation.config;

import eu.chargetime.ocpp.ServerEvents;
import eu.chargetime.ocpp.jsonserverimplementation.service.EVService;
import eu.chargetime.ocpp.model.SessionInformation;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.UUID;

@Configuration
@Getter
@Slf4j
@RequiredArgsConstructor
public class ServerEventConfig {
    private final EVService evService;

    @Bean
    public ServerEvents createServerCoreImpl() {
        return getNewServerEventsImpl();
    }

    private ServerEvents getNewServerEventsImpl() {
        return new ServerEvents() {

            @Override
            public void newSession(UUID sessionIndex, SessionInformation information) {
                System.out.println("New session " + sessionIndex + ": " + information.getIdentifier());
                evService.startConnection(sessionIndex.toString());
            }

            @Override
            public void lostSession(UUID sessionIndex) {
                System.out.println("Session " + sessionIndex + " lost connection");
                evService.stopConnection(sessionIndex.toString());
            }
        };
    }
}
