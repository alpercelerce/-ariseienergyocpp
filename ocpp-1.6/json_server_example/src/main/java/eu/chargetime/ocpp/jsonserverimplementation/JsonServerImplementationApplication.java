package eu.chargetime.ocpp.jsonserverimplementation;

import eu.chargetime.ocpp.jsonserverimplementation.util.DefaultProfileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;

@SpringBootApplication
public class JsonServerImplementationApplication {

	public static void main(String[] args) {
		var app = new SpringApplication(JsonServerImplementationApplication.class);
		DefaultProfileUtil.addDefaultProfile(app);
		app.run(args);
	}

}
