package util;

import model.Department;

import java.io.*;
import java.util.*;

public class CSVReader {

    public static List<Department> loadDepartments(String path) {
        Map<Integer, List<Double>> salesMap = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line = br.readLine(); // header

            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");

                int dept = Integer.parseInt(parts[0]);
                double weeklySales = Double.parseDouble(parts[2]);

                salesMap.putIfAbsent(dept, new ArrayList<>());
                salesMap.get(dept).add(weeklySales);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        List<Department> departments = new ArrayList<>();

        for (int deptId : salesMap.keySet()) {
            double avgSales = salesMap.get(deptId)
                    .stream().mapToDouble(Double::doubleValue).average().orElse(0);

            int value = (int) avgSales;
            int cost = (int) (avgSales * 0.05); // modelleme varsayımı

            departments.add(new Department(deptId, cost, value));
        }

        return departments;
    }
}

