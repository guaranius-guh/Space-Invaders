package game;

import entities.Enemy;
import entities.Entity;
import entities.Player;
import graphics.Spritesheet;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class Game extends Canvas implements Runnable, KeyListener {

    public static JFrame frame;
    private Thread thread;
    private boolean isRunning = true;
    public static boolean isPlaying = true;
    public static final int WIDTH = 256;
    public static final int HEIGHT = 304;
    public static final int SCALE = 2;

    public static int score = 0;

    private final BufferedImage image;

    public static List<Entity> entities;
    public static Spritesheet spritesheet;
    public static Player player;
    public EnemySpawn enemySpawn;

    public static void main(String[] args) {
        Game game = new Game();
        game.start();
    }

    public Game() {
        addKeyListener(this);
        setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
        initFrame();
        image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);

        spritesheet = new Spritesheet("/spritesheet.png");
        entities = new ArrayList<Entity>();
        player = new Player(WIDTH / 2 - 8, HEIGHT - 32, 16, 16, 1, spritesheet.getSprite(0, 0, 16, 16));
        enemySpawn = new EnemySpawn();
        enemySpawn.tick();

        entities.add(player);
    }
    
    public void initFrame() {
        frame = new JFrame("Space Invaders");
        frame.add(this);
        frame.setResizable(false);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    public synchronized void start() {
        thread = new Thread(this);
        isRunning = true;
        thread.start();
    }

    public synchronized void stop() {
        isRunning = false;
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void restart() {
        spritesheet = new Spritesheet("/spritesheet.png");
        entities = new ArrayList<Entity>();
        player = new Player(WIDTH / 2 - 8, HEIGHT - 32, 16, 16, 1, spritesheet.getSprite(0, 0, 16, 16));
        enemySpawn = new EnemySpawn();
        enemySpawn.tick();

        entities.add(player);
    }

    public void tick() {
        for(int i = 0; i < entities.size(); i++) {
            Entity e = entities.get(i);
            e.tick();
        }
    }

    public void render() {
        BufferStrategy bs = this.getBufferStrategy();
        if(bs == null) {
            this.createBufferStrategy(3);
            return;
        }
        Graphics g = image.getGraphics();
        g.setColor(new Color(0, 0, 0));
        g.fillRect(0, 0, WIDTH, HEIGHT);
        g.setColor(new Color(255, 255, 255));
        g.setFont(new Font("Arial", Font.BOLD, 8));
        g.drawString("Score: " + score, 4, 10);
        for(int i = 0; i < entities.size(); i++) {
            Entity e = entities.get(i);
            e.render(g);
        }
        g.dispose();
        g = bs.getDrawGraphics();
        g.drawImage(image, 0, 0, WIDTH * SCALE, HEIGHT * SCALE, null);
        bs.show();
    }

    @Override
    public void run() {
        long lastTime = System.nanoTime();
        double amountOfTicks = 60.0;
        double ns = 1000000000 / amountOfTicks;
        double delta = 0;
        int frames = 0;
        double timer = System.currentTimeMillis();
        requestFocus();
        while(isRunning) {
            while(isPlaying) {
                    long now = System.nanoTime();
                    delta += (now - lastTime) / ns;
                    lastTime = now;
                    if(delta >= 1) {
                        tick();
                        render();
                        frames++;
                        delta--;
                    }
                if(System.currentTimeMillis() - timer >= 1000) {
                    System.out.println("FPS: " + frames);
                    frames = 0;
                    timer += 1000;
                }
                if(entities.size() == 1) {
                    restart();
                }
                for(int i = 0; i < entities.size(); i++) {
                    Entity e = entities.get(i);
                    if(e == player || e == Player.bullet) {
                        continue;
                    }
                    if(Entity.isColidding(player, e)) {
                        isPlaying = false;
                        System.out.println("Game Over!");
                    }
                }
            }
        }
        stop();
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_D || e.getKeyCode() == KeyEvent.VK_RIGHT) {
            player.right = true;
        } else if (e.getKeyCode() == KeyEvent.VK_A || e.getKeyCode() == KeyEvent.VK_LEFT) {
            player.left = true;
        }
        if(e.getKeyCode() == KeyEvent.VK_SPACE) {
            player.shoot();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_D || e.getKeyCode() == KeyEvent.VK_RIGHT) {
            player.right = false;
        } else if (e.getKeyCode() == KeyEvent.VK_A || e.getKeyCode() == KeyEvent.VK_LEFT) {
            player.left = false;
        }
    }
}
