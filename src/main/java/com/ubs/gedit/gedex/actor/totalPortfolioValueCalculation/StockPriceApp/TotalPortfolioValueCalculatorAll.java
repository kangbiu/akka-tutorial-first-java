package com.ubs.gedit.gedex.actor.totalPortfolioValueCalculation.stockPriceApp;

import akka.actor.*;
import akka.util.Duration;
import com.ubs.gedit.gedex.actor.totalPortfolioValueCalculation.actor.SystemActor;
import com.ubs.gedit.gedex.actor.totalPortfolioValueCalculation.message.StockSharesMap;
import com.ubs.gedit.gedex.actor.totalPortfolioValueCalculation.message.TotalValue;
import com.ubs.gedit.gedex.actor.totalPortfolioValueCalculation.utilities.FileReaderUtilities;
import com.ubs.gedit.gedex.actor.totalPortfolioValueCalculation.utilities.YahooFinancePriceFetcher;

import java.io.IOException;
import java.util.Map;

import static akka.dispatch.Await.result;
import static akka.pattern.Patterns.ask;

public class TotalPortfolioValueCalculatorAll {


    public static void main(String[] args) throws Exception {

        Map<String, Integer> map = FileReaderUtilities.readTickers();

        System.out.println("=== Start Single Thread Example === ");

        long start = System.nanoTime();

        double total = 0;

        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            try {
                final double price = YahooFinancePriceFetcher.getPrice(entry.getKey());
                total += price * entry.getValue();
            } catch (IOException io) {
                System.out.println("FAIL TO GET THE STOCK PRICE:" + entry.getKey() + "Cause: " + io.getCause());
            }
        }
        long end = System.nanoTime();

        System.out.println("Total Value: " + total);
        System.out.println("Total Number of Stocks: " + map.size());
        System.out.println("Total Time:" + (end - start) / 1.0e9);

        System.out.println("=== End === ");
        System.out.println();

        System.out.println("=== Start Actor Example === ");

        final int NUMBER_OF_WORKER_4 = 4;
        System.out.println("Number of Worker:" + NUMBER_OF_WORKER_4);

        start = System.nanoTime();

        final StockSharesMap stockSharesMap = new StockSharesMap(map);

        final ActorSystem system = ActorSystem.create("TotalPortfolioValueCalculator");
        ActorRef systemActor = system.actorOf(new Props(new UntypedActorFactory() {
            public UntypedActor create() {
                return new SystemActor(NUMBER_OF_WORKER_4);
            }
        }), "systemActor");

        Duration timeoutDuration = Duration.parse("50 seconds");

        TotalValue totalValue = (TotalValue) result(
                ask(systemActor, stockSharesMap, timeoutDuration.toMillis()), timeoutDuration);

        end = System.nanoTime();

        System.out.println("Total Value: " + totalValue.getTotal());
        System.out.println("Total Number of Stocks: " + map.size());
        System.out.println("Total Time:" + (end - start) / 1.0e9);

        System.out.println("=== End === ");
        System.out.println();

        System.out.println("=== Start Actor Example === ");

        final int NUMBER_OF_WORKER_40 = 40;
        System.out.println("Number of Worker:" + NUMBER_OF_WORKER_40);

        start = System.nanoTime();


        ActorRef systemActor2 = system.actorOf(new Props(new UntypedActorFactory() {
            public UntypedActor create() {
                return new SystemActor(NUMBER_OF_WORKER_40);
            }
        }), "systemActor2");


        totalValue = (TotalValue) result(
                ask(systemActor2, stockSharesMap, timeoutDuration.toMillis()), timeoutDuration);

        end = System.nanoTime();

        System.out.println("Total Value: " + totalValue.getTotal());
        System.out.println("Total Number of Stocks: " + map.size());
        System.out.println("Total Time:" + (end - start) / 1.0e9);

        system.shutdown();

        System.out.println("=== End === ");

        System.out.println();


    }
}
