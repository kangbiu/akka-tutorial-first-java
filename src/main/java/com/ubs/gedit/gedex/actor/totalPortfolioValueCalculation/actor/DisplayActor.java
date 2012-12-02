package com.ubs.gedit.gedex.actor.totalPortfolioValueCalculation.actor;

import akka.actor.UntypedActor;
import com.ubs.gedit.gedex.actor.totalPortfolioValueCalculation.message.TotalValue;

public class DisplayActor extends UntypedActor {

    @Override
    public void onReceive(Object message) throws Exception {
        if(message instanceof TotalValue){
            System.out.print(((TotalValue) message).getTotalAsStringMessage());
        }

//        getContext().system().shutdown();
    }
}
