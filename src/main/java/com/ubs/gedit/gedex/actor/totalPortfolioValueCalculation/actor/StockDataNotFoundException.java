package com.ubs.gedit.gedex.actor.totalPortfolioValueCalculation.actor;

public class StockDataNotFoundException extends Exception {

    String customizedMessage;
    public StockDataNotFoundException() {
        super();
        this.customizedMessage = "Well.... Can't get this symbol from Yahoo! You might try next door - Google?? ";
    }

    public String getMessage(){
        return customizedMessage;
    }
}
