package IA.Azamon;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class AzaState {
    /*
        Class independent from AIMA classes
        It implements the representation of an state/solution in the state/solution space
        of the package-carriers optimization problem.
     */

    /*
        State data structure:
            -ArrayList containing the PACKAGES static information for the problem.
            -ArrayList containing the CARRIERS static information for the problem.
            -ArrayList containing the current assignment between packages and carriers such that
                (array[i] = k) and CARRIERS[k] is the carrier carrying the package PACKAGES[i]
            -ArrayList containing the weight still available for each carrier to be transported
                (array[i] = w) and w is the weight CARRIERS[i] can still carry
            -cost of the current assignment, which must account for the cost
                of both the carrier service and the storage costs
            -happiness as a measure of the anticipated days of shipping for the whole current assignment
     */

    private static Paquetes packages;
    private static Transporte carriers;

    private ArrayList<Integer> assignment;
    private ArrayList<Double> remainingWeight;

    private double cost;
    private int happiness;

    /*
        Constructors:
        First constructor (DEPRECATED) used to create the first state/solution representation with explicit packages and carriers objects
        Second constructor used to create the first state/solution representation with implicit packages and carriers objects
        Third constructor used to generate any descendant from the first one
     */

    public AzaState(int numPackages, int packageSeed, double proportion, int carrierSeed) {
        assignment = new ArrayList<Integer>(Arrays.asList(new Integer[numPackages]));
        Collections.fill(assignment, -1);
        packages = new Paquetes(numPackages, packageSeed);
        carriers = new Transporte(packages, proportion, carrierSeed);
        remainingWeight = new ArrayList<Double>(carriers.size());
        for (int i = 0; i < carriers.size(); ++i) 
            remainingWeight.add(carriers.get(i).getPesomax());
        cost = 0;
        happiness = 0;
    }

    public AzaState(AzaState father)
    {
        packages = father.packages;
        carriers = father.carriers;
        assignment = new ArrayList<Integer>(father.assignment);
        remainingWeight = new ArrayList<Double>(father.remainingWeight);
        cost = father.cost;
        happiness = father.happiness;
    }

    //Getters
    public int getNumPack() {
        return packages.size();
    }

    public int getNumOffers() {
        return carriers.size();
    }

    public int getOfferID(int packID) {
        return assignment.get(packID);
    }

    public double getCost()
    {
        return cost;
    }

    public double getHappiness()
    {
        return happiness;
    }
    
    //Testing
    public void printPackagePriorities()
    {
        for (int i = 0; i < packages.size(); i++)
        {
            System.out.println(packages.get(i).getPrioridad());
        }
    }

    // Generates initial solution 
    public void generateInitSolution1() {
        // Sorting 
        Collections.sort(packages, new CustomPackageComparator());
        Collections.sort(carriers, new CustomCarrierComparator());

        int offerID = 0;
        double emptyOfferSpace = remainingWeight.get(offerID);
    
        for (int i = 0; i < packages.size(); ++i) {
            double packageWeight = packages.get(i).getPeso();
            if(packageWeight <= emptyOfferSpace) {
                assignment.set(i, offerID);
                emptyOfferSpace -= packageWeight;
            }
            else { 
                remainingWeight.set(offerID, emptyOfferSpace);
                while (packageWeight > emptyOfferSpace) {
                    ++offerID;
                    emptyOfferSpace = remainingWeight.get(offerID);
                }
                assignment.set(i, offerID);
                emptyOfferSpace -= packageWeight;
            }   
        }
        // Set last remaining weight of the offer
        remainingWeight.set(offerID, emptyOfferSpace);  
        calculateCost();
        calculateHappiness();
    }

    public void generateInitSolutionON2() {
        // Sorting
        Collections.sort(packages, new CustomPackageComparator());
        Collections.sort(carriers, new CustomCarrierComparator());

        int offerID = 0;
        double emptyOfferSpace = remainingWeight.get(offerID);

        for (int i = 0; i < packages.size(); ++i) {
            double packageWeight = packages.get(i).getPeso();
            if(packageWeight <= emptyOfferSpace) {
                assignment.set(i, offerID);
                emptyOfferSpace -= packageWeight;
            }
            else {
                remainingWeight.set(offerID, emptyOfferSpace);
                while (packageWeight > emptyOfferSpace) {
                    ++offerID;
                    emptyOfferSpace = remainingWeight.get(offerID);
                }
                assignment.set(i, offerID);
                remainingWeight.set(offerID,emptyOfferSpace - packageWeight);
                offerID = 0;
                emptyOfferSpace = remainingWeight.get(offerID);
            }
        }
        // Set last remaining weight of the offer
        remainingWeight.set(offerID, emptyOfferSpace);
        calculateCost();
        calculateHappiness();
    }

    public void generateInitSolution2() {
        // Sorting
        Collections.sort(packages, new CustomPackageComparator());
        Collections.sort(carriers, new CustomCarrierComparator());
        
        int offerID = carriers.size()-1;
        double emptyOfferSpace = remainingWeight.get(offerID);

        for (int i = packages.size()-1; i >= 0; --i) {
            double packageWeight = packages.get(i).getPeso();
            if (packageWeight <= emptyOfferSpace && validMovement(i, offerID)) {
                assignment.set(i,offerID);
                emptyOfferSpace -= packageWeight;
            }
            else {
                remainingWeight.set(offerID, emptyOfferSpace);
                while (!validMovement(i, offerID) || packageWeight > emptyOfferSpace) {
                    --offerID;
                    emptyOfferSpace = remainingWeight.get(offerID);
                }
                assignment.set(i, offerID);
                emptyOfferSpace -= packageWeight;
            }
        }
        // Set last remaining weight of the offer
        remainingWeight.set(offerID, emptyOfferSpace);  
        calculateCost();
        calculateHappiness();
        
    }

    public void calculateCost() {
        cost = 0;
        for (int i =0; i < assignment.size(); ++i) {
            cost += carriers.get(assignment.get(i)).getPrecio() * packages.get(i).getPeso();
            int pkgPriority = packages.get(i).getPrioridad();
            double shoppingCost = 0.0;
            if (pkgPriority == 0) shoppingCost = 5;
            else if (pkgPriority == 1) shoppingCost = 3;
            else shoppingCost = 1.5; 
            cost -= shoppingCost;
            if(carriers.get(assignment.get(i)).getDias() == 5) 
                cost += 0.5 * packages.get(i).getPeso();
            else if (carriers.get(assignment.get(i)).getDias() >= 3)
                cost += 0.25 * packages.get(i).getPeso();
        }
    }

    public void calculateHappiness() {
        for (int i = 0; i < assignment.size(); ++i) {
            int pkgPriority = packages.get(i).getPrioridad();
            int offerDays = carriers.get(assignment.get(i)).getDias();
            if (pkgPriority == 1 && offerDays == 1) ++happiness;
            else if (pkgPriority == 2) happiness += Math.max(0,4 - offerDays);
        }
    }

    public int calculateHappiness(int pkgPriority, int offerDays) {
        int valHappiness = 0;
        if (pkgPriority == 1 && offerDays == 1) ++valHappiness;
        else if (pkgPriority == 2) valHappiness = Math.max(0,4 - offerDays);
        return valHappiness;
    }

    public boolean validMovement(int packID, int offerID) {
        double packWeight = packages.get(packID).getPeso();
        double emptyOffer = remainingWeight.get(offerID);
        int packagePriority = packages.get(packID).getPrioridad();
        int offerDays = carriers.get(offerID).getDias();
        if(packWeight <= emptyOffer && compatibleOffer(packagePriority,offerDays)) return true;
        else return false;
    }

    private boolean compatibleOffer(int packagePriority, int offerDays) {
        if (offerDays > 1 && packagePriority == 0) return false;
        if (offerDays > 3 && packagePriority == 1) return false;
        return true;
    }

    public void movePackage(int packID, int offerID, int oldOffer) {
        double packWeight = packages.get(packID).getPeso();
        double emptyWeightOffer = remainingWeight.get(offerID);
        double emptyWeightOldOffer = remainingWeight.get(oldOffer);
        double OfferPrice = carriers.get(offerID).getPrecio();
        double OldOfferPrice = carriers.get(oldOffer).getPrecio();
        int OfferDays = carriers.get(offerID).getDias();
        int OldOfferDays = carriers.get(oldOffer).getDias();
        int packPriority = packages.get(packID).getPrioridad();
        assignment.set(packID,offerID);
        remainingWeight.set(offerID, emptyWeightOffer-packWeight);
        remainingWeight.set(oldOffer, emptyWeightOldOffer+packWeight);
        cost -= (packWeight * OldOfferPrice);
        cost += (packWeight * OfferPrice);
        happiness -= calculateHappiness(packPriority, OldOfferDays);
        happiness += calculateHappiness(packPriority, OfferDays);
        if(OldOfferDays == 5) cost -= 0.5 * packWeight;
        else if(OldOfferDays >= 3) cost -= 0.25 * packWeight;
        if(OfferDays == 5) cost += 0.5 * packWeight;
        else if (OfferDays >= 3) cost += 0.25 * packWeight;
    }

    public void swapPackages(int packID1, int offerID1, int packID2, int offerID2) {
        movePackage(packID1,offerID2,offerID1);
        movePackage(packID2,offerID1,offerID2);
    }

    public String print() {
        StringBuffer printState = new StringBuffer();
        printState.append("\nSTATE SPECIFICATION\n");
        printState.append("Assignment:\n");
        for (int i = 0; i < assignment.size(); ++i) {
            printState.append("Package " + i + ": " + packages.get(i).getPeso() + "kg/PR" + packages.get(i).getPrioridad());
            printState.append(" -> Offer " + assignment.get(i) + "\n");
        }
        printState.append("Offers: \n");
        for (int i = 0; i < carriers.size(); ++i) {
            double offerWeight = carriers.get(i).getPesomax() - remainingWeight.get(i);
            printState.append("Offer " + i + ": " + carriers.get(i).getDias() + " days, " + 
            offerWeight + "/" + carriers.get(i).getPesomax() + "kg, " + carriers.get(i).getPrecio() + " €/kg\n");
        }
        printState.append("\nCOST: " + cost);
        printState.append("\nHAPPINESS: " + happiness);
        return printState.toString();
    }

    public String printPlanning() {
        StringBuffer planning = new StringBuffer();
        planning.append("\nPLANNING VISUALIZATION\n");
        for (int i = 0; i < carriers.size(); ++i) {
            planning.append("Offer " + i + ":");
            for (int j = 0; j < packages.size(); ++j) 
                if (assignment.get(j) == i) planning.append(" " + j);
            planning.append("\n");
        }
        return planning.toString();
    }
}
