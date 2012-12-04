package com.ubs.gedit.gedex.actor.totalPortfolioValueCalculation.stockPriceApp;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.util.Duration;
import com.ubs.gedit.gedex.actor.totalPortfolioValueCalculation.actor.SystemActor;
import com.ubs.gedit.gedex.actor.totalPortfolioValueCalculation.message.StockSharesMap;
import com.ubs.gedit.gedex.actor.totalPortfolioValueCalculation.message.TotalValue;
import com.ubs.gedit.gedex.actor.totalPortfolioValueCalculation.utilities.FileReaderUtilities;

import java.util.Map;

import static akka.dispatch.Await.result;
import static akka.pattern.Patterns.ask;

public class TotalPortfolioValueCalculator {

    public static void main(String[] args ) throws Exception {

        Map<String, Integer> map = FileReaderUtilities.readTickers();
        StockSharesMap stockSharesMap = new StockSharesMap(map);

        long start = System.nanoTime();

        final ActorSystem system = ActorSystem.create("TotalPortfolioValueCalculator");
        ActorRef systemActor = system.actorOf(new Props(SystemActor.class), "systemActor");

        Duration timeoutDuration = Duration.parse("50 seconds");

        TotalValue totalValue = (TotalValue) result(
                ask(systemActor, stockSharesMap, timeoutDuration.toMillis()), timeoutDuration);

        long end = System.nanoTime();

        System.out.println("Total Value: "+ totalValue.getTotal());
        System.out.println("Total Number of Stocks: "+ map.size());
        System.out.println("Total Time:" + (end - start) / 1.0e9);

        system.shutdown();

    }
}
