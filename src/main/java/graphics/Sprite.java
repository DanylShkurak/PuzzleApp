package graphics;

import entity.Puzzle;
import org.apache.commons.io.FileUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;

import static entity.Puzzle.N_N_MATRIX;
import static entity.Puzzle.RECT_SIZE;


public class Sprite {
    public int width;
    public int height;
    public int[] pixels;
    private int[] angles = {90, 180, 270};


    public static Sprite[] picture = createSprites("/pictures/pepe.jpg");

    public Sprite(int[] pp, int width, int height) {
        this.width = width;
        this.height = height;
        this.pixels = Arrays.copyOf(pp, pp.length);

    }

    private static BufferedImage resizeImage(BufferedImage originalImage, int type, int width, int height) {
        BufferedImage resizedImage = new BufferedImage(width, height, type);
        Graphics2D g = resizedImage.createGraphics();
        g.drawImage(originalImage, 0, 0, width, height, null);
        g.dispose();

        return resizedImage;
    }


    public static Sprite[] createSprites(String path) {
        try {
            BufferedImage image = ImageIO.read(Objects.requireNonNull(Sprite.class.getResource(path)));
            int type = image.getType() == 0 ? BufferedImage.TYPE_INT_RGB : image.getType();
            BufferedImage resizeImage = resizeImage(image, type, Puzzle.WIDTH, Puzzle.HEIGHT);

            Sprite[] sprites = new Sprite[N_N_MATRIX * N_N_MATRIX];
            for (int y = 0; y < N_N_MATRIX; y++) {
                for (int x = 0; x < N_N_MATRIX; x++) {
                    BufferedImage pieceImage = resizeImage.getSubimage(x * RECT_SIZE, y * RECT_SIZE, RECT_SIZE, RECT_SIZE);
                    int[] pp = new int[RECT_SIZE * RECT_SIZE];
                    pieceImage.getRGB(0, 0, RECT_SIZE, RECT_SIZE, pp, 0, RECT_SIZE);
                    sprites[x + y * N_N_MATRIX] = new Sprite(pp, RECT_SIZE, RECT_SIZE);
                }
            }
            saveCutPieces(resizeImage, "src/main/resources/pieces/", "png");
            return sprites;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    private static void saveCutPieces(BufferedImage image, String outputFolder, String ext) {
        File directory = new File(outputFolder);
        try {
            FileUtils.cleanDirectory(directory);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        for (int y = 0; y < N_N_MATRIX; y++) {
            for (int x = 0; x < N_N_MATRIX; x++) {
                BufferedImage pieceImage = image.getSubimage(x * RECT_SIZE, y * RECT_SIZE, RECT_SIZE, RECT_SIZE);
                File output = new File(outputFolder + "picture" + y + x + "." + ext);
                try {
                    ImageIO.write(pieceImage, ext, output);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void rotate(int angle) {
        if (angle != 90) {
            angle = angles[(int) (Math.random() * 2)];
        }
        int[] rotatedPixels = new int[width * height];
        int newX = 0;
        int newY = 0;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {

                if (angle == 90) {
                    newX = height - y - 1;
                    newY = x;
                }
                if (angle == 180) {
                    newX = width - x - 1;
                    newY = height - y - 1;
                }
                if (angle == 270) {
                    newX = y;
                    newY = width - x - 1;
                }


                rotatedPixels[newX + newY * width] = pixels[x + y * width];
            }
        }

        pixels = rotatedPixels;
        int temp = width;
        width = height;
        height = temp;
    }


    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int[] getPixels() {
        return pixels;
    }
}

