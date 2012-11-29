package com.ubs.gedit.gedex.actor.totalPortfolioValueCalculation.message;

public class StockTickerPrice {

    final private String stockTickerSymbol;
    final private double price;

    public StockTickerPrice(String stockTickerSymbol, double price) {
        this.stockTickerSymbol = stockTickerSymbol;
        this.price = price;
    }

    public double getPrice() {
        return price;
    }

    public String getStockTickerSymbol() {
        return stockTickerSymbol;
    }
}

