package org.yakimov.denis.currencyagregator.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.yakimov.denis.currencyagregator.models.User;

@Service
public class UserService {
    private static final Logger LOG = LoggerFactory.getLogger(UserService.class);

    public User create(User user){
        return null;
    }

    public boolean changeAccessibility(Long id, boolean disable){
        return false;
    }

    public boolean changePassword(String newPassword){
        return false;
    }
}
