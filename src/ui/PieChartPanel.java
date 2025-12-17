package ui;

import javax.swing.*;
import java.awt.*;

public class PieChartPanel extends JPanel {

    private double greedyValue;
    private double dpValue;
    private String title = "";

    public void setValues(double g, double d, String t) {
        greedyValue = g;
        dpValue = d;
        title = t;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (greedyValue == 0 && dpValue == 0) return;

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        int size = Math.min(getWidth(), getHeight()) - 60;
        int x = (getWidth() - size) / 2;
        int y = 20;

        double total = greedyValue + dpValue;

        double greedyAngle = greedyValue / total * 360;
        double dpAngle = dpValue / total * 360;

        // ===== PIE =====
        g2.setColor(Color.BLUE);
        g2.fillArc(x, y, size, size, 0, (int) greedyAngle);

        g2.setColor(Color.RED);
        g2.fillArc(x, y, size, size, (int) greedyAngle, (int) dpAngle);

        // ===== LABELS =====
        drawLabel(g2, greedyValue / total * 100, greedyAngle / 2, x, y, size);

        drawLabel(g2, dpValue / total * 100, greedyAngle + dpAngle / 2, x, y, size);

        // ===== LEGEND =====
        drawLegend(g2);

        // ===== CHART TITLE =====
        g2.setFont(new Font("Segoe UI", Font.BOLD, 12));
        int w = g2.getFontMetrics().stringWidth(title);
        g2.setColor(Color.BLACK);
        g2.drawString(title, getWidth() / 2 - w / 2, getHeight() - 5);
    }

    private void drawLabel(Graphics2D g2,
                           double percent,
                           double angle,
                           int x, int y, int size) {

        double rad = Math.toRadians(angle);
        int cx = x + size / 2;
        int cy = y + size / 2;

        int tx = (int) (cx + Math.cos(rad) * size * 0.35);
        int ty = (int) (cy - Math.sin(rad) * size * 0.35);

        g2.setFont(new Font("Segoe UI", Font.BOLD, 12));
        String text = String.format("%.1f%%", percent);

        g2.setColor(Color.WHITE);
        g2.fillOval(tx - 20, ty - 15, 40, 22);

        g2.setColor(Color.BLACK);
        g2.drawString(
                text,
                tx - g2.getFontMetrics().stringWidth(text) / 2,
                ty
        );
    }

    private void drawLegend(Graphics2D g2) {
        int x = 20;
        int y = 20;

        g2.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        g2.setColor(Color.BLUE);
        g2.fillRect(x, y, 15, 15);
        g2.setColor(Color.BLACK);
        g2.drawString("Greedy", x + 20, y + 12);

        g2.setColor(Color.RED);
        g2.fillRect(x, y + 20, 15, 15);
        g2.setColor(Color.BLACK);
        g2.drawString("DP", x + 20, y + 32);
    }
}