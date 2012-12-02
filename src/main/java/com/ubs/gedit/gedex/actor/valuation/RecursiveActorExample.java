package com.ubs.gedit.gedex.actor.valuation;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.actor.UntypedActorFactory;
import akka.routing.RoundRobinRouter;

import java.util.concurrent.ForkJoinPool;

public class RecursiveActorExample extends UntypedActor {

    private double currentPrice;
    private double strike;
    private double riskFreeRate;
    private double tau;
    private double annualVolatility;

    private ResultListener listener;

    private DigitalOptionValuationInput valuationInput = null;
    private int returnedSimulation = 0;
    private double totalResult = 0;
    private ActorRef worker;

    public RecursiveActorExample(double currentPrice, double strike, double riskFreeRateInPercent, double tau, double annualVolatilityInPercent, ResultListener listener) {
        this.currentPrice = currentPrice;
        this.strike = strike;
        this.riskFreeRate = riskFreeRateInPercent / 100;
        this.tau = tau;
        this.annualVolatility = annualVolatilityInPercent / 100;

        this.listener = listener;
    }

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof DigitalOptionValuationInput) {
            assertIsNull(valuationInput, "You need to create another Actor example instance as this one is already running");
            valuationInput = (DigitalOptionValuationInput) message;

            //single worker
            worker = getContext().actorOf(new Props(new UntypedActorFactory() {
                public UntypedActor create() {
                    return new TotalPayoffWorker(tau, riskFreeRate, annualVolatility, currentPrice,
                            valuationInput.getTimeSteps(), valuationInput.getNumberOfSimulation(), strike, valuationInput.getPayOff());
                }
            }));
            worker.tell("getToWork", getSelf());

        } else if (message instanceof Double) {
            if (!getSender().equals(worker)) {
                getContext().system().log().error("Got an unexpected Double message from " + getSender());
                unhandled(message);
                return;
            }

            double average = ((Double) message) / valuationInput.getNumberOfSimulation();
            double pvAverage = average * Math.exp(-riskFreeRate * tau);

            listener.gotResult(getContext().system().name() + " with Digital Option with payoff $1, simulated return = " + pvAverage);
            getContext().system().shutdown();

        } else {
            unhandled(message);
        }
    }

    private void assertIsNull(Object obj, String message) {
        if (obj != null) {
            throw new RuntimeException(message);
        }
    }

}
