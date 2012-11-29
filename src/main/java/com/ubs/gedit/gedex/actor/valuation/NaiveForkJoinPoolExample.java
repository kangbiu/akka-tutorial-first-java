package com.ubs.gedit.gedex.actor.valuation;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class NaiveForkJoinPoolExample {

    private double currentPrice;
    private double strike;
    private double riskFreeRate;
    private double tau;
    private double annualVolatility;

    public NaiveForkJoinPoolExample(double currentPrice, double strike, double riskFreeRateInPercent, double tau, double annualVolatilityInPercent) {
        this.currentPrice = currentPrice;
        this.strike = strike;
        this.riskFreeRate = riskFreeRateInPercent / 100;
        this.tau = tau;
        this.annualVolatility = annualVolatilityInPercent / 100;
    }

    public double digitalOption(double payoff, int timePeriods, int simulation) throws Exception {

        ForkJoinPool pool = new ForkJoinPool(); //Use all available processors

        List<Future<Double>> priceResults = new ArrayList<Future<Double>>();
        for (int i = 0; i < simulation; i++) {
            Callable<Double> worker = new FinalPriceCallableSimulation(tau, riskFreeRate, annualVolatility, currentPrice, timePeriods);
            Future<Double> submit = pool.submit(worker);
            priceResults.add(submit);
        }
        System.out.println("Number of price simulated = " + priceResults.size());

        double totalResult = 0;
        // Now retrieve the result
        for (Future<Double> price : priceResults) {
            double result = price.get() >= strike ? payoff : 0;
            totalResult += result;
        }
        pool.shutdown();

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
