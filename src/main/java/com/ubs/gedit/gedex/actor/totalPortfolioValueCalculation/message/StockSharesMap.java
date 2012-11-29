package com.ubs.gedit.gedex.actor.totalPortfolioValueCalculation.message;

import java.util.Collections;
import java.util.Map;

public class StockSharesMap {

    final private Map<String, Integer> stockSharesMap;

    public StockSharesMap(Map<String, Integer> stockSharesMap) {
        this.stockSharesMap = Collections.unmodifiableMap(stockSharesMap);
    }

    public Map<String, Integer> getStockSharesMap() {
        return stockSharesMap;
    }

    public Integer getShareBySymbol(String symbol){
        return this.stockSharesMap.get(symbol);
    }
}
