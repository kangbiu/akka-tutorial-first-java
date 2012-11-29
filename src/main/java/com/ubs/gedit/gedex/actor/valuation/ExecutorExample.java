package com.ubs.gedit.gedex.actor.valuation;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ExecutorExample {

    public static final int NTHREDS = 4;

    private double currentPrice;
    private double strike;
    private double riskFreeRate;
    private double tau;
    private double annualVolatility;

    public ExecutorExample(double currentPrice, double strike, double riskFreeRateInPercent, double tau, double annualVolatilityInPercent) {
        this.currentPrice = currentPrice;
        this.strike = strike;
        this.riskFreeRate = riskFreeRateInPercent / 100;
        this.tau = tau;
        this.annualVolatility = annualVolatilityInPercent / 100;
    }

    public double digitalOption(double payoff, int timePeriods, int simulation) throws Exception {



        ExecutorService executor = Executors.newFixedThreadPool(NTHREDS);
        List<Future<Double>> priceResults = new ArrayList<Future<Double>>();
        for (int i = 0; i < simulation; i++) {
            Callable<Double> worker = new FinalPriceCallableSimulation(tau, riskFreeRate, annualVolatility, currentPrice, timePeriods);
            Future<Double> submit = executor.submit(worker);
            priceResults.add(submit);
        }
        System.out.println("Number of price simulated = " + priceResults.size());

        double totalResult = 0;
        // Now retrieve the result
        for (Future<Double> price : priceResults) {
            double result = price.get() >= strike ? payoff : 0;
            totalResult += result;
        }
        executor.shutdown();

        double average = totalResult / simulation;
        double pvAverage = average * Math.exp(-riskFreeRate*tau);
//        System.out.println("Average = " + average);
//        System.out.println("pvAverage = " + pvAverage);

        return pvAverage;
    }

    public double blackScholePrice() {
        return 0;
    }
}
