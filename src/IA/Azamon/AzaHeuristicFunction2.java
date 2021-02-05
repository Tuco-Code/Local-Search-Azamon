package IA.Azamon;

import aima.search.framework.HeuristicFunction;

public class AzaHeuristicFunction2 implements HeuristicFunction {
    // Esta es la heuristica de felicidad hay que cambiarla
    public double getHeuristicValue(Object father) {
        AzaState Solution = (AzaState) father;
        return Solution.getCost() - 5*(double)Solution.getHappiness();
    }
}
