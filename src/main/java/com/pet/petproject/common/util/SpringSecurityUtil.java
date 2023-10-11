package com.pet.petproject.common.util;

import com.pet.petproject.common.exception.AppException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SpringSecurityUtil {
    public static String getLoginId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication.getName() == null) {
            throw new AppException(HttpStatus.INTERNAL_SERVER_ERROR, "get authentication 오류");
        }
        return authentication.getName();

    }
}
