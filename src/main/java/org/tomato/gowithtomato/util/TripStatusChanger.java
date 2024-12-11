package org.tomato.gowithtomato.util;

import lombok.extern.slf4j.Slf4j;
import org.tomato.gowithtomato.factory.DaoFactory;

import java.time.LocalDateTime;

@Slf4j
public class TripStatusChanger implements Runnable {
    @Override
    public void run() {
        log.info("start status changer : {}", LocalDateTime.now());
        DaoFactory.getTripDAO().checkTheRelevanceOfTheTripStatus();
    }
}
