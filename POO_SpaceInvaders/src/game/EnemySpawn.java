package game;

import entities.Enemy;

public class EnemySpawn {

    public static final int ENEMY_ROWS = 3;
    public static final int ENEMY_COLUMNS = 6;

    public void tick() {
        for (int row = 0; row < ENEMY_ROWS; row++) {
            for (int col = 0; col < ENEMY_COLUMNS; col++) {
                int x = col * 32;
                int y = row * 32;
                Enemy enemy = new Enemy(x, y, 16, 16, 9.5, Game.spritesheet.getSprite(16, 0, 16, 16));
                Game.entities.add(enemy);
            }
        }
    }
}
