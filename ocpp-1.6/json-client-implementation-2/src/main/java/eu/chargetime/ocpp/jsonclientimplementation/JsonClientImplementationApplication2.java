package eu.chargetime.ocpp.jsonclientimplementation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class JsonClientImplementationApplication2 {

	private static final Logger logger = LoggerFactory.getLogger(JsonClientImplementationApplication2.class);

	public static void main(String[] args) {
		SpringApplication.run(JsonClientImplementationApplication2.class, args);
	}
}
