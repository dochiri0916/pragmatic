package com.example.pragmatic.application.user.query;

import com.example.pragmatic.domain.user.User;

public interface UserLoader {

    User loadActiveUserById(Long id);

    User loadActiveUserByEmail(String email);

    User loadByEmail(String email);

}