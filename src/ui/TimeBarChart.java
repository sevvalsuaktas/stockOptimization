package ui;

import javax.swing.*;
import java.awt.*;

public class TimeBarChart extends JPanel {

    private double gTime, dTime;

    public void setTimes(double g, double d) {
        gTime = g;
        dTime = d;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        int h = getHeight() - 50;
        double max = Math.max(gTime, dTime);

        int gh = (int) (gTime / max * h);
        int dh = (int) (dTime / max * h);

        g2.setColor(Color.BLUE);
        g2.fillRect(60, h - gh + 30, 40, gh);

        g2.setColor(Color.RED);
        g2.fillRect(140, h - dh + 30, 40, dh);

        g2.setColor(Color.BLACK);
        g2.drawString(String.format("Greedy: %.3f ms", gTime), 30, 20);
        g2.drawString(String.format("DP: %.3f ms", dTime), 30, 35);
    }
}

