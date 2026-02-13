package com.example.pragmatic.application.auth.query;

import com.example.pragmatic.domain.auth.RefreshToken;
import java.time.LocalDateTime;

public interface RefreshTokenLoader {

    RefreshToken loadValidToken(String token, LocalDateTime now);

}