package graphics;

import entity.Puzzle;

import java.util.ArrayList;
import java.util.Comparator;

import static entity.Puzzle.N_N_MATRIX;
import static entity.Puzzle.RECT_SIZE;
import static graphics.Side.*;

public class Compare {

    public static boolean autoCheck(Rect[] rects) {

        for (int i = 0; i < rects.length; i++) {
            if (isExistRight(rects[i])) {
                if (!compareRightBorder(i, i + 1, rects)) {
                    return false;

                }
            }
            if (isExistTop(rects[i])) {
                if (!compareTopBorder(i, rects)) {
                    return false;
                }
            }
            ;
            if (isExistBottom(rects[i])) {
                if (!compareBottomBorder(i, i + N_N_MATRIX, rects)) {
                    return false;
                }
            }
            if (isExistLeft(rects[i])) {
                if (!compareLeftBorder(i, rects)) {
                    return false;
                }
            }

        }
        return true;
    }

    public static boolean isExistTop(Rect rect) {
        if (rect.y == 0) {
            return false;
        }

        return true;
    }

    public static boolean isExistBottom(Rect rect) {

        if (rect.y == Puzzle.HEIGHT - RECT_SIZE) {
            return false;
        }
        return true;
    }

    public static boolean isExistLeft(Rect rect) {
        if (rect.x == 0) {
            return false;
        }


        return true;
    }

    public static boolean isExistRight(Rect rect) {
        if (rect.x == Puzzle.WIDTH - RECT_SIZE) {
            return false;
        }
        return true;
    }

    public static boolean compareRightBorder(int i, int j, Rect[] rects) {
        ArrayList<Integer> integers = new ArrayList<>();
        int first = compareWithOneSide(rects[i], rects[j], RIGHT);
        for (int k = 0; k < rects.length; k++) {
            if (k != i) {
                integers.add(compareWithAllSides(rects[i], rects[k], RIGHT, 0));
            }
        }
        return integers.stream().allMatch(match -> first >= match);


    }

    public static int compareRightBorderForAutoFold(int i, int j, Rect[] rects) {
        ArrayList<Integer> integers = new ArrayList<>();
        int first = compareWithOneSide(rects[i], rects[j], RIGHT);
        for (int k = 0; k < rects.length; k++) {
            integers.add(compareWithOneSide(rects[i], rects[k], RIGHT));
        }
        if (integers.stream().allMatch(match -> first >= match)) {
            return i;
        } else return integers.indexOf(integers.stream().max(Integer::compare).orElse(Integer.MIN_VALUE));


    }


    public static boolean compareLeftBorder(int j, Rect[] rects) {
        ArrayList<Integer> integers = new ArrayList<>();
        int first = compareWithOneSide(rects[j], rects[j - 1], LEFT);
        for (int k = 0; k < rects.length - N_N_MATRIX - 1; k++) {
            if (k != j) {
                integers.add(compareWithAllSides(rects[j], rects[k], LEFT, 0));
            }
        }
        return integers.stream().allMatch(match -> first >= match);

    }

    public static boolean compareBottomBorder(int i, int j, Rect[] rects) {
        ArrayList<Integer> integers = new ArrayList<>();
        int first = compareWithOneSide(rects[i], rects[j], BOTTOM);
        for (int k = 0; k < rects.length; k++) {
            if (k != i) {
                integers.add(compareWithAllSides(rects[i], rects[k], BOTTOM, 0));
            }
        }
        return integers.stream().allMatch(match -> first >= match);
    }

    public static boolean compareTopBorder(int j, Rect[] rects) {
        ArrayList<Integer> integers = new ArrayList<>();
        int first = compareWithOneSide(rects[j], rects[j - N_N_MATRIX], TOP);
        for (int k = 0; k < rects.length; k++) {
            if (k != j) {
                integers.add(compareWithAllSides(rects[j], rects[k], TOP, 0));
            }
        }

        return integers.stream().allMatch(value -> value <= first);

    }

    public static Integer compareWithAllSides(Rect rect1, Rect rect2, Side side, int value) {
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
        if (value == 0) {
            return integers.stream()
                    .max(Comparator.naturalOrder())
                    .orElse(0);
        }
        return integers.stream().mapToInt(Integer::intValue).sum();


    }

    public static int compareWithOneSide(Rect rect1, Rect rect2, Side side) {
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

    public static int rightRight(int[] pixelsA, int[] pixelsB, int rectSize) {
        int max = 0;
        for (int y = 0; y < rectSize; y++) {
            int pixelA = pixelsA[(rectSize - 1) + y * rectSize];
            int pixelB = pixelsB[(rectSize - 1) + y * rectSize];
            if (pixelA == pixelB) {
                max++;
            }
        }
        return max;
    }

    public static int rightLeft(int[] pixelsA, int[] pixelsB, int rectSize) {
        int max = 0;
        for (int y = 0; y < rectSize; y++) {
            int pixelA = pixelsA[(rectSize - 1) + y * rectSize];
            int pixelB = pixelsB[y * rectSize];
            if (pixelA == pixelB) {
                max++;
            }
        }
        return max;
    }

    public static int rightTop(int[] pixelsA, int[] pixelsB, int rectSize) {
        int max = 0;
        for (int y = 0; y < rectSize; y++) {
            int pixelA = pixelsA[(rectSize - 1) + y * rectSize];
            int pixelB = pixelsB[y];
            ;
            if (pixelA == pixelB) {
                max++;
            }
        }
        return max;
    }

    public static int rightBottom(int[] pixelsA, int[] pixelsB, int rectSize) {
        int max = 0;
        for (int y = 0; y < rectSize; y++) {
            int pixelA = pixelsA[(rectSize - 1) + y * rectSize];
            int pixelB = pixelsB[(rectSize - 1) * rectSize + y];
            ;
            if (pixelA == pixelB) {
                max++;
            }
        }
        return max;
    }

    public static int bottomBottom(int[] pixelsA, int[] pixelsB, int rectSize) {
        int max = 0;
        for (int y = 0; y < rectSize; y++) {
            int pixelA = pixelsA[(rectSize - 1) * rectSize + y];
            int pixelB = pixelsB[(rectSize - 1) * rectSize + y];
            ;
            if (pixelA == pixelB) {
                max++;
            }
        }
        return max;
    }

    public static Integer bottomTop(int[] pixelsA, int[] pixelsB, int rectSize) {
        int max = 0;
        for (int y = 0; y < rectSize; y++) {
            int pixelA = pixelsA[(rectSize - 1) * rectSize + y];
            int pixelB = pixelsB[y];
            ;
            if (pixelA == pixelB) {
                max++;
            }
        }
        return max;
    }

    public static Integer bottomLeft(int[] pixelsA, int[] pixelsB, int rectSize) {
        int max = 0;
        for (int y = 0; y < rectSize; y++) {
            int pixelA = pixelsA[(rectSize - 1) * rectSize + y];
            int pixelB = pixelsB[y * rectSize];
            if (pixelA == pixelB) {
                max++;
            }
        }
        return max;
    }

    public static Integer leftLeft(int[] pixelsA, int[] pixelsB, int rectSize) {
        int max = 0;
        for (int y = 0; y < rectSize; y++) {
            int pixelA = pixelsA[y * rectSize];
            int pixelB = pixelsB[y * rectSize];
            if (pixelA == pixelB) {
                max++;
            }
        }
        return max;
    }

    public static Integer leftTop(int[] pixelsA, int[] pixelsB, int rectSize) {
        int max = 0;
        for (int y = 0; y < rectSize; y++) {
            int pixelA = pixelsA[y * rectSize];
            int pixelB = pixelsB[y];
            ;
            if (pixelA == pixelB) {
                max++;
            }
        }
        return max;
    }

    public static Integer topTop(int[] pixelsA, int[] pixelsB, int rectSize) {
        int max = 0;
        for (int y = 0; y < rectSize; y++) {
            int pixelA = pixelsA[y];
            ;
            int pixelB = pixelsB[y];
            ;
            if (pixelA == pixelB) {
                max++;
            }
        }
        return max;
    }


}
