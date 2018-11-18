package org.yakimov.denis.currencyagregator.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.yakimov.denis.currencyagregator.models.Bank;

public interface IBankRepository extends JpaRepository<Bank, Integer> {
    Bank getByDisplayName(String displayName);
}
