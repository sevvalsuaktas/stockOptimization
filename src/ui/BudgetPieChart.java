package ui;

import javax.swing.*;
import java.awt.*;

public class BudgetPieChart extends JPanel {

    private double gCost, dCost;

    public void setCosts(double g, double d) {
        gCost = g;
        dCost = d;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        double total = gCost + dCost;
        if (total == 0) return;

        int size = Math.min(getWidth(), getHeight()) - 40;
        int x = 20;
        int y = 20;

        int gAngle = (int) (360 * gCost / total);

        g2.setColor(Color.BLUE);
        g2.fillArc(x, y, size, size, 0, gAngle);

        g2.setColor(Color.RED);
        g2.fillArc(x, y, size, size, gAngle, 360 - gAngle);

        g2.setColor(Color.BLACK);
        g2.drawString("Greedy", x + size + 10, y + 20);
        g2.drawString("DP", x + size + 10, y + 40);
    }
}

