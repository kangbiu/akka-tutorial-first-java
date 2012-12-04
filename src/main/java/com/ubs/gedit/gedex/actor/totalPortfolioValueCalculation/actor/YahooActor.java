package com.ubs.gedit.gedex.actor.totalPortfolioValueCalculation.actor;

import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.ubs.gedit.gedex.actor.totalPortfolioValueCalculation.message.StockTicker;
import com.ubs.gedit.gedex.actor.totalPortfolioValueCalculation.message.StockTickerPrice;
import com.ubs.gedit.gedex.actor.totalPortfolioValueCalculation.utilities.YahooFinancePriceFetcher;

import java.io.IOException;

public class YahooActor extends UntypedActor{

    LoggingAdapter log = Logging.getLogger(getContext().system(), this);

    private double priceState;

    @Override
    public void onReceive(Object onReceiveObject) throws Exception {

        if(onReceiveObject instanceof StockTicker){

            final String symbol = ((StockTicker) onReceiveObject).getTickerSymbol();

            try{
                final double price = getStockPriceFromYahooDotCom(symbol);

                priceState = price;

                final StockTickerPrice resultTickerPrice = new StockTickerPrice(symbol, price);
                getSender().tell(resultTickerPrice);
            }catch (Exception io){
                log.warning("FAIL TO GET THE STOCK PRICE:" +symbol + " Price NOT Price");
                throw new StockDataNotFoundException();
            }

        } else

            unhandled(onReceiveObject);

    }

    private double getStockPriceFromYahooDotCom(String symbol) throws IOException {
        return YahooFinancePriceFetcher.getPrice(symbol);
    }

    public double getPriceState() {
        return priceState;
    }
}
