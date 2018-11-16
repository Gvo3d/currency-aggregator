package org.yakimov.denis.currencyagregator.services;

import org.yakimov.denis.currencyagregator.models.User;

public interface SecurityService {
    boolean cryptUserPass(User user);
    boolean isPasswordMatchEncrypted(String raw, String encoded);
    Long getAuthenticatedUserId(String s);
    Boolean autologin(String username, String password);
}
