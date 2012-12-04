package com.ubs.gedit.gedex.actor.totalPortfolioValueCalculation.actor;

import akka.actor.*;
import akka.japi.Function;
import akka.routing.RoundRobinRouter;
import akka.util.Duration;
import com.ubs.gedit.gedex.actor.totalPortfolioValueCalculation.message.StockSharesMap;
import com.ubs.gedit.gedex.actor.totalPortfolioValueCalculation.message.StockTicker;
import com.ubs.gedit.gedex.actor.totalPortfolioValueCalculation.message.StockTickerPrice;
import com.ubs.gedit.gedex.actor.totalPortfolioValueCalculation.message.TotalValue;

import java.util.Map;

import static akka.actor.SupervisorStrategy.escalate;
import static akka.actor.SupervisorStrategy.resume;

public class SystemActor extends UntypedActor{

    //Not sure if the Actor itself should maintain the state of any Object
    StockSharesMap initialStockShares;
    int initialStockListSize;
    int counter;
    double total;

    final long start;
    final int NUMBER_OF_WORKER;

    private ActorRef requestActor;
    ActorRef yahooActor;

    public SystemActor(int NUMBER_OF_WORKER) {
        this.NUMBER_OF_WORKER = NUMBER_OF_WORKER;
        start = System.nanoTime();
        yahooActor = getContext().actorOf(new Props(YahooActor.class).withRouter(new RoundRobinRouter(NUMBER_OF_WORKER).withSupervisorStrategy(strategy)));
    }

    private static SupervisorStrategy strategy = new OneForOneStrategy(
      3,
      Duration.parse("90 seconds"),
      new Function<Throwable, SupervisorStrategy.Directive>() {
          @Override
          public SupervisorStrategy.Directive apply(Throwable t) {
            if (t instanceof StockDataNotFoundException) {
              return resume();
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
            requestActor = getSender();

            initialStockShares = ((StockSharesMap) message);
            initialStockListSize = initialStockShares.getStockSharesMap().size();

            Map<String, Integer> map = initialStockShares.getStockSharesMap();

            for(Map.Entry<String, Integer> entry:map.entrySet()){
                yahooActor.tell(new StockTicker(entry.getKey()), getSelf());
            }

        }else if (message instanceof StockTickerPrice){

            counter++;
            StockTickerPrice stockTickerPrice = (StockTickerPrice) message;
            int share = Integer.valueOf(initialStockShares.getShareBySymbol(stockTickerPrice.getStockTickerSymbol()));
            total += stockTickerPrice.getPrice() * share;

        } else {
            unhandled(message);
        }

        if(counter==initialStockListSize){
            requestActor.tell(new TotalValue(total));
        }
    }

}
