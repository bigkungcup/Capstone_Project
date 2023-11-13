package sit.cp23ej2;

import java.util.TimeZone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Cp23ej2Application {

	public static void main(String[] args) {
		TimeZone.setDefault(TimeZone.getTimeZone("GMT+7:00"));
		SpringApplication.run(Cp23ej2Application.class, args);
	}

}
