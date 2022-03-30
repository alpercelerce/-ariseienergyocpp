package eu.chargetime.ocpp.jsonclientimplementation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class SchedulerService {

    private static final Logger logger = LoggerFactory.getLogger(SchedulerService.class);

    private JSONClientSample jsonClientSample = new JSONClientSample();

    public SchedulerService () {
        try {
            jsonClientSample.connect();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            jsonClientSample.sendBootNotification();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            jsonClientSample.sendChargingStationStatus();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Scheduled(fixedRateString ="${console.fetchMetrics}", initialDelay=1000)
    public void sendHeartbeatMessage() {

        logger.info("I'm still alive");

        try {
            jsonClientSample.sendHeartbeat();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
