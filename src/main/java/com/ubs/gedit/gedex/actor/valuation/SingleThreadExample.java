package com.ubs.gedit.gedex.actor.valuation;

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

    public double digitalOption(double payoff, int timePeriods, int simulation) throws Exception {

        double totalResult = 0;
        FinalPriceCallableSimulation sim = new FinalPriceCallableSimulation(tau, riskFreeRate, annualVolatility, currentPrice, timePeriods);

        for (int i = 0; i < simulation; i++) {
            double price = (Double) sim.call();
            double result = price >= strike ? payoff : 0;
            totalResult += result;
        }
        double average = totalResult / simulation;
        double pvAverage = average * Math.exp(-riskFreeRate*tau);
//        System.out.println("Average = " + average);
//        System.out.println("pvAverage = " + pvAverage);

        return pvAverage;
    }

//    private double getPriceSimulation(int timePeriods) {
//        double time = tau / timePeriods;
//        double drift = (riskFreeRate - 0.5 * Math.pow(annualVolatility, 2)) * time;
//        double volatility = annualVolatility * Math.sqrt(time);
//
//        double price = currentPrice;
//        for (int i = 0; i < timePeriods; i++) {
//            double normRandom = Distribution.NormInv(Math.random(), 0, 1);
//            price = price * Math.exp(drift + volatility * normRandom);
////            System.out.println("** price = " + price);
//        }
//        return price;
//    }

    public double blackScholePrice() {
        return 0;
    }
}
