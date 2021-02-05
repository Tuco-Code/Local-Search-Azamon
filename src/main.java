import IA.Azamon.*;
import aima.search.framework.Problem;
import aima.search.framework.Search;
import aima.search.framework.SearchAgent;
import aima.search.informed.HillClimbingSearch;
import aima.search.informed.SimulatedAnnealingSearch;

import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Random;


public class main {

    static long startTime;

    public static void main(String[] args) {

        if (args.length != 8) {
            printUsage();
            System.exit(1);
        }

        try {

            int numPackages = Integer.parseInt(args[2]);
            int seed = Integer.parseInt(args[3]);
            double proportion = Double.parseDouble(args[4]);
            int initialSolutionID = Integer.parseInt(args[5]);
            int operatorID = Integer.parseInt(args[6]);
            int heuristicID = Integer.parseInt(args[7]);

            // Random seed
            Random random = new Random();
            if (seed == -1) seed = random.nextInt(5000);

            startTime = System.currentTimeMillis();
           
            AzaState state = new AzaState(numPackages, seed, proportion, seed);
            
            if (initialSolutionID == 1) state.generateInitSolution1();
            else state.generateInitSolution2();

            //System.out.println(state.print());
    
            if (args[0].equals("Hill") && args[1].equals("Climbing")) 
                AzaHillClimbing(state, operatorID, heuristicID);
            else if (args[0].equals("Simulated") && args[1].equals("Annealing"))
                AzaSimulatedAnnealing(state, operatorID, heuristicID);
            else printUsage();

        } catch (NumberFormatException e) {
            printUsage();
            System.exit(1);
        }
    }

    private static void AzaHillClimbing(AzaState state, int operator, int heuristic) {
        try {

            Problem prob;
            if(operator == 0 && heuristic == 1)
                prob = new Problem(state, new AzaSwapMoveSuccessorHC(), new AzaGoalTest(), new AzaHeuristicFunction1());
            else if(operator == 0)
                prob = new Problem(state, new AzaSwapMoveSuccessorHC(), new AzaGoalTest(), new AzaHeuristicFunction2());
            else if (operator == 1 && heuristic == 1)
                prob = new Problem(state, new AzaMoveSuccessorHC(), new AzaGoalTest(), new AzaHeuristicFunction1());
            else if (operator == 1)
                prob = new Problem(state, new AzaMoveSuccessorHC(), new AzaGoalTest(), new AzaHeuristicFunction2());
            else if(operator == 2 && heuristic == 1)
                prob = new Problem(state, new AzaMoveSwapSuccessorHC(), new AzaGoalTest(), new AzaHeuristicFunction1());
            else if(operator == 2)
                prob = new Problem(state, new AzaMoveSwapSuccessorHC(), new AzaGoalTest(), new AzaHeuristicFunction2());
            else if (heuristic == 1)
                prob = new Problem(state, new AzaSwapSuccessorHC(), new AzaGoalTest(), new AzaHeuristicFunction1());
            else
                prob = new Problem(state, new AzaSwapSuccessorHC(), new AzaGoalTest(), new AzaHeuristicFunction2());
            // Instantiate the search Algorithm
            Search algHC = new HillClimbingSearch();
            // Instantiate the search agent
            SearchAgent agentHC = new SearchAgent(prob, algHC);

            long stopTime = System.currentTimeMillis();
            long elapsedTime = stopTime - startTime;
    
            printActions(agentHC.getActions());
            printInstrumentation(agentHC.getInstrumentation());
            System.out.print(((AzaState) algHC.getGoalState()).printPlanning());
            System.out.println(((AzaState) algHC.getGoalState()).print());
            
            System.out.println("\nTime elapsed: " + elapsedTime + " milliseconds");

        } catch (Exception e) {
            e.printStackTrace();;
        }
    }

    private static void AzaSimulatedAnnealing(AzaState state, int operator, int heuristic) {
        try {

            Problem prob;
            if(operator == 0 && heuristic == 1)
                prob = new Problem(state, new AzaSwapMoveSuccessorSA(), new AzaGoalTest(), new AzaHeuristicFunction1());
            else if(operator == 0)
                prob = new Problem(state, new AzaSwapMoveSuccessorSA(), new AzaGoalTest(), new AzaHeuristicFunction2());
            else if (operator == 1 && heuristic == 1)
                prob = new Problem(state, new AzaMoveSuccessorSA(), new AzaGoalTest(), new AzaHeuristicFunction1());
            else if (operator == 1)
                prob = new Problem(state, new AzaMoveSuccessorSA(), new AzaGoalTest(), new AzaHeuristicFunction2());
            else if(operator == 2 && heuristic == 1)
                prob = new Problem(state, new AzaMoveSwapSuccessorSA(), new AzaGoalTest(), new AzaHeuristicFunction1());
            else if(operator == 2)
                prob = new Problem(state, new AzaMoveSwapSuccessorSA(), new AzaGoalTest(), new AzaHeuristicFunction2());
            else if (heuristic == 1)
                prob = new Problem(state, new AzaSwapSuccessorSA(), new AzaGoalTest(), new AzaHeuristicFunction1());
            else
                prob = new Problem(state, new AzaSwapSuccessorSA(), new AzaGoalTest(), new AzaHeuristicFunction2());
            // Instantiate the search Algorithm
            Search algSA = new SimulatedAnnealingSearch(10000,100,5,0.001);
            // Instantiate the search agent
            SearchAgent agentSA = new SearchAgent(prob, algSA);

            long stopTime = System.currentTimeMillis();
            long elapsedTime = stopTime - startTime;

            printInstrumentation(agentSA.getInstrumentation());
            System.out.print(((AzaState) algSA.getGoalState()).printPlanning());
            System.out.println(((AzaState) algSA.getGoalState()).print());
            
            System.out.println("\nTime elapsed: " + elapsedTime + " milliseconds");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void printInstrumentation(Properties properties) {
        Iterator keys = properties.keySet().iterator();
        while (keys.hasNext()) {
            String key = (String) keys.next();
            String property = properties.getProperty(key);
            System.out.println(key + " : " + property);
        }
    }

    private static void printActions(List actions) {
        for (int i = 0; i < actions.size(); i++) {
            String action = (String) actions.get(i);
            System.out.println(action);
        }
    }

    public static void printUsage() {
        System.out.println("Arguments: [search] [npaq] [seed] [proportion] [initialSol] [operator] [heuristic]\n");
        System.out.print("[search]: Hill Climbing / Simulated Annealing\n[npaq]: Number of packages\n");
        System.out.print("[seed]: The random seed used for generating numbers; seed = -1 -> random seed\n[proportion]: The proportion of transportable weight\n");
        System.out.print("[initialSol]: The initial solution generation strategy; initialSol = 1 -> initialSolution1, otherwise initialSolution2\n");
        System.out.print("[operator]: The operator; operator = 0 -> swapmove, operator = 1 -> move, operator = 2 -> moveswap, otherwise swap\n");
        System.out.print("[heuristic]: The heuristic function; heuristic = 1 -> heursiticFunction1, otherwise heuristicFunction2\n\n");
        System.out.print("EXAMPLE:\njava main Hill Climbing 100 5 1.25 1 1 2\n");
    }
}
