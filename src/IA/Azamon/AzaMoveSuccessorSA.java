package IA.Azamon;

import aima.search.framework.SuccessorFunction;
import aima.search.framework.Successor;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AzaMoveSuccessorSA implements SuccessorFunction {
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

        return retVal;
    }
}