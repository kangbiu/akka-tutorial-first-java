package com.ubs.gedit.gedex.actor.valuation;

public class DigitalOptionValuationInput {
    private final int payOff;
    private final int timeSteps;
    private final int numberOfSimulation;

    public DigitalOptionValuationInput(int payOff, int timeSteps, int numberOfSimulation) {
        this.payOff = payOff;
        this.timeSteps = timeSteps;
        this.numberOfSimulation = numberOfSimulation;
    }

    public int getPayOff() {
        return payOff;
    }

    public int getTimeSteps() {
        return timeSteps;
    }

    public int getNumberOfSimulation() {
        return numberOfSimulation;
    }
}
