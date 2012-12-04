package com.ubs.gedit.gedex.actor.totalPortfolioValueCalculation.stockPriceApp;

import com.ubs.gedit.gedex.actor.totalPortfolioValueCalculation.utilities.FileReaderUtilities;
import com.ubs.gedit.gedex.actor.totalPortfolioValueCalculation.utilities.YahooFinancePriceFetcher;

import java.io.IOException;
import java.util.Map;
import java.util.logging.Logger;

public class TotalPortfolioValueCalculatorSingleThread {
    public static void main(String[] args ) throws Exception {
        Logger log = Logger.getAnonymousLogger();

        final long start = System.nanoTime();

        Map<String, Integer> map = FileReaderUtilities.readTickers();

        double totalValue =0;
        for(Map.Entry<String, Integer> entry:map.entrySet()){
            try{
                final double price = YahooFinancePriceFetcher.getPrice(entry.getKey());
                totalValue += price*entry.getValue();
            }catch (IOException io){
                log.warning("FAIL TO GET THE STOCK PRICE:" +entry.getKey() + "Cause: "+ io.getCause());
            }
        }
        final long end = System.nanoTime();

        System.out.println("Total Value: "+ totalValue);
        System.out.println("Total Number of Stocks: "+ map.size());
        System.out.println("Total Time:" + (end - start) / 1.0e9);

    }
}
