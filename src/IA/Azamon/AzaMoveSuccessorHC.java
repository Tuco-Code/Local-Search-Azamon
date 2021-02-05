package IA.Azamon;

import aima.search.framework.SuccessorFunction;
import aima.search.framework.Successor;

import java.util.ArrayList;
import java.util.List;

public class AzaMoveSuccessorHC implements SuccessorFunction {
    public List getSuccessors(Object father) {
        ArrayList retVal = new ArrayList();
        AzaState descendant = (AzaState) father;
        int NumPackages = descendant.getNumPack();
        int NumOffers = descendant.getNumOffers();
        for(int i = 0; i < NumPackages; ++i) {
            int actualOffer = descendant.getOfferID(i);
            for(int j = 0; j < NumOffers; ++j) {
                if(j != actualOffer) {
                    AzaState newDescendant = new AzaState(descendant);
                    boolean isValidMovement = newDescendant.validMovement(i, j);
                    if(isValidMovement) {
                        newDescendant.movePackage(i, j, actualOffer);
                        Successor succ = new Successor("move package " + i + " from offer " + actualOffer + " to " + j + " current cost: " + newDescendant.getCost(), newDescendant);
                        retVal.add(succ);
                    }
                }
            }
        }
        return retVal;
    }
}