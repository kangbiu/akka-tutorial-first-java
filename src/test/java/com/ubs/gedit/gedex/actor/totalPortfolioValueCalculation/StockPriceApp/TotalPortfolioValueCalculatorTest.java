package com.ubs.gedit.gedex.actor.totalPortfolioValueCalculation.stockPriceApp;

import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.dispatch.Await;
import akka.testkit.TestActorRef;
import akka.testkit.TestKit;
import akka.util.Duration;
import com.ubs.gedit.gedex.actor.totalPortfolioValueCalculation.actor.SystemActor;
import com.ubs.gedit.gedex.actor.totalPortfolioValueCalculation.message.StockSharesMap;
import com.ubs.gedit.gedex.actor.totalPortfolioValueCalculation.message.TotalValue;
import com.ubs.gedit.gedex.actor.totalPortfolioValueCalculation.utilities.FileReaderUtilities;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import static akka.pattern.Patterns.ask;

public class TotalPortfolioValueCalculatorTest extends TestKit {

    Duration timeout = Duration.parse("100 seconds");

    static ActorSystem  system = ActorSystem.create("test");
    TestActorRef<SystemActor>  supervisor = TestActorRef.apply(new Props(SystemActor.class), system);

    public TotalPortfolioValueCalculatorTest() {
        super(system);
    }

    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void testRetryStrategy() throws Exception {
        Map<String, Integer> map = FileReaderUtilities.readTickers("stocks_null.txt");
        StockSharesMap stockSharesMap = new StockSharesMap(map);

//        supervisor.tell(stockSharesMap);
//        Props systemActorPops = new Props(SystemActor.class);
//        ActorRef systemActor = system.actorOf(systemActorPops, "systemActor");
        TotalValue totalValue = (TotalValue) Await.result(ask(supervisor, stockSharesMap, timeout.toMillis()), timeout);
//        system.shutdown();
    }



}
