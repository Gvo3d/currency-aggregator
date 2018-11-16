package org.yakimov.denis.currencyagregator.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.yakimov.denis.currencyagregator.models.CurrencyValue;

public interface ICurrencyValueRepository extends JpaRepository<CurrencyValue, Long> {
}
