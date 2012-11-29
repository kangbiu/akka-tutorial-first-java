package com.ubs.gedit.gedex.actor.totalPortfolioValueCalculation.actor;

import akka.actor.UntypedActor;
import com.ubs.gedit.gedex.actor.totalPortfolioValueCalculation.message.StockTicker;
import com.ubs.gedit.gedex.actor.totalPortfolioValueCalculation.message.StockTickerPrice;
import com.ubs.gedit.gedex.actor.totalPortfolioValueCalculation.utilities.YahooFinancePriceFetcher;

import java.io.IOException;
import java.util.logging.Logger;

public class YahooActor extends UntypedActor{
    Logger log = Logger.getAnonymousLogger();

    @Override
    public void onReceive(Object onReceiveObject) throws Exception {
        if(onReceiveObject instanceof StockTicker){
            final String symbol = ((StockTicker) onReceiveObject).getTickerSymbol();
            try{
                final double price = getStockPriceFromYahooDotCom(symbol);
                final StockTickerPrice resultTickerPrice = new StockTickerPrice(symbol, price);
                getSender().tell(resultTickerPrice);
            }catch (IOException io){
                log.warning("FAIL TO GET THE STOCK PRICE:" +symbol + "Cause: "+ io.getCause());
                throw new StockDataNotFoundException();
            }
        } else
            unhandled(onReceiveObject);
    }

    private double getStockPriceFromYahooDotCom(String symbol) throws IOException {
        return YahooFinancePriceFetcher.getPrice(symbol);
    }


}
