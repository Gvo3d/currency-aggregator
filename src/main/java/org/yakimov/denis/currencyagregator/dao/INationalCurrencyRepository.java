package org.yakimov.denis.currencyagregator.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.yakimov.denis.currencyagregator.models.NationalCurrency;

public interface INationalCurrencyRepository extends JpaRepository<NationalCurrency, Integer> {
    NationalCurrency getByShortName(String shortName);

//    @Query(value = "SELECT NationalCurrency.")
//    Integer getLastOrder();
}
