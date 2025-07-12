package com.DoIt2.Flip;

import com.DoIt2.Flip.global.env.EnvLoaderApplicationContextInitializer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FlipApplication {

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(FlipApplication.class);

		// .env 값 등록을 위한 커스텀 Initializer 등록
		app.addInitializers(new EnvLoaderApplicationContextInitializer());
		app.run(args);

		System.out.println("Sever Start");
	}
}
