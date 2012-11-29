package com.ubs.gedit.gedex.actor.totalPortfolioValueCalculation.message;

public class StockTicker {

    final private String tickerSymbol;

    public StockTicker(String tickerSymbol) {
        this.tickerSymbol = tickerSymbol;
    }

    public String getTickerSymbol() {
        return tickerSymbol;
    }
}
