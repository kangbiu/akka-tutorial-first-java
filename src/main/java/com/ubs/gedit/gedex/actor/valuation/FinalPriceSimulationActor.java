package com.ubs.gedit.gedex.actor.valuation;

import akka.actor.UntypedActor;
import com.ubs.gedit.gedex.actor.util.Distribution;

public class FinalPriceSimulationActor extends UntypedActor{

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof SimulationInput){
            SimulationInput input = (SimulationInput) message;
            double price = simulateFinalPrice(input);
            getSender().tell(price, getSelf());

        }else{
            unhandled(message);
        }
    }

    private double simulateFinalPrice(SimulationInput input) {
        double time = input.getTau() / input.getTimeSteps();
        double drift = (input.getRiskFreeRate() - 0.5 * Math.pow(input.getAnnualVolatility(), 2)) * time;
        double volatility = input.getAnnualVolatility() * Math.sqrt(time);

        double price = input.getCurrentPrice();
        for (int i = 0; i < input.getTimeSteps(); i++) {
            double normRandom = Distribution.NormInv(Math.random(), 0, 1);
            price = price * Math.exp(drift + volatility * normRandom);
//            System.out.println("** price = " + price);
        }
        return price;
    }
}
