package GeneticAlgorithm;

import java.util.*;

public class SimpleGA {
    private static int numOfGenes = 10; // 한 세대의 gene 개수
    private static TestData testData = new TestData();
    private double succeedCondFit; // 만족 조건이 되는 fitness

    Population pop = new Population();
    int generation = 0;
    public static void main(String[] args) {

        SimpleGA ga = new SimpleGA();
        ga.testData.printInputData();
        ga.pop.initPop();  // create initial population

        System.out.println("한 Population 당 개체 수 : " + numOfGenes);
        System.out.println("한 개체 당 gene 수 : " + ga.pop.solutions[0].chromosomeLen + "\n");
        System.out.println("초기 생성된 Chromosome. ");
        for (int i = 0; i < numOfGenes; i++) {
            System.out.print(ga.pop.solutions[i].chromosome + " ");
            System.out.println(" fitness : " + ga.pop.solutions[i].getFitness());
        }
        System.out.println();


        while (true) {
            if (ga.pop.mostFit >= ga.succeedCondFit) // 일단 100세대까지
                break;
            ++ga.generation;
            System.out.println("---------------------" + ga.generation + "세대 시작---------------------");

            Population childPop = new Population();

            int[] crossOverIndex = ga.rouletteSelection();  // 룰렛 선택
            ga.crossover(childPop, crossOverIndex); // 교배 후 N 개의 자식 생성
            ga.mutation(childPop);  // 돌연변이 생성
            ga.replace(childPop);   // 2N -> N 대체
            ga.printMostFit();  // 가장 높은 적응도의 개체 확인

            System.out.println("---------------------" + ga.generation + "세대 종료---------------------");
            if (ga.generation > 10)
                break;
        }   // 조건에 맞거나 100세대를 초과할 시 break
    }

    public SimpleGA() {
        succeedCondFit = 100;
    }   // 만족의 조건이 되는 최소 fitness 를 구한다.
    //

    int[] rouletteSelection() {
        System.out.println("현제 세대의 Population");
        for (int i = 0; i < numOfGenes; i++) {
            System.out.print(pop.solutions[i].chromosome + " ");
            System.out.println(" fitness : " + pop.solutions[i].getFitness());
        }

        System.out.println();
        Random rn = new Random();
        double totalFitness = 0;
        double[] fitRatio = new double[numOfGenes];
        int[] selectedIndex = new int[numOfGenes];

        for (int i = 0; i < pop.solutions.length; i++) {
            totalFitness += pop.solutions[i].getFitness();
            fitRatio[i] = 0;
        }
        for (int i = 0; i < pop.solutions.length; i++) {
            fitRatio[i] = pop.solutions[i].getFitness() / totalFitness;
        }

        double[][] Ratio = new double[pop.solutions.length][2];
        for (int i = 0; i < pop.solutions.length; i++) {
            Ratio[i][0] = fitRatio[i];
            Ratio[i][1] = i;
        }

        // int index 에 0,1 2,3 4,5 6,7 8,9, 교배하도록 9번 뽑아야 함
        // index에 그 교배할 solution의 index 넣어놓자

        for (int i = 0; i < numOfGenes; i++) {
            double randomNum = (double) rn.nextInt(11) / (double) 10;
            for (int j = 0; j < numOfGenes; j++) {
                randomNum -= Ratio[j][0];
                if (randomNum <= 0) {
                    selectedIndex[i] = j;
                    break;
                }
            }
        }
        System.out.println();
        System.out.println("- Roulette 을 통해 선택된 부모 개체의 index 순서 쌍 -");
        for (int i = 0; i < numOfGenes; i += 2)
            System.out.print("[ " + selectedIndex[i] + " , " + selectedIndex[i + 1] + " ] ");

       /*for(int i=0 ; i< numOfGenes ; i++) {
           System.out.print(selectedIndex[i] + " ");
       }*/
        System.out.println("\n");

        return selectedIndex;
    }

    void crossover(Population childPop, int[] crossOverIndex) { // 이중 교배
        childPop.initPop();
        Random rn = new Random();


        int mid1 = rn.nextInt(pop.solutions[0].chromosomeLen);   // 0~4 사이 의 mid1 정함
        int mid2;
        do {
            mid2 = rn.nextInt((pop.solutions[0].chromosomeLen));    // 0~4 사이의 mid2 정함
            if (mid1 > mid2) {
                int tmp = mid1;
                mid1 = mid2;
                mid2 = mid1;
            }
        } while (mid1 == mid2);


        System.out.println("[crossover point]  mid1 : " + mid1 + " , mid2 : " + mid2 + " ");

        for (int i = 0; i < numOfGenes; i += 2) { // childPop
            if (mid2 == 4) {
                for (int j = 0; j < pop.solutions[0].chromosomeLen; j++) {
                    if (j <= mid1) {
                        childPop.solutions[i].chromosome.set(j, pop.solutions[crossOverIndex[i]].chromosome.get(j));
                        childPop.solutions[i + 1].chromosome.set(j, pop.solutions[crossOverIndex[i + 1]].chromosome.get(j));
                    } else {
                        childPop.solutions[i].chromosome.set(j, pop.solutions[crossOverIndex[i + 1]].chromosome.get(j));
                        childPop.solutions[i + 1].chromosome.set(j, pop.solutions[crossOverIndex[i]].chromosome.get(j));
                    }
                }
            }   // crossover point = mid1
            else {
                for (int j = 0; j < pop.solutions[0].chromosomeLen; j++) {
                    if (j > mid1 && j <= mid2) {
                        childPop.solutions[i].chromosome.set(j, pop.solutions[crossOverIndex[i + 1]].chromosome.get(j));
                        childPop.solutions[i + 1].chromosome.set(j, pop.solutions[crossOverIndex[i]].chromosome.get(j));
                    } else {
                        childPop.solutions[i].chromosome.set(j, pop.solutions[crossOverIndex[i]].chromosome.get(j));
                        childPop.solutions[i + 1].chromosome.set(j, pop.solutions[crossOverIndex[i + 1]].chromosome.get(j));
                    }
                }
            }   // crossover point = mid1 & mid2
        }
        System.out.println("교배 이후 생성된 자식의 chromosome ");
        for (int i = 0; i < numOfGenes; i++) {
            System.out.print(childPop.solutions[i].chromosome + " ");
            System.out.println(" fitness : " + childPop.solutions[i].getFitness());
        }
        System.out.println();
        // 교배 이후 N 개의 자식 생성
    }

    void mutation(Population childPop) {
        // 낮은 확률로 돌연변이를 적용시켜야 하지만 데이터 셋이 적고 많은 세대를 거칠 예정이 아니므로
        // 각 개체에 대해 10% 확률로 돌연변이를 적용시키기로 결정
        // 돌연변이 개체는 0 <-> 1 gene 전체가 변경

        Random rn = new Random();
        int count = 0;
        for (int i = 0; i < numOfGenes; i++) {
            int ranNum1 = rn.nextInt(10);
            if (ranNum1 != 0) ;
            else {
                System.out.println("부모 개체의 " + i + "번 째 개체가 돌연변이로 선정");
                for (int j = 0; j < pop.solutions[0].chromosomeLen; j++) {
                    if (pop.solutions[i].chromosome.get(j) == 0)
                        pop.solutions[i].chromosome.set(j, 1);
                    else
                        pop.solutions[i].chromosome.set(j, 0);
                }
                count++;
                pop.solutions[i].getFitness();
            }

            int ranNum2 = rn.nextInt(10);
            if (ranNum2 != 0) ;
            else {
                System.out.println("자식 개체의 " + i + "번 째 개체가 돌연변이로 선정");
                for (int j = 0; j < childPop.solutions[0].chromosomeLen; j++) {
                    if (childPop.solutions[i].chromosome.get(j) == 0)
                        childPop.solutions[i].chromosome.set(j, 1);
                    else
                        childPop.solutions[i].chromosome.set(j, 0);
                }
                count++;
                childPop.solutions[i].getFitness();
            }
        }
        System.out.println("현재 세대 돌연변이 수 : " + count);
    }

    void replace(Population childPop) {
        Population nextPop = new Population();
        nextPop.initPop();

        System.out.println();
        for (int i = 0; i < numOfGenes; i++) {
            for (int j = i + 1; j < numOfGenes; j++) {
                if (pop.solutions[i].getFitness() < pop.solutions[j].getFitness()) {
                    EachSolution tmp = pop.solutions[i];
                    pop.solutions[i] = pop.solutions[j];
                    pop.solutions[j] = tmp;
                }
                if (childPop.solutions[i].getFitness() < childPop.solutions[j].getFitness()) {
                    EachSolution tmp = childPop.solutions[i];
                    childPop.solutions[i] = childPop.solutions[j];
                    childPop.solutions[j] = tmp;
                }
            }
        }
        for (int i = 0; i < numOfGenes; i++) {
            System.out.println(pop.solutions[i].getFitness() + " " + childPop.solutions[i].getFitness());
        }

        int a = 0;
        int b = 0;
        for (int i = 0; i < numOfGenes; i++) {
            if(pop.solutions[a].getFitness() >= childPop.solutions[b].getFitness()) {
                nextPop.solutions[i] = pop.solutions[a];
                a++;
            }
            else {
                nextPop.solutions[i] = childPop.solutions[b];
                b++;
            }
        }
        for (int i = 0; i < numOfGenes; i++) {
            pop.solutions[i] = nextPop.solutions[i];
        }
        System.out.println("교체된 Population");
        for (int i = 0; i < numOfGenes; i++) {
            System.out.print(pop.solutions[i].chromosome + " ");
            System.out.println(" fitness : " + pop.solutions[i].getFitness());
        }
        System.out.println();
    }

    void printMostFit() {
        System.out.println("현제 세대의 가장 적합한 Chromosome ");
        System.out.print(pop.solutions[0].chromosome );
        System.out.println(" fitness : " + pop.solutions[0].getFitness());
    }

}