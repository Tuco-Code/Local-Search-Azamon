package IA.Azamon;

import aima.search.framework.SuccessorFunction;
import aima.search.framework.Successor;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AzaMoveSwapSuccessorSA implements SuccessorFunction {
    public List getSuccessors(Object father) {
        ArrayList retVal = new ArrayList();
        Random myRandom = new Random();
        AzaState descendant = (AzaState) father;
        int NumPackages = descendant.getNumPack();
        int NumOffers = descendant.getNumOffers();
        int randomPackage = myRandom.nextInt(NumPackages);
        int actualOffer = descendant.getOfferID(randomPackage);
        int randomOffer = myRandom.nextInt(NumOffers);
        AzaState newDescendant = new AzaState(descendant);
        while ((actualOffer == randomOffer) || !(newDescendant.validMovement(randomPackage, randomOffer))) {
            randomOffer = myRandom.nextInt(NumOffers);
            randomPackage = myRandom.nextInt(NumPackages);
            actualOffer = newDescendant.getOfferID(randomPackage);
        }
        newDescendant.movePackage(randomPackage, randomOffer, actualOffer);
        Successor succ = new Successor("move package " + randomPackage + " from offer " + actualOffer + " to " + randomOffer + " current cost: " + newDescendant.getCost(), newDescendant);
        retVal.add(succ);

        boolean seguir = true;
        for(int i = 0; i < NumPackages && seguir; ++i) {
            int actualOffer1 = descendant.getOfferID(i);
            for(int j = i+1; j < NumPackages && seguir; ++j) {
                int actualOffer2 = descendant.getOfferID(j);
                AzaState newDescendant1 = new AzaState(descendant);
                boolean isValidMovement1 = newDescendant1.validMovement(i, actualOffer2);
                boolean isValidMovement2 = newDescendant1.validMovement(j, actualOffer1);
                if(isValidMovement1 && isValidMovement2) {
                    seguir = false;
                    newDescendant1.swapPackages(i, actualOffer1, j, actualOffer2);
                    Successor succ1 = new Successor("swapped packages: " + i + " to offer " + actualOffer2 + " and package " + j + " to package " + actualOffer1 + " current cost: " + newDescendant.getCost(), newDescendant);
                    retVal.add(succ);
                }
            }
        }
        return retVal;
    }
}