package algorithm;

import model.Department;

import java.util.*;

public class GreedySolver {

    public static List<Department> solve(List<Department> depts, int budget) {

        depts.sort((a, b) -> Double.compare(b.getRatio(), a.getRatio()));

        List<Department> selected = new ArrayList<>();
        int usedBudget = 0;

        for (Department d : depts) {
            if (usedBudget + d.cost <= budget) {
                selected.add(d);
                usedBudget += d.cost;
            }
        }

        return selected;
    }
}

