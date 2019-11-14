package GeneticAlgorithm;

public class Population {
    private final int numOfGenes = 10;
    EachSolution[] solutions = new EachSolution[numOfGenes];
    // 한 Population 에 10 개의 Chromosome
    double mostFit;
    int mostIndex = 0;
    TestData testData = new TestData();

    void initPop() {
        for (int i = 0; i < solutions.length; i++)
            solutions[i] = new EachSolution();
    }

    EachSolution getMostFit() {
        mostFit = 0;
        for (int i = 0; i < solutions.length; i++) {
            if (mostFit < solutions[i].fitness) {
                mostFit = solutions[i].fitness;
                mostIndex = i;
            }
        }
        return solutions[mostIndex];
    } // fitness 가 가장 높은 index ( mostIndex ) 를 찾아야 함

    EachSolution getSecMostFit() {

        int secondIndex = 0;
        double secondFit = 0;

        for (int i = 0; i < solutions.length; i++) {
            if (i != mostIndex && secondFit < solutions[i].fitness) {
                secondFit = solutions[i].fitness;
                secondIndex = i;
            }
        }
        return solutions[secondIndex];
    } // fitness 가 두 번째인 index( secondIndex )를 찾아야 함.
}
