package eu.chargetime.ocpp.jsonclientimplementation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class JsonClientImplementationApplication {


	public static void main(String[] args) {

		SpringApplication.run(JsonClientImplementationApplication.class, args);

		JSONClientSample jsonClientSample = new JSONClientSample();

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
			jsonClientSample.sendHeartbeat();
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			jsonClientSample.sendChargingStationStatus();
		} catch (Exception e) {
			e.printStackTrace();
		}


	}


}
