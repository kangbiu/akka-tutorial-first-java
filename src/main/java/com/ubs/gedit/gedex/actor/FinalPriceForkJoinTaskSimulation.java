package com.ubs.gedit.gedex.actor;

import com.ubs.gedit.gedex.actor.util.Distribution;

import java.util.concurrent.Callable;
import java.util.concurrent.RecursiveTask;

public class FinalPriceForkJoinTaskSimulation extends RecursiveTask {

    private double tau;
    private double riskFreeRate;
    private double annualVolatility;
    private double currentPrice;
    private int timePeriods;

    public FinalPriceForkJoinTaskSimulation(double tau, double riskFreeRate, double annualVolatility, double currentPrice, int timePeriods) {
        this.tau = tau;
        this.riskFreeRate = riskFreeRate;
        this.annualVolatility = annualVolatility;
        this.currentPrice = currentPrice;
        this.timePeriods = timePeriods;
    }

    private double getPriceSimulation(int timePeriods) {
        double time = tau / timePeriods;
        double drift = (riskFreeRate - 0.5 * Math.pow(annualVolatility, 2)) * time;
        double volatility = annualVolatility * Math.sqrt(time);

        double price = currentPrice;
        for (int i = 0; i < timePeriods; i++) {
            double normRandom = Distribution.NormInv(Math.random(), 0, 1);
            price = price * Math.exp(drift + volatility * normRandom);
//            System.out.println("** price = " + price);
        }
        return price;
    }

    @Override
    protected Object compute() {
        return getPriceSimulation(timePeriods);
    }
}
