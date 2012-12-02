package com.ubs.gedit.gedex.actor.valuation;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.routing.RoundRobinRouter;

public class ActorExample extends UntypedActor {

    public static final int NUMBER_OF_WORKERS = 4;

    private double currentPrice;
    private double strike;
    private double riskFreeRate;
    private double tau;
    private double annualVolatility;

    private ResultListener listener;
    private ActorRef simulators;

    private DigitalOptionValuationInput valuationInput = null;
    private int returnedSimulation = 0;
    private double totalResult = 0;

    public ActorExample(double currentPrice, double strike, double riskFreeRateInPercent, double tau, double annualVolatilityInPercent, ResultListener listener) {
        this.currentPrice = currentPrice;
        this.strike = strike;
        this.riskFreeRate = riskFreeRateInPercent / 100;
        this.tau = tau;
        this.annualVolatility = annualVolatilityInPercent / 100;
        this.listener = listener;

        simulators = getContext().actorOf(
                new Props(FinalPriceSimulationActor.class).withRouter(new RoundRobinRouter(NUMBER_OF_WORKERS)), "simulators");
//                new Props(FinalPriceSimulationActor.class).withRouter(new SmallestMailboxRouter(NUMBER_OF_WORKERS)), "simulators");
    }


//    public double digitalOption(double payoff, int timePeriods, int simulation) throws Exception {
//        List<Future<Double>> priceResults = new ArrayList<Future<Double>>();
//        for (int i = 0; i < simulation; i++) {
//            Callable<Double> worker = new FinalPriceCallableSimulation(tau, riskFreeRate, annualVolatility, currentPrice, timePeriods);
//            Future<Double> submit = executor.submit(worker);
//            priceResults.add(submit);
//        }
//        System.out.println("Number of price simulated = " + priceResults.size());
//
//        double totalResult = 0;
//        // Now retrieve the result
//        for (Future<Double> price : priceResults) {
//            double result = price.get() >= strike ? payoff : 0;
//            totalResult += result;
//        }
//
//
//        double average = totalResult / simulation;
//        double pvAverage = average * Math.exp(-riskFreeRate * tau);
//
//        return pvAverage;
//    }


    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof DigitalOptionValuationInput) {
            assertIsNull(valuationInput, "You need to create another Actor example instance as this one is already running");
            valuationInput = (DigitalOptionValuationInput) message;
            for (int i = 0; i < valuationInput.getNumberOfSimulation(); i++) {
                SimulationInput input = new SimulationInput(tau, riskFreeRate, annualVolatility, currentPrice, valuationInput.getTimeSteps());
                simulators.tell(input, getSelf());
            }
        } else if (message instanceof Double) {
            returnedSimulation++;
            double price = (Double) message;
            double result = price >= strike ? valuationInput.getPayOff() : 0;
            totalResult += result;

            if (returnedSimulation == valuationInput.getNumberOfSimulation()){
                double average = totalResult / valuationInput.getNumberOfSimulation();
                double pvAverage = average * Math.exp(-riskFreeRate * tau);

                listener.gotResult(getContext().system().name() + " with Digital Option with payoff $1, simulated return = " + pvAverage);
                getContext().system().shutdown();
            }

        } else {
            unhandled(message);
        }
    }

    private void assertIsNull(Object obj, String message) {
        if(obj != null){
            throw new RuntimeException(message);
        }
    }

}
