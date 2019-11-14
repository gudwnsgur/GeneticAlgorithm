package GeneticAlgorithm;

import java.util.ArrayList;
import java.util.Random;

public class EachSolution {
    double fitness;
    int weight;
    int chromosomeLen;
    public ArrayList<Integer> chromosome;
    TestData testData = new TestData();


    public EachSolution() {   // 생성자- NumOfItem 개의 0/1 비트값 생성.
        chromosomeLen = testData.getNumOfItem();
        chromosome = new ArrayList<>(chromosomeLen);

        weight = 0;
        chromosome.clear();
        for (int i = 0; i < chromosomeLen; i++) {
            Random rn = new Random();
            chromosome.add(i, rn.nextInt(2));

            weight += chromosome.get(i) * testData.w.get(i);
        }
        compFitness();
    }


    void compFitness() {
        int weight=0;
        int value=0;

        fitness = 0;
        double realFit = 0; // 소수점을 버리지 않은 Fitness

        for (int i = 0; i < chromosomeLen; i++) {
            value += chromosome.get(i) * testData.v.get(i);
            weight += chromosome.get(i) * testData.w.get(i);
        }
        if(weight > testData.getMaxWeight()) {
            fitness = 0;
        }
        else {

            realFit = (double) value / (double) (testData.getMaxWeight());
            fitness = Math.round(realFit * 1000) / 1000.0;
        }
    }
    /* 각각의 chromosome에 대한 fitness 계산
       MaxWeight을 넘으면 적응도 = 0.1
       넘지 않으면 해당 chromosome의 value/maxWeight = fitness
    * */

    public double getFitness() {
        return fitness;
    }   // 해당 solution 의 fitness 출력
}
