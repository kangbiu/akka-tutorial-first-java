package com.ubs.gedit.gedex.actor.totalPortfolioValueCalculation.actor;

import akka.actor.*;
import akka.japi.Function;
import com.ubs.gedit.gedex.actor.totalPortfolioValueCalculation.message.StockSharesMap;
import com.ubs.gedit.gedex.actor.totalPortfolioValueCalculation.message.StockTicker;
import com.ubs.gedit.gedex.actor.totalPortfolioValueCalculation.message.StockTickerPrice;
import com.ubs.gedit.gedex.actor.totalPortfolioValueCalculation.message.TotalValue;
import akka.util.Duration;

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

//    public SystemActor(ActorSystem actorSystem) {
//        this.actorSystem = actorSystem;
//    }

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
            TotalValue totalValue = new TotalValue(total);
//            getSender().tell(getContext().ac);
//            getContext().actorFor("")
//            return totalValue;
//            getSelf().tell(totalValue);
//            getContext().actorFor("systemActor").tell(totalValue);
//            final ActorRef displayActor = getContext().actorOf(new Props(DisplayActor.class));
//            displayActor.forward(totalValue, getContext());
//            actorSystem.actorFor("systemActor").
//            actorSystem.actorFor("systemActor").tell(totalValue);
//              getSender().tell(totalValue, getSelf());

            try {
                final ActorRef displayActor = getContext().actorOf(new Props(DisplayActor.class));
                displayActor.tell(totalValue);
            } catch (Exception e) {
              getSender().tell(new akka.actor.Status.Failure(e), getSelf());
              throw e;
            }
        }
    }
}
