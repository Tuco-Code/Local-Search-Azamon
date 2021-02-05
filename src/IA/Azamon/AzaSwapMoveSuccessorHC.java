package IA.Azamon;

import aima.search.framework.SuccessorFunction;
import aima.search.framework.Successor;

import java.util.ArrayList;
import java.util.List;

public class AzaSwapMoveSuccessorHC implements SuccessorFunction {
    public List getSuccessors(Object father) {
        ArrayList retVal = new ArrayList();
        AzaState descendant = (AzaState) father;
        int NumPackages = descendant.getNumPack();
        for (int i = 0; i < NumPackages; ++i) {
            boolean doMove = true;
            int actualOffer1 = descendant.getOfferID(i);
            for (int j = i + 1; j < NumPackages; ++j) {
                int actualOffer2 = descendant.getOfferID(j);
                AzaState newDescendant = new AzaState(descendant);
                boolean isValidMovement1 = newDescendant.validMovement(i, actualOffer2);
                boolean isValidMovement2 = newDescendant.validMovement(j, actualOffer1);
                if (isValidMovement1 && isValidMovement2) {
                    doMove = false;
                    newDescendant.swapPackages(i, actualOffer1, j, actualOffer2);
                    Successor succ = new Successor("swapped packages: " + i + " to offer " + actualOffer2 + " and package " + j + " to package " + actualOffer1 + " current cost: " + newDescendant.getCost(), newDescendant);
                    retVal.add(succ);
                }
            }
            if (doMove) {
                int NumOffers = descendant.getNumOffers();
                for (int k = 0; k < NumOffers; ++k) {
                    if (k != actualOffer1) {
                        AzaState newDescendant = new AzaState(descendant);
                        boolean isValidMovement = newDescendant.validMovement(i, k);
                        if (isValidMovement) {
                            newDescendant.movePackage(i, k, actualOffer1);
                            Successor succ = new Successor("move package " + i + " from offer " + actualOffer1 + " to " + k + " current cost: " + newDescendant.getCost(), newDescendant);
                            retVal.add(succ);
                        }
                    }
                }
            }
        }
        return retVal;
    }
}
