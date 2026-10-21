package com.hotel;

import com.hotel.dao.DaoException;
import com.hotel.dao.ScenarioDao;
import com.hotel.ui.ConsoleUI;

public class Main {
    public static void main(String[] args) {
        try {
            new ConsoleUI(new ScenarioDao()).start();
        } catch (DaoException e) {
            System.out.println("FATAL: " + e.getMessage());
        }
    }
}
