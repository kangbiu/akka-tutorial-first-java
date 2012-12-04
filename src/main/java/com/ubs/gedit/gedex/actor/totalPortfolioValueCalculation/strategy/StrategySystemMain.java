package com.ubs.gedit.gedex.actor.totalPortfolioValueCalculation.strategy;

import akka.actor.*;
import com.ubs.gedit.gedex.actor.totalPortfolioValueCalculation.message.StockTicker;

public class StrategySystemMain {
    public static void main(String arg[]) throws Exception {



        final ActorSystem system = ActorSystem.create("TotalPortfolioValueCalculator");
        ActorRef strategySystemActor = system.actorOf(new Props(new UntypedActorFactory() {
            public UntypedActor create() {
                return new StrategySystemActor();
            }
        }), "strategySystemActor");

        strategySystemActor.tell(new StockTicker("0005.hk"));
        strategySystemActor.tell(new StockTicker("0001.hk"));
        strategySystemActor.tell(new StockTicker("HHHHHHHH"));
        strategySystemActor.tell(new StockTicker("0002.hk"));

    }
}
