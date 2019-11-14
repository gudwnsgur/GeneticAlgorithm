package GeneticAlgorithm;

import java.util.Arrays;
import java.util.ArrayList;

public class TestData {
    private int maxWeight;
    private int numOfItem;
    public ArrayList<Integer> w;
    public ArrayList<Integer> v;

    public TestData() {
        maxWeight = 25;
        numOfItem = 7;
        w = new ArrayList<>(Arrays.asList(1, 2, 4, 6, 5, 7, 8));
        v = new ArrayList<>(Arrays.asList(5, 3, 2, 6, 8, 7, 9));

    }

    public int getMaxWeight() {
        return maxWeight;
    }

    public int getNumOfItem() {
        return numOfItem;
    }

    public void printInputData() {

        System.out.println("Total Weight Limit : " + maxWeight);
        System.out.println("Number of Item : " + numOfItem);

        System.out.print("Weight of each item : ");
        for (int i = 0; i < numOfItem; i++)
            System.out.print(w.get(i)+ " ");
        System.out.println();

        System.out.print("Value of each item : ");
        for (int i = 0; i < numOfItem; i++) {
            System.out.print(v.get(i)+ " ");
        }
        System.out.println("\n");
    }
}