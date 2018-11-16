package org.yakimov.denis.currencyagregator.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.yakimov.denis.currencyagregator.dao.IHistoryActionRepository;
import org.yakimov.denis.currencyagregator.models.CurrencyValue;
import org.yakimov.denis.currencyagregator.models.HistoryAction;
import org.yakimov.denis.currencyagregator.models.HistoryActionType;
import org.yakimov.denis.currencyagregator.models.User;
import org.yakimov.denis.currencyagregator.support.StaticMessages;

import java.util.Date;

@Service
public class HistoryService {
    private static final Logger LOG = LoggerFactory.getLogger(HistoryService.class);

    @Autowired
    private IHistoryActionRepository historyActionRepository;

    @Autowired
    private UserService userService;


    public void persistHistory(CurrencyValue oldOne, CurrencyValue newOne){
        HistoryAction action = getAction(oldOne, newOne);
        historyActionRepository.save(action);
    }


    private HistoryAction getAction(CurrencyValue oldOne, CurrencyValue newOne){
        HistoryAction action = new HistoryAction();
        action.setAuthor(getCurrentUser());

        Date now = new Date();
        action.setDate(now);

        if (oldOne==null && !newOne.isDisabled()){
            action.setActionType(HistoryActionType.CREATED);
            action.setValueInstance(newOne);
            action.setNewValue(String.valueOf(now.getTime()));

        } else if (newOne.isDisabled()) {
            action.setActionType(HistoryActionType.DELETED);
            action.setValueInstance(oldOne);
            action.setPreviousValue(StaticMessages.ENABLED);
            action.setNewValue(StaticMessages.DISABLED);

        } else if (Boolean.compare(oldOne.isOperationAllowed(), newOne.isOperationAllowed())!=0){
            action.setActionType(HistoryActionType.CHANGE_ALLOWED);
            action.setValueInstance(oldOne);
            action.setPreviousValue(String.valueOf(oldOne.isOperationAllowed()));
            action.setNewValue(String.valueOf(newOne.isOperationAllowed()));

        } else if (oldOne.getBank()!=newOne.getBank()){
            action.setActionType(HistoryActionType.CHANGE_BANK);
            action.setValueInstance(oldOne);
            action.setPreviousValue(oldOne.getBank().getDisplayName());
            action.setNewValue(newOne.getBank().getDisplayName());

        } else if (oldOne.getValue().compareTo(newOne.getValue())!=0){
            action.setActionType(HistoryActionType.CHANGE_VALUE);
            action.setValueInstance(oldOne);
            action.setPreviousValue(oldOne.getValue().toString());
            action.setNewValue(newOne.getValue().toString());

        } else {
            LOG.warn(StaticMessages.HISTORY_UNKNOWN_MESSAGE, oldOne,newOne);
            action.setActionType(HistoryActionType.UNKNOWN);
            action.setValueInstance(oldOne);
            action.setNewValue(StaticMessages.HISTORY_UNKNOWN_MESSAGE);
        }

        return action;
    }


    private User getCurrentUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String login = authentication.getName();
        return userService.getUserByLogin(login);
    }
}
