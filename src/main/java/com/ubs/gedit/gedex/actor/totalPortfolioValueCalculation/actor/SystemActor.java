package com.ubs.gedit.gedex.actor.totalPortfolioValueCalculation.actor;

import akka.actor.*;
import akka.japi.Function;
import akka.util.Duration;
import com.ubs.gedit.gedex.actor.totalPortfolioValueCalculation.message.StockSharesMap;
import com.ubs.gedit.gedex.actor.totalPortfolioValueCalculation.message.StockTicker;
import com.ubs.gedit.gedex.actor.totalPortfolioValueCalculation.message.StockTickerPrice;

import java.util.Map;

import static akka.actor.SupervisorStrategy.escalate;
import static akka.actor.SupervisorStrategy.stop;

public class SystemActor extends UntypedActor{

    //Not sure if the Actor itself should maintain the state of any Object
    StockSharesMap initialStockShares;
    int initialStockListSize;
    int counter;
    double total;
    private ActorSystem actorSystem;

    final long start;

    public SystemActor() {
        start = System.nanoTime();
    }

    private static SupervisorStrategy strategy = new OneForOneStrategy(3,
      Duration.parse("10 seconds"), new Function<Throwable, SupervisorStrategy.Directive>() {
      @Override
      public SupervisorStrategy.Directive apply(Throwable t) {
        if (t instanceof StockDataNotFoundException) {
          return stop();
        } else {
          return escalate();
        }
      }
    });


    @Override
    public SupervisorStrategy supervisorStrategy() {
      return strategy;
    }


    @Override
    public void onReceive(Object message) throws Exception {
        if(message instanceof StockSharesMap){
            initialStockShares = ((StockSharesMap) message);
            initialStockListSize = initialStockShares.getStockSharesMap().size();
            Map<String, Integer> map = initialStockShares.getStockSharesMap();
            for(Map.Entry<String, Integer> entry:map.entrySet()){
                final ActorRef yahooActor = getContext().actorOf(new Props(YahooActor.class));
                yahooActor.tell(new StockTicker(entry.getKey()), getSelf());
            }
        }else if (message instanceof StockTickerPrice){
            counter++;
            StockTickerPrice stockTickerPrice = (StockTickerPrice) message;
            int share = Integer.valueOf(initialStockShares.getShareBySymbol(stockTickerPrice.getStockTickerSymbol()));
            total += stockTickerPrice.getPrice() * share;

        }else {
            unhandled(message);
        }

        if(counter==initialStockListSize){
            long end = System.nanoTime();
            System.out.println("Total Value: "+ total);
            System.out.println("Total Number of Stocks: "+ counter);
            System.out.println("Total Time:" + (end - start) / 1.0e9);
            getContext().system().shutdown();
        }
    }
}
