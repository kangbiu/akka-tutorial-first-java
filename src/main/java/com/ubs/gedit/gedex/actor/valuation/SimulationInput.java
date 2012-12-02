package com.ubs.gedit.gedex.actor.valuation;

public class SimulationInput {
    private final double tau;
    private final double riskFreeRate;
    private final double annualVolatility;
    private final double currentPrice;
    private final int timeSteps;

    public double getTau() {
        return tau;
    }

    public double getRiskFreeRate() {
        return riskFreeRate;
    }

    public double getAnnualVolatility() {
        return annualVolatility;
    }

    public double getCurrentPrice() {
        return currentPrice;
    }

    public int getTimeSteps() {
        return timeSteps;
    }

    public SimulationInput(double tau, double riskFreeRate, double annualVolatility, double currentPrice, int timeSteps){
        this.tau = tau;
        this.riskFreeRate = riskFreeRate;
        this.annualVolatility = annualVolatility;
        this.currentPrice = currentPrice;
        this.timeSteps = timeSteps;
    }
}
