package ui;

import algorithm.DPSolver;
import algorithm.GreedySolver;
import model.Department;
import util.CSVReader;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.List;

public class MainFrame extends JFrame {

    private JTextArea greedyArea;
    private JTextArea dpArea;
    private ValueCostBarChart valueCostChart;
    private TimeBarChart timeChart;
    private PieChartPanel budgetPie;
    private PieChartPanel valuePie;
    private PieChartPanel timePie;

    private List<Department> lastGreedy;
    private List<Department> lastDP;

    public MainFrame() {

        setTitle("Stock Optimization");
        setSize(1100, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        Font titleFont = new Font("Segoe UI", Font.BOLD, 16);
        Font textFont = new Font("Segoe UI", Font.PLAIN, 18);

        // ================= TOP PANEL =================
        JPanel top = new JPanel();
        top.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel budgetLabel = new JLabel("Bütçe %:");
        budgetLabel.setFont(titleFont);

        Integer[] percents = {10, 20, 30, 40, 50};
        JComboBox<Integer> budgetBox = new JComboBox<>(percents);
        budgetBox.setFont(textFont);

        JButton runButton = new JButton("Algoritmaları Çalıştır");
        runButton.setFont(textFont);

        JButton greedyDetails = new JButton("Greedy ile Seçilen Ürünler");
        JButton dpDetails = new JButton("DP ile Seçilen Ürünler");

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

        greedyArea.setMargin(new Insets(10, 10, 10, 10));
        dpArea.setMargin(new Insets(10, 10, 10, 10));

        greedyArea.setEditable(false);
        dpArea.setEditable(false);

        greedyArea.setBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(Color.GRAY),
                        "Greedy için Sonuçlar",
                        TitledBorder.LEFT,
                        TitledBorder.TOP,
                        new Font("Segoe UI", Font.BOLD, 16)
                )
        );

        dpArea.setBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(Color.GRAY),
                        "DP için Sonuçlar",
                        TitledBorder.LEFT,
                        TitledBorder.TOP,
                        new Font("Segoe UI", Font.BOLD, 16)
                )
        );

        JScrollPane greedyScroll = new JScrollPane(greedyArea);
        JScrollPane dpScroll = new JScrollPane(dpArea);

        JSplitPane centerSplit =
                new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, greedyScroll, dpScroll);
        centerSplit.setDividerLocation(550);

        add(centerSplit, BorderLayout.CENTER);

        // ================= BOTTOM (CHART) =================
        valueCostChart = new ValueCostBarChart();
        timeChart = new TimeBarChart();

        budgetPie = new PieChartPanel();
        valuePie = new PieChartPanel();
        timePie = new PieChartPanel();

        JPanel bottomPanel = new JPanel(new GridLayout(2, 1));

        // ÜST: BAR CHARTS
        JPanel barPanel = new JPanel(new GridLayout(1, 2));
        barPanel.add(valueCostChart);
        barPanel.add(timeChart);

        // ALT: PIE CHARTS
        JPanel piePanel = new JPanel(new GridLayout(1, 3));
        piePanel.add(budgetPie);
        piePanel.add(valuePie);
        piePanel.add(timePie);

        bottomPanel.add(barPanel);
        bottomPanel.add(piePanel);

        bottomPanel.setPreferredSize(new Dimension(1100, 320));
        add(bottomPanel, BorderLayout.SOUTH);

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
                    "Seçilen Ürünler: " + greedy.size() + "\n" +
                            "Bütçe Limiti: " + budget + "\n" +
                            "Kullanılan Bütçe: " + gRes.cost + "\n" +
                            "Toplam Fayda: " + gRes.value + "\n" +
                            "Yürütme Süresi: " + String.format("%.3f", gTime) + " ms"
            );

            dpArea.setText(
                    "Seçilen Ürünler: " + dp.size() + "\n" +
                            "Bütçe Limiti: " + budget + "\n" +
                            "Kullanılan Bütçe: " + dRes.cost + "\n" +
                            "Toplam Fayda: " + dRes.value + "\n" +
                            "Yürütme Süresi: " + String.format("%.3f", dTime) + " ms"
            );

            // ===== EN İYİ SONUCU VURGULA =====
            if (gRes.value > dRes.value) {
                greedyArea.setBackground(new Color(220, 255, 220));
                dpArea.setBackground(Color.WHITE);
            } else if (dRes.value > gRes.value) {
                dpArea.setBackground(new Color(220, 255, 220));
                greedyArea.setBackground(Color.WHITE);
            } else {
                greedyArea.setBackground(Color.WHITE);
                dpArea.setBackground(Color.WHITE);
            }

            valueCostChart.setValues(
                    gRes.value, dRes.value,
                    gRes.cost, dRes.cost
            );

            timeChart.setTimes(gTime, dTime);
            budgetPie.setValues(
                    gRes.cost,
                    dRes.cost,
                    "Kullanılan Bütçe Dağılımı (%)"
            );

            valuePie.setValues(
                    gRes.value,
                    dRes.value,
                    "Toplam Fayda Dağılımı (%)"
            );

            timePie.setValues(
                    gTime,
                    dTime,
                    "Yürütme Süresi Dağılımı (%)"
            );
        });

        greedyDetails.addActionListener(e -> {
            if (lastGreedy != null)
                new DepartmentTableDialog(this,
                        "Greedy ile Seçilen Ürünler", lastGreedy).setVisible(true);
        });

        dpDetails.addActionListener(e -> {
            if (lastDP != null)
                new DepartmentTableDialog(this,
                        "DP ile Seçilen Ürünler", lastDP).setVisible(true);
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
