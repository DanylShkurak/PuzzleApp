package entity;

import autofold.Algorithm;
import game.Game;
import graphics.Compare;
import graphics.Rect;
import mouse.Mouse;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

public class Puzzle extends Canvas implements Runnable {
    public static JFrame frame;
    public static int N_N_MATRIX = 4;
    public static int RECT_SIZE = 80;
    public static int WIDTH = N_N_MATRIX * RECT_SIZE;
    public static int HEIGHT = N_N_MATRIX * RECT_SIZE;
    public static float scale = 2f;
    private final Game game;
    private final Algorithm algorithm;
    private Thread thread;
    private final Mouse mouse;
    private static int switcher = 1;
    private boolean isRunning = false;
    public static boolean shufflePressed = true;
    public static boolean assemblePressed = false;
    private static final BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
    public static int[] pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();


    public static void main(String[] args) {
        start();
    }


    public Puzzle() {
        setPreferredSize(new Dimension((int) (WIDTH * scale), (int) (HEIGHT * scale)));
        game = new Game();
        frame = new JFrame();
        mouse = new Mouse();
        algorithm = new Algorithm();
        addMouseListener(mouse);
        addMouseMotionListener(mouse);
        JButton shuffleButton = new JButton("Shuffle");
        JButton endButton = new JButton("End Game");
        JButton completeButton = new JButton("Complete Puzzle");
        shuffleButton.addActionListener(e -> {
            switcher = 1;
            Game.shuffle(Game.rects);
            if (Compare.autoCheck(Game.rects)) {
                JOptionPane.showMessageDialog(frame, "You won!", "Congratulations", JOptionPane.INFORMATION_MESSAGE);
                System.exit(0);
            }
            shufflePressed = true;
            assemblePressed = false;
        });


        completeButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(frame, "Please wait...", "Message", JOptionPane.INFORMATION_MESSAGE);
            if (switcher == 1) {
                if (Algorithm.assemble(Algorithm.rects)) {
                    switcher = 0;
                    assemblePressed = true;
                    shufflePressed = false;
                }
                JOptionPane.showMessageDialog(frame, "Good job :)", "Message", JOptionPane.INFORMATION_MESSAGE);
                System.exit(0);
            }
        });
        endButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(frame, "Game end!", "Message", JOptionPane.INFORMATION_MESSAGE);
            System.exit(0);
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(shuffleButton);
        buttonPanel.add(completeButton);
        buttonPanel.add(endButton);
        frame.getContentPane().add(buttonPanel, BorderLayout.SOUTH);

    }

    @Override
    public void run() {
        while (isRunning) {
            update();
            if (shufflePressed) {
                render(Game.rects);
            }
            if (assemblePressed) {
                render(Algorithm.rects);
            }

        }
        stop();
    }


    public void threadStart() {
        isRunning = true;
        thread = new Thread(this, "loop");
        thread.start();
    }

    public void stop() {
    }


    public void update() {
        game.update(Game.rects);
        mouse.update();
    }


    public void render(Rect[] rects) {
        BufferStrategy strategy = getBufferStrategy();
        if (strategy == null) {
            createBufferStrategy(3);
            return;
        }
        game.render(rects);
        Graphics2D graphic = (Graphics2D) strategy.getDrawGraphics();
        graphic.drawImage(image, 0, 0, (int) (WIDTH * scale), (int) (HEIGHT * scale), null);
        graphic.dispose();
        strategy.show();
    }


    public static void start() {
        Puzzle puzzle = new Puzzle();
        frame.setResizable(false);
        frame.setTitle("Puzzle");
        frame.add(puzzle);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
        frame.setAlwaysOnTop(true);
        puzzle.threadStart();

    }
}
