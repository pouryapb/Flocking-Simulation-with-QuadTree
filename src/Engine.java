import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public abstract class Engine extends JFrame {
    private static final long serialVersionUID = -7044496989599732077L;
    private static final int WIDTH = 800;
    private static final int HEIGHT = WIDTH / 4 * 3;
    private boolean shouldLoop = true;

    private class DrawCanvas extends JPanel {
        private static final long serialVersionUID = 8193212504627188925L;

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);

            ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            setBackground(Color.decode("#212121"));

            if (shouldLoop) {
                loop(g);
                repaint();
            } else {
                loop(g);
            }
        }
    }

    private void init() {

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException
                | UnsupportedLookAndFeelException e1) {
            e1.printStackTrace();
        }

        var canvas = new DrawCanvas();
        setContentPane(canvas);

        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setFocusable(true);
        requestFocusInWindow();
        setup();
        pack();
        setVisible(true);
        setLocationRelativeTo(null);
    }

    protected void start() {
        SwingUtilities.invokeLater(this::init);
    }

    protected void noLoop() {
        shouldLoop = false;
    }

    protected void loop() {
        shouldLoop = true;
        repaint();
    }

    protected abstract void setup();

    protected abstract void loop(Graphics g);
}
