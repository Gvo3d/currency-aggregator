package org.yakimov.denis.currencyagregator.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.yakimov.denis.currencyagregator.models.NationalCurrency;

public interface INationalCurrencyRepository extends JpaRepository<NationalCurrency, Integer> {
    NationalCurrency getByShortName(String shortName);

    @Query(value = "SELECT MAX(order_sequence) FROM nationalcurrency", nativeQuery = true)
    Integer getLastOrder();
}
