package ui;

import javax.swing.*;
import java.awt.*;

public class FlowPanel extends JPanel {

    private CQRSStage activeStage = CQRSStage.NONE;

    public void setActiveStage(CQRSStage stage) {
        this.activeStage = stage;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {

        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;

        g2.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
        );

        drawInput(g2);

        drawCommandFlow(g2);

        drawQueryFlow(g2);

        drawRepository(g2);
    }

    private void drawInput(Graphics2D g2) {

        drawBox(
                g2,
                "INPUT",
                220,
                20,
                Color.GRAY,
                activeStage == CQRSStage.INPUT
        );
    }

    private void drawCommandFlow(Graphics2D g2) {

        drawArrow(g2,295,70,160,120);

        drawBox(
                g2,
                "COMMAND",
                80,
                120,
                new Color(52,152,219),
                activeStage == CQRSStage.COMMAND
        );

        drawArrow(g2,160,170,160,220);

        drawBox(
                g2,
                "COMMAND HANDLER",
                80,
                220,
                new Color(41,128,185),
                activeStage == CQRSStage.COMMAND_HANDLER
        );

        drawArrow(g2,160,270,250,340);
    }

    private void drawQueryFlow(Graphics2D g2) {

        drawArrow(g2,295,70,430,120);

        drawBox(
                g2,
                "QUERY",
                350,
                120,
                new Color(243,156,18),
                activeStage == CQRSStage.QUERY
        );

        drawArrow(g2,430,170,430,220);

        drawBox(
                g2,
                "QUERY HANDLER",
                350,
                220,
                new Color(230,126,34),
                activeStage == CQRSStage.QUERY_HANDLER
        );

        drawArrow(g2,430,270,340,340);
    }

    private void drawRepository(Graphics2D g2) {

        boolean active =
                activeStage == CQRSStage.REPOSITORY;

        if(active) {
            g2.setColor(new Color(46,204,113));
        }
        else {
            g2.setColor(new Color(149,165,166));
        }

        int x = 220;
        int y = 340;

        g2.fillOval(x,y,120,30);
        g2.fillRect(x,y+15,120,60);
        g2.fillOval(x,y+60,120,30);

        g2.setColor(Color.BLACK);

        g2.drawString(
                "REPOSITORY",
                x + 18,
                y + 50
        );
    }

    private void drawBox(
            Graphics2D g2,
            String text,
            int x,
            int y,
            Color color,
            boolean active) {

        if(active) {
            g2.setColor(color.brighter());
            g2.fillRoundRect(
                    x,
                    y,
                    170,
                    50,
                    20,
                    20
            );

            g2.setStroke(
                    new BasicStroke(3)
            );
        }
        else {

            g2.setColor(color);

            g2.fillRoundRect(
                    x,
                    y,
                    170,
                    50,
                    20,
                    20
            );

            g2.setStroke(
                    new BasicStroke(1)
            );
        }

        g2.setColor(Color.BLACK);

        g2.drawRoundRect(
                x,
                y,
                170,
                50,
                20,
                20
        );

        g2.drawString(
                text,
                x + 25,
                y + 28
        );
    }

    private void drawArrow(
            Graphics2D g2,
            int x1,
            int y1,
            int x2,
            int y2) {

        g2.drawLine(
                x1,
                y1,
                x2,
                y2
        );
    }
}