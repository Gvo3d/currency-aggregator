package org.yakimov.denis.currencyagregator.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.yakimov.denis.currencyagregator.models.User;

public interface IUserRepository extends JpaRepository<Long, User> {
}
