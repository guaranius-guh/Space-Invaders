package entities;

import game.Game;

import java.awt.image.BufferedImage;

public class Player extends Entity {

    public boolean right, left;

    public static Bullet bullet;

    public static boolean isShooting = false;

    public Player(int x, int y, int width, int height, double speed, BufferedImage sprite) {
        super(x, y, width, height, speed, sprite);
    }

    public void tick() {
        if(right) {
            x += speed;
        } else if(left) {
            x -= speed;
        }
        if(x >= Game.WIDTH) {
            x = -16;
        } else if (x + 16 < 0) {
            x = Game.WIDTH;
        }
    }

    public void shoot() {
        if (!isShooting) {
            isShooting = true;
            bullet = new Bullet(x + 7, y, 2, 3, 4, null);
            Game.entities.add(bullet);
        }

    }
}
