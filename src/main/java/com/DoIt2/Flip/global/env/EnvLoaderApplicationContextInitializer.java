package com.DoIt2.Flip.global.env;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.MapPropertySource;

import java.util.HashMap;
import java.util.Map;

public class EnvLoaderApplicationContextInitializer implements ApplicationContextInitializer {

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        Dotenv dotenv = Dotenv.configure()
                .filename(".env")  // 기본 파일명
                .ignoreIfMissing() // 없을 경우 무시
                .load();

        // 1. Spring Environment에 등록
        Map<String, Object> props = new HashMap<>();
        dotenv.entries().forEach(entry -> {
            props.put(entry.getKey(), entry.getValue());

            // 2. 시스템 프로퍼티로도 설정
            System.setProperty(entry.getKey(), entry.getValue());
        });

        applicationContext.getEnvironment()
                .getPropertySources()
                .addFirst(new MapPropertySource("dotenvProperties", props));
    }
}
