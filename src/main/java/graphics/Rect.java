package graphics;

import entity.Puzzle;
import mouse.Mouse;

import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Comparator;

import static entity.Puzzle.N_N_MATRIX;
import static game.Game.rects;
import static graphics.Side.*;

public class Rect {
    public double x;
    public double y;
    private double nx;
    private double ny;
    private double ox;
    private double oy;
    public static int width;
    public static int height;
    public Sprite sprite;
    ;
    private boolean moving = false;
    public static int rotateAngle = 90;
    public int number;


    public Rect() {

    }


    public Rect(double x, double y, Sprite sprite, int number) {
        this.x = x;
        this.y = y;
        width = sprite.getWidth();
        height = sprite.getHeight();
        this.sprite = sprite;
        this.number = number;
    }

    private boolean checkIsValidMove() {
        nx = (int) (Math.round(x / Puzzle.RECT_SIZE) * Puzzle.RECT_SIZE);
        ny = (int) (Math.round(y / Puzzle.RECT_SIZE) * Puzzle.RECT_SIZE);
        int xi = (int) (Math.abs(nx - ox));
        int yi = (int) (Math.abs(ny - oy));
        if (nx < 0 || ny < 0 || nx >= Puzzle.WIDTH || ny >= Puzzle.HEIGHT || xi > Puzzle.RECT_SIZE || yi > Puzzle.RECT_SIZE)
            return false;
        return xi != Puzzle.RECT_SIZE || yi != Puzzle.RECT_SIZE;
    }

    public void update(Rect[] rects) {
        if (Mouse.buttonDown(MouseEvent.BUTTON1)) {
            if (Mouse.getX() > x && Mouse.getX() < x + width && Mouse.getY() > y && Mouse.getY() < y + height) {
                moving = true;
                ox = x;
                oy = y;
            }
        }
        if (moving && Mouse.isDragging()) {
            int mx = Mouse.getX();
            int my = Mouse.getY();
            x = mx - width / 2;
            y = my - height / 2;
        } else if (moving) {
            if (checkIsValidMove()) {
                Rect rectAtNewPos = getRectAtPosition(nx, ny);
                if (rectAtNewPos != null) {
                    int index1 = getIndexFromPosition(ox, oy);
                    int index2 = getIndexFromPosition(rectAtNewPos.x, rectAtNewPos.y);
                    swapRectangles(index1, index2, rects);
                } else {
                    x = ox;
                    y = oy;
                }
            } else {
                x = ox;
                y = oy;
            }

            moving = false;
        }
    }

    private int getIndexFromPosition(double x, double y) {
        int column = (int) (x / Rect.width);
        int row = (int) (y / Rect.height);
        return row * N_N_MATRIX + column;
    }

    public void swapRectangles(int index1, int index2, Rect[] rects) {
        Rect temp = rects[index1];
        rects[index1] = rects[index2];
        rects[index2] = temp;


        rects[index1].x = index1 % N_N_MATRIX * Rect.width;
        rects[index1].y = index1 / N_N_MATRIX * Rect.height;
        rects[index2].x = index2 % N_N_MATRIX * Rect.width;
        rects[index2].y = index2 / N_N_MATRIX * Rect.height;
    }

    private Rect getRectAtPosition(double x, double y) {
        for (Rect rect : rects) {
            if (rect != this && rect.x == x && rect.y == y) {
                return rect;
            }
        }
        return null;
    }

    public void render() {
        Renderer.renderSprite(sprite, (int) x, (int) y);
    }

    public void rotate(int button) {
        if (button == MouseEvent.BUTTON3) {
            if (Mouse.getX() > x && Mouse.getX() < x + width && Mouse.getY() > y && Mouse.getY() < y + height) {
                sprite.rotate(rotateAngle);

            }
        } else if (button == MouseEvent.BUTTON2) {
            sprite.rotate(0);
        }
    }


    public boolean compareRightBorder(int j) {
        ArrayList<Integer> integers = new ArrayList<>();
        int first = compareWithOneSide(rects[j], rects[j + 1], RIGHT);
        for (int k = 0; k < rects.length; k++) {
            if (k != j) {
                integers.add(compareWithAllSides(rects[j], rects[k], RIGHT));
            }
        }
        return integers.stream().allMatch(match -> first >= match);


    }


    public boolean compareLeftBorder(int j) {
        ArrayList<Integer> integers = new ArrayList<>();
        int first = compareWithOneSide(rects[j], rects[j - 1], LEFT);
        for (int k = 0; k < rects.length - N_N_MATRIX - 1; k++) {
            if (k != j) {
                integers.add(compareWithAllSides(rects[j], rects[k], LEFT));
            }
        }
        return integers.stream().allMatch(match -> first >= match);

    }

    public boolean compareBottomBorder(int j) {
        ArrayList<Integer> integers = new ArrayList<>();
        int first = compareWithOneSide(rects[j], rects[j + N_N_MATRIX], BOTTOM);
        for (int k = 0; k < rects.length; k++) {
            if (k != j) {
                integers.add(compareWithAllSides(rects[j], rects[k], BOTTOM));
            }
        }
        return integers.stream().allMatch(match -> first >= match);
    }

    public boolean compareTopBorder(int j) {
        ArrayList<Integer> integers = new ArrayList<>();
        int first = compareWithOneSide(rects[j], rects[j - N_N_MATRIX], TOP);
        for (int k = 0; k < rects.length; k++) {
            if (k != j) {
                integers.add(compareWithAllSides(rects[j], rects[k], TOP));
            }
        }

        return integers.stream().allMatch(value -> value <= first);

    }

    public Integer compareWithAllSides(Rect rect1, Rect rect2, Side side) {
        ArrayList<Integer> integers = new ArrayList<>();
        int[] pixelsA = rect1.sprite.getPixels();
        int[] pixelsB = rect2.sprite.getPixels();
        int rectSize = Puzzle.RECT_SIZE;
        if (side == RIGHT) {
            integers.add(Compare.rightRight(pixelsA, pixelsB, rectSize));
            integers.add(Compare.rightTop(pixelsA, pixelsB, rectSize));
            integers.add(Compare.rightLeft(pixelsA, pixelsB, rectSize));
            integers.add(Compare.rightBottom(pixelsA, pixelsB, rectSize));
        }
        if (side == BOTTOM) {
            integers.add(Compare.bottomBottom(pixelsA, pixelsB, rectSize));
            integers.add(Compare.bottomTop(pixelsA, pixelsB, rectSize));
            integers.add(Compare.bottomLeft(pixelsA, pixelsB, rectSize));
            integers.add(Compare.rightBottom(pixelsB, pixelsA, rectSize));
        }
        if (side == LEFT) {
            integers.add(Compare.leftLeft(pixelsA, pixelsB, rectSize));
            integers.add(Compare.rightLeft(pixelsB, pixelsA, rectSize));
            integers.add(Compare.bottomLeft(pixelsB, pixelsA, rectSize));
            integers.add(Compare.leftTop(pixelsA, pixelsB, rectSize));
        }
        if (side == TOP) {
            integers.add(Compare.rightTop(pixelsB, pixelsA, rectSize));
            integers.add(Compare.leftTop(pixelsB, pixelsA, rectSize));
            integers.add(Compare.bottomTop(pixelsB, pixelsA, rectSize));
            integers.add(Compare.topTop(pixelsA, pixelsB, rectSize));
        }
        return integers.stream()
                .max(Comparator.naturalOrder())
                .orElse(0);
    }

    public int compareWithOneSide(Rect rect1, Rect rect2, Side side) {
        int max = 0;
        int[] pixelsA = rect1.sprite.getPixels();
        int[] pixelsB = rect2.sprite.getPixels();
        int rectSize = Puzzle.RECT_SIZE;
        for (int y = 0; y < rectSize; y++) {
            if (side == LEFT) {
                int pixelA = pixelsA[y * rectSize]; // Left border pixel of this Rect
                int pixelB = pixelsB[(rectSize - 1) + y * rectSize];
                if (pixelA == pixelB) {
                    max++;
                }
            }
            if (side == RIGHT) {
                int pixelA = pixelsA[(rectSize - 1) + y * rectSize]; // Right border pixel of this Rect
                int pixelB = pixelsB[y * rectSize];
                if (pixelA == pixelB) {
                    max++;
                }
            }
        }
        for (int x = 0; x < rectSize; x++) {
            if (side == TOP) {
                int pixelA = pixelsA[x];
                int pixelB = pixelsB[(rectSize - 1) * rectSize + x];
                if (pixelA == pixelB) {
                    max++;
                }
            }
            if (side == BOTTOM) {
                int pixelA = pixelsA[(rectSize - 1) * rectSize + x]; // Bottom border pixel of this Rect
                int pixelB = pixelsB[x];
                if (pixelA == pixelB) {
                    max++;
                }
            }
        }

        return max;
    }


}






