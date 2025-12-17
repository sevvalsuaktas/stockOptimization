package ui;

import javax.swing.*;
import java.awt.*;

public class PieChartPanel extends JPanel {

    private double greedyValue;
    private double dpValue;

    public void setValues(double g, double d) {
        greedyValue = g;
        dpValue = d;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (greedyValue == 0 && dpValue == 0) return;

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        int size = Math.min(getWidth(), getHeight()) - 40;
        int x = (getWidth() - size) / 2;
        int y = (getHeight() - size) / 2;

        double total = greedyValue + dpValue;

        double greedyAngle = greedyValue / total * 360;
        double dpAngle = dpValue / total * 360;

        // ===== PIE =====
        g2.setColor(new Color(70, 130, 180));
        g2.fillArc(x, y, size, size, 0, (int) greedyAngle);

        g2.setColor(new Color(220, 20, 60));
        g2.fillArc(x, y, size, size, (int) greedyAngle, (int) dpAngle);

        // ===== LABELS =====
        drawLabel(g2,
                "Greedy",
                greedyValue,
                greedyValue / total * 100,
                0 + greedyAngle / 2,
                x, y, size);

        drawLabel(g2,
                "DP",
                dpValue,
                dpValue / total * 100,
                greedyAngle + dpAngle / 2,
                x, y, size);

        // ===== LEGEND =====
        drawLegend(g2);
    }

    private void drawLabel(Graphics2D g2,
                           String name,
                           double value,
                           double percent,
                           double angle,
                           int x, int y, int size) {

        double rad = Math.toRadians(angle);
        int cx = x + size / 2;
        int cy = y + size / 2;

        int tx = (int) (cx + Math.cos(rad) * size * 0.35);
        int ty = (int) (cy - Math.sin(rad) * size * 0.35);

        g2.setColor(Color.BLACK);
        g2.setFont(new Font("Segoe UI", Font.BOLD, 12));

        g2.drawString(name, tx - 15, ty - 5);
        g2.drawString(String.format("%.0f", value), tx - 15, ty + 10);
        g2.drawString(String.format("(%.1f%%)", percent), tx - 15, ty + 25);
    }

    private void drawLegend(Graphics2D g2) {
        int x = 20;
        int y = 20;

        g2.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        g2.setColor(new Color(70, 130, 180));
        g2.fillRect(x, y, 15, 15);
        g2.setColor(Color.BLACK);
        g2.drawString("Greedy", x + 20, y + 12);

        g2.setColor(new Color(220, 20, 60));
        g2.fillRect(x, y + 20, 15, 15);
        g2.setColor(Color.BLACK);
        g2.drawString("DP", x + 20, y + 32);
    }
}