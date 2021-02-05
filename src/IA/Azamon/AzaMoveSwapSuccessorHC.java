package IA.Azamon;

import aima.search.framework.Successor;
import aima.search.framework.SuccessorFunction;

import java.util.ArrayList;
import java.util.List;

public class AzaMoveSwapSuccessorHC implements SuccessorFunction {
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
            for(int j = i+1; j < NumPackages; ++j) {
                int actualOffer2 = descendant.getOfferID(j);
                AzaState newDescendant = new AzaState(descendant);
                boolean isValidMovement1 = newDescendant.validMovement(i, actualOffer2);
                boolean isValidMovement2 = newDescendant.validMovement(j, actualOffer);
                if(isValidMovement1 && isValidMovement2) {
                    newDescendant.swapPackages(i, actualOffer, j, actualOffer2);
                    Successor succ = new Successor("swapped packages: " + i + " to offer " + actualOffer2 + " and package " + j + " to package " + actualOffer + " current cost: " + newDescendant.getCost(), newDescendant);
                    retVal.add(succ);
                }
            }
        }
        return retVal;
    }
}
