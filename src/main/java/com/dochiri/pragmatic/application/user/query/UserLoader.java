package com.dochiri.pragmatic.application.user.query;

import com.dochiri.pragmatic.domain.user.User;

public interface UserLoader {

    User loadByIdAndDeletedAtIsNull(Long id);

    User loadByEmailAndDeletedAtIsNull(String email);

}