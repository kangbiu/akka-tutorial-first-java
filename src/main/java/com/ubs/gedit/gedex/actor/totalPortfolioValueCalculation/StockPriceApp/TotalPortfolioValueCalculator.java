package com.ubs.gedit.gedex.actor.totalPortfolioValueCalculation.StockPriceApp;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.dispatch.Future;
import akka.pattern.Patterns;
import akka.util.Duration;
import akka.util.Timeout;
import com.ubs.gedit.gedex.actor.totalPortfolioValueCalculation.actor.SystemActor;
import com.ubs.gedit.gedex.actor.totalPortfolioValueCalculation.message.StockSharesMap;
import com.ubs.gedit.gedex.actor.totalPortfolioValueCalculation.message.TotalValue;
import com.ubs.gedit.gedex.actor.totalPortfolioValueCalculation.utilities.FileReaderUtilities;

import java.util.Map;

import static akka.dispatch.Await.result;

public class TotalPortfolioValueCalculator {

    public static void main(String[] args ) throws Exception {

        Map<String, Integer> map = FileReaderUtilities.readTickers();
        StockSharesMap stockSharesMap = new StockSharesMap(map);

        final ActorSystem system = ActorSystem.create("TotalPortfolioValueCalculator");
        ActorRef systemActor = system.actorOf(new Props(SystemActor.class), "systemActor");

//        systemActor.tell(stockSharesMap);

        Timeout timeout = new Timeout(Duration.parse("120 seconds"));

        Future<Object> future = Patterns.ask(systemActor, stockSharesMap, timeout);

        TotalValue totalValue =  (TotalValue) result(future, timeout.duration());

        akka.pattern.Patterns.pipe(future).to(systemActor);

        System.out.print(totalValue.getTotalAsStringMessage());

        system.shutdown();


    }
}
