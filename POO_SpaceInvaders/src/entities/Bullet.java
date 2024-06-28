package entities;

import game.Game;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Bullet extends Entity {

    public Bullet(double x, double y, int width, int height, double speed, BufferedImage sprite) {
        super(x, y, width, height, speed, sprite);
    }

    public void tick() {
        y -= speed;
        if(y < 0) {
            Player.isShooting = false;
            Game.entities.remove(this);
        }
    }

    public void render(Graphics g) {
        g.setColor(new Color(255, 175, 0));
        g.fillRect(getX(), getY(), width, height);
    }
}
