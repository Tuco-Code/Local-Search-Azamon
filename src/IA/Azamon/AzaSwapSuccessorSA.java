package IA.Azamon;

import aima.search.framework.SuccessorFunction;
import aima.search.framework.Successor;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AzaSwapSuccessorSA implements SuccessorFunction {
    public List getSuccessors(Object father) {
        ArrayList retVal = new ArrayList();
        AzaState descendant = (AzaState) father;
        int NumPackages = descendant.getNumPack();
        boolean seguir = true;
        for (int i = 0; i < NumPackages && seguir; ++i) {
            int actualOffer1 = descendant.getOfferID(i);
            for (int j = i + 1; j < NumPackages && seguir; ++j) {
                int actualOffer2 = descendant.getOfferID(j);
                AzaState newDescendant = new AzaState(descendant);
                boolean isValidMovement1 = newDescendant.validMovement(i, actualOffer2);
                boolean isValidMovement2 = newDescendant.validMovement(j, actualOffer1);
                if (isValidMovement1 && isValidMovement2) {
                    seguir = false;
                    newDescendant.swapPackages(i, actualOffer1, j, actualOffer2);
                    Successor succ = new Successor("swapped packages: " + i + " to offer " + actualOffer2 + " and package " + j + " to package " + actualOffer1 + " current cost: " + newDescendant.getCost(), newDescendant);
                    retVal.add(succ);
                }
            }
        }
        return retVal;
    }
}