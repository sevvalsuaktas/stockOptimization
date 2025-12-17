package ui;

import algorithm.DPSolver;
import algorithm.GreedySolver;
import model.Department;
import util.CSVReader;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class MainFrame extends JFrame {

    private JTextArea greedyArea;
    private JTextArea dpArea;
    private ValueCostBarChart valueCostChart;
    private TimeBarChart timeChart;
    private PieChartPanel pieChart;

    private List<Department> lastGreedy;
    private List<Department> lastDP;

    public MainFrame() {

        setTitle("Walmart Stock Optimization");
        setSize(1100, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        Font titleFont = new Font("Segoe UI", Font.BOLD, 16);
        Font textFont = new Font("Segoe UI", Font.PLAIN, 14);

        // ================= TOP PANEL =================
        JPanel top = new JPanel();
        top.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel budgetLabel = new JLabel("Budget %:");
        budgetLabel.setFont(titleFont);

        Integer[] percents = {10, 20, 30, 40, 50};
        JComboBox<Integer> budgetBox = new JComboBox<>(percents);
        budgetBox.setFont(textFont);

        JButton runButton = new JButton("Run Algorithms");
        runButton.setFont(textFont);

        JButton greedyDetails = new JButton("Greedy Departments");
        JButton dpDetails = new JButton("DP Departments");

        top.add(budgetLabel);
        top.add(budgetBox);
        top.add(runButton);
        top.add(greedyDetails);
        top.add(dpDetails);

        add(top, BorderLayout.NORTH);

        // ================= CENTER PANEL =================
        greedyArea = new JTextArea();
        dpArea = new JTextArea();

        greedyArea.setFont(textFont);
        dpArea.setFont(textFont);

        greedyArea.setEditable(false);
        dpArea.setEditable(false);

        greedyArea.setBorder(BorderFactory.createTitledBorder("Greedy Result"));
        dpArea.setBorder(BorderFactory.createTitledBorder("DP Result"));

        JScrollPane greedyScroll = new JScrollPane(greedyArea);
        JScrollPane dpScroll = new JScrollPane(dpArea);

        JSplitPane centerSplit =
                new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, greedyScroll, dpScroll);
        centerSplit.setDividerLocation(550);

        add(centerSplit, BorderLayout.CENTER);

        // ================= BOTTOM (CHART) =================
        JPanel bottomPanel = new JPanel(new GridLayout(2, 1));

        valueCostChart = new ValueCostBarChart();
        timeChart = new TimeBarChart();
        pieChart = new PieChartPanel();

        bottomPanel.add(valueCostChart);
        bottomPanel.add(timeChart);
        bottomPanel.add(pieChart);

        bottomPanel.setPreferredSize(new Dimension(1100, 300));
        add(bottomPanel, BorderLayout.SOUTH);

//        valueCostChart = new ValueCostBarChart();
//        timeChart = new TimeBarChart();
//        pieChart = new BudgetPieChart();
//
//        valueCostChart.setPreferredSize(new Dimension(1100, 150));
//
//        JPanel lowerCharts = new JPanel(new GridLayout(1, 2));
//        lowerCharts.add(timeChart);
//        lowerCharts.add(pieChart);
//
//        bottomPanel.add(valueCostChart);
//        bottomPanel.add(lowerCharts);
//
//        add(bottomPanel, BorderLayout.SOUTH);

        // ================= ACTIONS =================
        runButton.addActionListener(e -> {

            List<Department> departments =
                    CSVReader.loadDepartments("data/cleanedWalmartTopStore.csv");

            int totalCost = departments.stream().mapToInt(d -> d.cost).sum();
            int percent = (int) budgetBox.getSelectedItem();
            int budget = totalCost * percent / 100;

            // -------- GREEDY --------
            long gStart = System.nanoTime();
            List<Department> greedy =
                    GreedySolver.solve(departments, budget);
            long gEnd = System.nanoTime();

            // -------- DP --------
            long dStart = System.nanoTime();
            List<Department> dp =
                    DPSolver.solve(departments, budget);
            long dEnd = System.nanoTime();

            lastGreedy = greedy;
            lastDP = dp;

            Result gRes = calculate(greedy);
            Result dRes = calculate(dp);

            double gTime = (gEnd - gStart) / 1_000_000.0;
            double dTime = (dEnd - dStart) / 1_000_000.0;

            greedyArea.setText(
                    "Selected Departments: " + greedy.size() + "\n" +
                            "Used Budget: " + gRes.cost + "\n" +
                            "Total Value: " + gRes.value + "\n" +
                            "Execution Time: " + String.format("%.3f", gTime) + " ms"
            );

            dpArea.setText(
                    "Selected Departments: " + dp.size() + "\n" +
                            "Used Budget: " + dRes.cost + "\n" +
                            "Total Value: " + dRes.value + "\n" +
                            "Execution Time: " + String.format("%.3f", dTime) + " ms"
            );

            valueCostChart.setValues(
                    gRes.value, dRes.value,
                    gRes.cost, dRes.cost
            );

            timeChart.setTimes(gTime, dTime);
            pieChart.setValues(gRes.cost, dRes.cost); // budget için
            pieChart.setValues(gRes.value, dRes.value); // value için
        });

        greedyDetails.addActionListener(e -> {
            if (lastGreedy != null)
                new DepartmentTableDialog(this,
                        "Greedy Departments", lastGreedy).setVisible(true);
        });

        dpDetails.addActionListener(e -> {
            if (lastDP != null)
                new DepartmentTableDialog(this,
                        "DP Departments", lastDP).setVisible(true);
        });
    }

    // ================= HELPER =================
    private Result calculate(List<Department> list) {
        int cost = 0, value = 0;
        for (Department d : list) {
            cost += d.cost;
            value += d.value;
        }
        return new Result(cost, value);
    }

    private static class Result {
        int cost, value;
        Result(int c, int v) {
            cost = c;
            value = v;
        }
    }
}
