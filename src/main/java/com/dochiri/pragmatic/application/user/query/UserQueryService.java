package com.dochiri.pragmatic.application.user.query;

import com.dochiri.pragmatic.application.user.dto.UserDetail;
import com.dochiri.pragmatic.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserQueryService {

    private final UserRepository userRepository;

    public UserDetail getActiveUser(Long id) {
        return UserDetail.from(userRepository.findByIdAndDeletedAtIsNull(id));
    }

}