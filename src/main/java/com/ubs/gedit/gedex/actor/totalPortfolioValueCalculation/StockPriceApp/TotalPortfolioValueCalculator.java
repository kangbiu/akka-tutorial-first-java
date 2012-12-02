package com.ubs.gedit.gedex.actor.totalPortfolioValueCalculation.StockPriceApp;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import com.ubs.gedit.gedex.actor.totalPortfolioValueCalculation.actor.SystemActor;
import com.ubs.gedit.gedex.actor.totalPortfolioValueCalculation.message.StockSharesMap;
import com.ubs.gedit.gedex.actor.totalPortfolioValueCalculation.utilities.FileReaderUtilities;

import java.util.Map;

public class TotalPortfolioValueCalculator {

    public static void main(String[] args ) throws Exception {

        Map<String, Integer> map = FileReaderUtilities.readTickers();
        StockSharesMap stockSharesMap = new StockSharesMap(map);

        final ActorSystem system = ActorSystem.create("TotalPortfolioValueCalculator");
        ActorRef systemActor = system.actorOf(new Props(SystemActor.class), "systemActor");

        systemActor.tell(stockSharesMap);

    }
}
