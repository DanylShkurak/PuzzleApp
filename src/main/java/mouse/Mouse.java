package mouse;

import entity.Puzzle;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class Mouse implements MouseListener, MouseMotionListener {
    private static int x = -1;
    private static int y = -1;
    private static boolean[] buttons = new boolean[6];
    private static boolean[] lastButtons = new boolean[6];
    private static boolean dragging = false;

    public static int getX() {
        return x;
    }

    public static int getY() {
        return y;
    }

    public static boolean isDragging() {
        return dragging;
    }

    public void update() {
        for (int i = 0; i < buttons.length; i++) {
            lastButtons[i] = buttons[i];
        }
    }

    public static boolean button(int button) {
        return buttons[button];
    }

    public static boolean buttonDown(int button) {
        return buttons[button] && !lastButtons[button];
    }

    public static boolean buttonUp(int button) {
        return !buttons[button] && lastButtons[button];
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        buttons[e.getButton()] = true;
        dragging = true;
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        buttons[e.getButton()] = false;
        dragging = false;
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }



    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {
        x = (int) (e.getX() / Puzzle.scale);
        y = (int) (e.getY() / Puzzle.scale);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        x = (int) (e.getX() / Puzzle.scale);
        y = (int) (e.getY() / Puzzle.scale);
    }

}
