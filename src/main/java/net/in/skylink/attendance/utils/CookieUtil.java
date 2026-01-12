package net.in.skylink.attendance.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.in.skylink.attendance.jwt.JwtTokenProvider;

@Component
public class CookieUtil {
    private final JwtTokenProvider jwtTokenProvider;
    private final boolean secureCookies;

    public CookieUtil(JwtTokenProvider jwtTokenProvider,
                    @Value("${app.cookie.secure:false}") boolean secureCookies) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.secureCookies = secureCookies;
    }

    public Cookie addRefreshTokenCookie(HttpServletResponse response, String refreshToken) {
        Cookie cookie = new Cookie(jwtTokenProvider.getJwtRefreshTokenCookieName(), refreshToken);
        cookie.setHttpOnly(true);
        cookie.setSecure(secureCookies);
        cookie.setPath("/");
        cookie.setMaxAge(32 * 24 * 60 * 60); // 32 days
        cookie.setAttribute("SameSite", "Lax");
        response.addCookie(cookie);
        return cookie;
    }

    public void deleteRefreshTokenCookie(HttpServletResponse response) {
        Cookie cookie = new Cookie(jwtTokenProvider.getJwtRefreshTokenCookieName(), null);
        cookie.setHttpOnly(true);
        cookie.setSecure(secureCookies);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }

    public String getRefreshTokenFromCookies(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(jwtTokenProvider.getJwtRefreshTokenCookieName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}