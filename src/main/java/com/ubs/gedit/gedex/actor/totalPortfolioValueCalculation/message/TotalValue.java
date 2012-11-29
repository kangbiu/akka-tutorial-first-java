package com.ubs.gedit.gedex.actor.totalPortfolioValueCalculation.message;

public class TotalValue {

    final private double total;

    public TotalValue(double total) {
        this.total = total;
    }

    public double getTotal() {
        return total;
    }

    public String getTotalAsStringMessage(){
        return "Total is " + Double.toString(total);
    }
}
