package game;

import entity.Puzzle;
import graphics.Compare;
import graphics.Rect;
import graphics.Sprite;
import graphics.Renderer;
import mouse.Mouse;

import javax.swing.*;
import java.util.Random;

import static entity.Puzzle.*;
import static graphics.Compare.autoCheck;
import static graphics.Sprite.createSprites;
import static java.awt.event.MouseEvent.*;

public class Game {

    public static Rect[] rects = new Rect[N_N_MATRIX * N_N_MATRIX];
    Compare compare = new Compare();
    public static Random random = new Random();


    public static Rect[] fillRects(Rect[] rects) {
        int index = 0;
        for (int i = 0; i < N_N_MATRIX; i++) {
            for (int j = 0; j < N_N_MATRIX; j++) {
                rects[index] = new Rect(j * RECT_SIZE, i * RECT_SIZE, Sprite.picture[index], index);
                index++;
            }
        }

        return rects;
    }


    public Game() {
        createSprites("/pictures/pepe.jpg");
        fillRects(rects);
        init();

    }

    public static void init() {
        shuffle(rects);
    }

    public static void shuffle(Rect[] rects) {
        for (int i = 0; i < rects.length; i++) {
            int swap = random.nextInt(rects.length - 1);
            Rect rect = new Rect();
            rect.swapRectangles(i, swap, rects);
            rects[i].rotate(BUTTON2);
        }

    }


    public void update(Rect[] rects) {
        for (int i = 0; i < rects.length; i++) {
            rects[i].update(rects);
        }


        if (Mouse.buttonUp(BUTTON1)) {
            autoCheck(rects);
            if (autoCheck(rects)) {
                JOptionPane.showMessageDialog(frame, "You won!", "Congratulations", JOptionPane.INFORMATION_MESSAGE);
                System.exit(0);
            }
        }

        if (Mouse.buttonDown(BUTTON3)) {
            for (int i = 0; i < rects.length; i++) {
                rects[i].rotate(BUTTON3);
                autoCheck(rects);
            }
        }
        if (Mouse.buttonUp(BUTTON3)) {
            if (autoCheck(rects)) {
                JOptionPane.showMessageDialog(frame, "You won!", "Congratulations", JOptionPane.INFORMATION_MESSAGE);
                System.exit(0);
            }

        }

        if (Mouse.buttonUp(BUTTON2)) {
            shuffle(rects);
            autoCheck(rects);
            if (autoCheck(rects)) {
                JOptionPane.showMessageDialog(frame, "You won!", "Congratulations", JOptionPane.INFORMATION_MESSAGE);
                System.exit(0);
            }
        }


    }


    public void render(Rect[] rects) {
        Renderer.renderBackground();
        for (int i = 0; i < rects.length; i++) {
            rects[i].render();

        }

        for (int i = 0; i < Puzzle.pixels.length; i++) {
            Puzzle.pixels[i] = Renderer.pixels[i];

        }
    }

}