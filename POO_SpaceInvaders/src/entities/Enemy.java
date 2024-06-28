package entities;

import game.EnemySpawn;
import game.Game;

import java.awt.image.BufferedImage;

public class Enemy extends Entity {

    int life = 1;

    public Enemy(double x, double y, int width, int height, double speed, BufferedImage sprite) {
        super(x, y, width, height, speed, sprite);
    }

    public void tick() {
        x += speed;
        if(x <= 0 || x >= Game.WIDTH - width) {
            speed = -speed;
            y += 16;
        }
        for(int i = 0; i < Game.entities.size(); i++) {
            Entity e = Game.entities.get(i);
            if(e instanceof Bullet) {
                if(Entity.isColidding(this, e)) {
                    life--;
                    Game.score++;
                    Game.entities.remove(e);
                    Player.isShooting = false;
                    Game.entities.remove(this);
                    break;
                }
            }
        }
    }
}
