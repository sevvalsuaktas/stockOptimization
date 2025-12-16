package ui;

import javax.swing.*;
import java.awt.*;

public class ValueCostBarChart extends JPanel {

    private double gValue, dValue, gCost, dCost;

    public void setValues(double gv, double dv, double gc, double dc) {
        gValue = gv;
        dValue = dv;
        gCost = gc;
        dCost = dc;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        int h = getHeight() - 60;
        int baseY = getHeight() - 30;

        drawPair(g2, 100, h, gValue, dValue, "Total Value");
        drawPair(g2, 320, h, gCost, dCost, "Used Budget");
    }

    private void drawPair(Graphics2D g, int x, int h,
                          double gv, double dv, String label) {

        double max = Math.max(gv, dv);
        if (max == 0) return;

        int bw = 50;
        int gap = 25;

        int gh = (int) (gv / max * h);
        int dh = (int) (dv / max * h);

        g.setColor(new Color(70, 130, 180));
        g.fillRoundRect(x, h - gh + 30, bw, gh, 10, 10);

        g.setColor(new Color(220, 20, 60));
        g.fillRoundRect(x + bw + gap, h - dh + 30, bw, dh, 10, 10);

        g.setColor(Color.BLACK);
        g.drawString(String.format("%.2f", gv), x, h - gh + 20);
        g.drawString(String.format("%.2f", dv), x + bw + gap, h - dh + 20);

        g.drawString(label, x + 15, getHeight() - 10);
    }
}

