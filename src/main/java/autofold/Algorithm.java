package autofold;

import graphics.Rect;
import graphics.Sprite;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static entity.Puzzle.N_N_MATRIX;
import static entity.Puzzle.RECT_SIZE;
import static graphics.Compare.*;
import static graphics.Side.LEFT;
import static graphics.Side.TOP;

public class Algorithm {

    private static final int n = N_N_MATRIX;
    private final int rectSize = RECT_SIZE;
    public static Rect[] rects = new Rect[n * n];
    public static Rect rect = new Rect();
    static Random random = new Random();

    public Algorithm() {
        fillRects("src/main/resources/pieces/");
    }


    public static boolean assemble(Rect[] rects) {
        shuffle(rects);
        while (!autoCheck(rects)) {
            rects[0] = findFirstRect(rects);
            shuffle(rects);
            for (int i = 0; i < rects.length; i++) {
                if (isExistRight(rects[i])) {
                    if (i == compareRightBorderForAutoFold(i, i + 1, rects)) {
                        continue;
                    }
                    if (i != compareRightBorderForAutoFold(i, i + 1, rects)) {
                        rect.swapRectangles(i + 1, compareRightBorderForAutoFold(i, i + 1, rects), rects);
                    }
                }
            }
        }
        return true;
    }

    public static Rect findFirstRect(Rect[] rects) {
        ArrayList<Integer> sum = new ArrayList<>();
        ArrayList<Integer> fullSum = new ArrayList<>();
        int leftBorder = 0;
        int topBorder = 0;
        for (int k = 0; k < rects.length; k++) {
            sum.removeAll(sum);
            for (int j = 0; j < rects.length; j++) {
                if (k == j) continue;
                leftBorder = rect.compareWithAllSides(rects[k], rects[j], LEFT);
                topBorder = rect.compareWithAllSides(rects[k], rects[j], TOP);
                sum.add(leftBorder + topBorder);
            }
            fullSum.add(sum.stream().mapToInt(Integer::intValue).sum());
        }
        int first = fullSum.indexOf(fullSum.stream().min(Integer::compare).orElse(Integer.MIN_VALUE));
        rect.swapRectangles(first, 0, rects);
        return rects[0];
    }

    public void fillRects(String folderPath) {
        List<BufferedImage> images = loadImagesFromFolder(folderPath);
        int index = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                int randomIndex = getRandomImageIndex(images);
                int[] pp = new int[rectSize * rectSize];
                BufferedImage image = images.remove(randomIndex);
                image.getRGB(0, 0, rectSize, rectSize, pp, 0, rectSize);
                rects[index] = new Rect(j * rectSize, i * rectSize, new Sprite(pp, rectSize, rectSize), index);
                index++;
            }
        }

    }

    public static void shuffle(Rect[] rects) {
        for (int i = 0; i < rects.length; i++) {
            int swap = random.nextInt(rects.length - 1);
            Rect rect = new Rect();
            rect.swapRectangles(i, swap, rects);
        }
    }


    private List<BufferedImage> loadImagesFromFolder(String folderPath) {
        File folder = new File(folderPath);
        File[] files = folder.listFiles();
        List<BufferedImage> images = new ArrayList<>();

        if (files != null) {
            for (File file : files) {
                try {
                    BufferedImage image = ImageIO.read(file);
                    images.add(image);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return images;
    }

    private int getRandomImageIndex(List<BufferedImage> images) {
        Random random = new Random();

        return random.nextInt(images.size());

    }
}
