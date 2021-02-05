package IA.Azamon;

import aima.search.framework.HeuristicFunction;

public class AzaHeuristicFunction1 implements HeuristicFunction {
    public double getHeuristicValue(Object father) {
        AzaState Solution = (AzaState) father;
        return Solution.getCost();
    }
}
