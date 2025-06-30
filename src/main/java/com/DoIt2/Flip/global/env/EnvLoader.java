package com.DoIt2.Flip.global.env;

import io.github.cdimascio.dotenv.Dotenv;

public class EnvLoader {

    private static final Dotenv dotenv = Dotenv.configure()
                .filename(".env")  // 기본 파일명
                .ignoreIfMissing() // 없을 경우 무시
                .load();

    public static String get(String key){
        return dotenv.get(key);
    }

    public static int getInt(String key, int defaultValue){
        try{
            return Integer.parseInt(dotenv.get(key));
        } catch (Exception e){
            return defaultValue;
        }
    }
}