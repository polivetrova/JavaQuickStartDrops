package catch_the_drop;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

public class GameWindow extends JFrame {

    private static GameWindow game_window;
    private static long lastFrameTime;
    private static Image background;
    private static Image gameOver;
    private static Image drop;
    private static float dropLeft = 100;
    private static float dropTop = -100;
    private static float dropSpeed = 200;
    private static int score = 0;

    public static void main(String[] args) throws IOException {

        background = ImageIO.read(GameWindow.class.getResourceAsStream("background.png"));
        gameOver = ImageIO.read(GameWindow.class.getResourceAsStream("gameover.png"));
        drop = ImageIO.read(GameWindow.class.getResourceAsStream("drop.png"));

        game_window = new GameWindow();
        game_window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        game_window.setLocation(100,50);
        game_window.setSize(1200,720);
        game_window.setResizable(false);
        lastFrameTime = System.nanoTime();
        GameField game_field = new GameField();
        game_field.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int x = e.getX();
                int y = e.getY();
                float dropRight = dropLeft + drop.getWidth(null);
                float dropBottom = dropTop + drop.getHeight(null);
                boolean isDrop = x >= dropLeft && x <= dropRight && y >= dropTop && y <= dropBottom;
                if(isDrop){
                    dropTop = -100;
                    dropLeft = (int)(Math.random() * (game_field.getWidth() - drop.getWidth(null)));
                    dropSpeed = dropSpeed + 20;
                    score++;
                    game_window.setTitle("Score: " + score);
                }
            }
        });
        game_window.add(game_field);
        game_window.setVisible(true);
    }

    private static void onRepaint(Graphics g){
        long currentTime = System.nanoTime();
        float deltaTime= (currentTime - lastFrameTime) * 0.000000001f;
        lastFrameTime = currentTime;

        dropTop = dropTop + dropSpeed * deltaTime;

        g.drawImage(background, 0, 0, null);
        g.drawImage(drop,(int) dropLeft,(int) dropTop,null);

        if(dropTop > game_window.getHeight())
            g.drawImage(gameOver,220,120,null);
    }

    private static class GameField extends JPanel {
        @Override
        protected void paintComponent(Graphics g){
            super.paintComponent(g);
            onRepaint(g);
            repaint();
        }
    }
}