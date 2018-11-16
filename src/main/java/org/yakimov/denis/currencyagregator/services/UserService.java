package org.yakimov.denis.currencyagregator.services;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yakimov.denis.currencyagregator.components.passwords.IPassowordGenerator;
import org.yakimov.denis.currencyagregator.dao.IUserRepository;
import org.yakimov.denis.currencyagregator.models.User;

@Service
public class UserService {
    private static final Logger LOG = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    IPassowordGenerator passowordGenerator;


    public User create(User user) {
        LOG.info("User creation", user);

        User newUser = new User();
        newUser.setLogin(user.getLogin());
        newUser.setName(StringUtils.isEmpty(user.getName()) ? user.getLogin() : user.getName());
        newUser.setPassword(StringUtils.isEmpty(user.getPassword()) ? passowordGenerator.generateRandomPassword() : user.getPassword());

        User newOne = userRepository.save(user);
        LOG.info("User created");
        return newOne;
    }


    public User getUserByLogin(String login){
        return userRepository.getByLogin(login);
    }


    @Transactional
    public User updateUser(String login, User updatedUser){
        LOG.info("Updating user", login);

        try {
            boolean updated = false;
            User user = userRepository.getByLogin(login);

            if (!StringUtils.isEmpty(updatedUser.getPassword())){
                user.setPassword(updatedUser.getPassword());
                updated = true;
            }

            if (!StringUtils.isEmpty(updatedUser.getName())){
                user.setPassword(updatedUser.getName());
                updated = true;
            }

            if (updated){
                LOG.info("User updated");
            } else {
                LOG.info("User not updated - nothing to update");
            }
            return user;

        } catch (RuntimeException e) {
            LOG.warn("Can't change user password", e);
            return null;
        }
    }
}
