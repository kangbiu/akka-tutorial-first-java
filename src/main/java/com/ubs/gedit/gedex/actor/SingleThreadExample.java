package com.ubs.gedit.gedex.actor;

import com.ubs.gedit.gedex.actor.util.Distribution;

public class SingleThreadExample {

    private double currentPrice;
    private double strike;
    private double riskFreeRate;
    private double tau;
    private double annualVolatility;

    public SingleThreadExample(double currentPrice, double strike, double riskFreeRateInPercent, double tau, double annualVolatilityInPercent) {
        this.currentPrice = currentPrice;
        this.strike = strike;
        this.riskFreeRate = riskFreeRateInPercent / 100;
        this.tau = tau;
        this.annualVolatility = annualVolatilityInPercent / 100;
    }

    public static void main(String[] args) {

        SingleThreadExample singleThread = new SingleThreadExample(36, 40, 4, 1, 24);
        System.out.println("Black Scholes result = " + singleThread.blackScholePrice());

        long startTime = System.currentTimeMillis();
        double result = singleThread.digitalOption(1, 30, 100000);
        double duration = (System.currentTimeMillis() - startTime);
        System.out.println("Digital Option with payoff $1, simulated return = " + result);
        System.out.println("(" + duration + " millisec); Java version = " + System.getProperty("java.version"));

    }

    private double digitalOption(double payoff, int timePeriods, int simulation) {

        double totalResult = 0;

        for (int i = 0; i < simulation; i++) {
            double price = getPriceSimulation(timePeriods);
            double result = price >= strike ? payoff : 0;
//            System.out.println("** price = " + price + "; result = " + result);
            totalResult += result;
        }
        double average = totalResult / simulation;
        double pvAverage = average * Math.exp(-riskFreeRate*tau);
//        System.out.println("Average = " + average);
//        System.out.println("pvAverage = " + pvAverage);

        return pvAverage;
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

    private double blackScholePrice() {
        return 0;
    }
}
