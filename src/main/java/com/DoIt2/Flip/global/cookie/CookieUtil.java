package com.DoIt2.Flip.global.cookie;

import jakarta.servlet.http.Cookie;
import org.springframework.stereotype.Component;

@Component
public class CookieUtil {

    public Cookie createCookie(String key, String value) {

        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(24*60*60); // 쿠키 생명주기
        //cookie.setSecure(true); // https 라면 설정 필요
        //cookie.setPath("/"); // 쿠기 적용 범위
        cookie.setHttpOnly(true); // 클라이언트에서 해당 쿠키 접근 불가

        return cookie;
    }
}
