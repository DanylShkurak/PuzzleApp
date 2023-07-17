package graphics;

import entity.Puzzle;

import java.awt.*;

public class Renderer {
    public static int width = Puzzle.WIDTH;
    public static int height = Puzzle.HEIGHT;
    public static int[] pixels = new int[width * height];

    public static void renderBackground() {
        for (int i = 0; i < pixels.length; i++) {
            pixels[i] = Color.WHITE.hashCode();
        }
    }

    public static void renderSprite(Sprite sprite, int xp, int yp) {
        if (xp < -sprite.width || xp > width || yp < -sprite.height || yp > height) {
            return;
        }
        for (int y = 0; y < sprite.height; y++) {
            int yy = y + yp;
            if (yy < 0 || yy >= height) continue;
            for (int x = 0; x < sprite.width; x++) {
                int xx = x + xp;
                if (xx < 0 || xx >= width) continue;
                int color = sprite.pixels[x + y * sprite.width];
                pixels[xx + yy * width] = color;
            }
        }
    }
}

