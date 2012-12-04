package com.ubs.gedit.gedex.actor.totalPortfolioValueCalculation.strategy;

import akka.actor.*;
import akka.japi.Function;
import akka.util.Duration;
import com.ubs.gedit.gedex.actor.totalPortfolioValueCalculation.actor.StockDataNotFoundException;
import com.ubs.gedit.gedex.actor.totalPortfolioValueCalculation.message.StockSharesMap;
import com.ubs.gedit.gedex.actor.totalPortfolioValueCalculation.message.StockTicker;
import com.ubs.gedit.gedex.actor.totalPortfolioValueCalculation.message.TotalValue;

import static akka.actor.SupervisorStrategy.escalate;
import static akka.actor.SupervisorStrategy.restart;

public class StrategySystemActor extends UntypedActor {
    //Not sure if the Actor itself should maintain the state of any Object
        StockSharesMap initialStockShares;
        int initialStockListSize;
        int counter;
        double total;

        final long start;
        final int NUMBER_OF_WORKER = 1;

        private ActorRef requestActor;
        ActorRef yahooActor;

        public StrategySystemActor() {
            start = System.nanoTime();
            yahooActor = getContext().actorOf(new Props(StrategyYahooActor.class));
        }

        private static SupervisorStrategy strategy = new OneForOneStrategy(
          3,
          Duration.parse("90 seconds"),
          new Function<Throwable, SupervisorStrategy.Directive>() {
              @Override
              public SupervisorStrategy.Directive apply(Throwable t) {
                if (t instanceof StockDataNotFoundException) {
                  return restart();
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
        if(message instanceof StockTicker)
            yahooActor.tell(message, getSelf());
        else if (message instanceof TotalValue){
            System.out.println("Total:" + ((TotalValue) message).getTotal());
            getContext().parent().tell(total);
        }
    }
}
