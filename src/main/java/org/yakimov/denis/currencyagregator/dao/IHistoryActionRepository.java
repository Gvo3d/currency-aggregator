package org.yakimov.denis.currencyagregator.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.yakimov.denis.currencyagregator.models.HistoryAction;

public interface IHistoryActionRepository extends JpaRepository<HistoryAction, Long> {
}
