package com.ubs.gedit.gedex.actor.totalPortfolioValueCalculation.strategy;

import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.ubs.gedit.gedex.actor.totalPortfolioValueCalculation.actor.StockDataNotFoundException;
import com.ubs.gedit.gedex.actor.totalPortfolioValueCalculation.message.StockTicker;
import com.ubs.gedit.gedex.actor.totalPortfolioValueCalculation.message.TotalValue;
import com.ubs.gedit.gedex.actor.totalPortfolioValueCalculation.utilities.YahooFinancePriceFetcher;

public class StrategyYahooActor extends UntypedActor {

    LoggingAdapter log = Logging.getLogger(getContext().system(), this);

    double total;
    @Override
    public void onReceive(Object message) throws Exception {
        double price = 0;
        if(message instanceof StockTicker){
            try{
            price = YahooFinancePriceFetcher.getPrice(((StockTicker) message).getTickerSymbol());
            total = total + price;
            getSender().tell(new TotalValue(total));
            }catch (Exception e)  {
                throw new StockDataNotFoundException();
            }
        }
    }


    @Override
    public void preStart(){
    }

    @Override
    public void postRestart(Throwable reason) {
          log.info("Yahoo!Actor : supervisor restarted me.");
    }

    @Override
   	public void postStop() {
   		log.info("Stopping WorkerActor instance hashcode #" + this.hashCode());
   	}


}
