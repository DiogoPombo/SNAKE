/*------------------------------------------
-        SNAKE - "Jogo da Cobrinha"        -
- Author: Diogo Santos Pombo - \Õ/ - @2026 -
--------------------------------------------*/

package com.Snake.Snake;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import java.time.LocalTime;

/**
 * Snake Game - Java Swing
 * Compilar: javac SnakeGame.java
 * Executar: java SnakeGame
 */
public class SnakeGame extends JFrame {

    public static void main(String[] args) {
    	log("Iniciando...");
        SwingUtilities.invokeLater(() -> new SnakeGame());
    }

    public SnakeGame() {
    	log("Criando janela do jogo...");
        setTitle("SNAKE");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        GamePanel panel = new GamePanel();
        add(panel);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
    
    static void log(String msg) {
        System.out.println("[" + LocalTime.now() + "] [INFO] " + msg);
    }
}

class GamePanel extends JPanel implements ActionListener, KeyListener {

    // Configurações do grid
    static final int CELL_SIZE = 24;
    static final int GRID_W = 25;
    static final int GRID_H = 25;
    static final int WIDTH = GRID_W * CELL_SIZE;
    static final int HEIGHT = GRID_H * CELL_SIZE;
    static final int TICK_MS = 130;

    // Cores
    static final Color COLOR_BG       = new Color(15, 23, 42);
    static final Color COLOR_GRID     = new Color(30, 41, 59);
    static final Color COLOR_HEAD     = new Color(34, 211, 238);
    static final Color COLOR_BODY     = new Color(8, 145, 178);
    static final Color COLOR_TAIL     = new Color(21, 94, 117);
    static final Color COLOR_FOOD     = new Color(244, 63, 94);
    static final Color COLOR_TEXT     = new Color(226, 232, 240);
    static final Color COLOR_OVERLAY  = new Color(15, 23, 42, 200);

    // Estado
    enum GameState { IDLE, PLAYING, PAUSED, GAMEOVER }
    GameState state = GameState.IDLE;

    // Snake
    ArrayList<Point> snake = new ArrayList<>();
    Point food = new Point();
    int dx = 1, dy = 0;
    int nextDx = 1, nextDy = 0;
    int score = 0;
    int highScore = 0;

    Timer timer;
    Random random = new Random();
    
    void log(String msg) {
        System.out.println("[" + LocalTime.now() + "] [GAME] " + msg);
    }

    public GamePanel() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT + 40));
        setBackground(COLOR_BG);
        setFocusable(true);
        addKeyListener(this);
        resetGame();
        timer = new Timer(TICK_MS, this);
        log("Timer configurado: " + TICK_MS + "ms");
    }

    void resetGame() {
    	log("Resetando jogo...");
        snake.clear();
        snake.add(new Point(12, 12));
        snake.add(new Point(11, 12));
        snake.add(new Point(10, 12));
        dx = 1; dy = 0;
        nextDx = 1; nextDy = 0;
        score = 0;
        spawnFood();
    }

    void spawnFood() {
    	//log("Gerando comida...");
        Point p;
        do {
            p = new Point(random.nextInt(GRID_W), random.nextInt(GRID_H));
        } while (snake.contains(p));
        food = p;
        log("Comida em: (" + food.x + "," + food.y + ")");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
    	//log("Novo frame");
    	
        dx = nextDx;
        dy = nextDy;

        Point head = snake.get(0);
        Point newHead = new Point(head.x + dx, head.y + dy);
        
        //log("Movimento: (" + head.x + "," + head.y + ") -> (" + newHead.x + "," + newHead.y + ")");

        // Colisão com paredes
        if (newHead.x < 0 || newHead.x >= GRID_W || newHead.y < 0 || newHead.y >= GRID_H) {
        	log("Colisão com parede!");
            gameOver();
            return;
        }

        // Colisão com o corpo
        if (snake.contains(newHead)) {
        	log("Colisão com corpo!");
            gameOver();
            return;
        }

        snake.add(0, newHead);

        // Comeu a comida
        if (newHead.equals(food)) {
            score += 10;
            log("Comeu comida! Score: " + score);
            if (score > highScore) highScore = score;
            spawnFood();
        } else {
            snake.remove(snake.size() - 1);
        }

        repaint();
    }

    void gameOver() {
    	log("GAME OVER! Pontuação desta partida: " + score + ", Pontuação máxima: " + highScore);
        timer.stop();
        if (score > highScore) highScore = score;
        state = GameState.GAMEOVER;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        //log("Renderizando frame");
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Score bar
        g2.setColor(new Color(30, 41, 59));
        g2.fillRect(0, 0, WIDTH, 40);
        g2.setColor(COLOR_TEXT);
        g2.setFont(new Font("Monospaced", Font.BOLD, 14));
        g2.drawString("Pontos: " + score, 12, 26);
        String hi = "Recorde: " + highScore;
        FontMetrics fm = g2.getFontMetrics();
        g2.setColor(new Color(250, 204, 21));
        g2.drawString(hi, WIDTH - fm.stringWidth(hi) - 12, 26);

        int offsetY = 40;

        // Background
        g2.setColor(COLOR_BG);
        g2.fillRect(0, offsetY, WIDTH, HEIGHT);

        // Grid
        g2.setColor(COLOR_GRID);
        for (int x = 0; x <= GRID_W; x++)
            g2.drawLine(x * CELL_SIZE, offsetY, x * CELL_SIZE, offsetY + HEIGHT);
        for (int y = 0; y <= GRID_H; y++)
            g2.drawLine(0, offsetY + y * CELL_SIZE, WIDTH, offsetY + y * CELL_SIZE);

        // Comida
        //log("Desenhando comida em (" + food.x + "," + food.y + ")");
        g2.setColor(COLOR_FOOD);
        int fx = food.x * CELL_SIZE + 3;
        int fy = offsetY + food.y * CELL_SIZE + 3;
        int fs = CELL_SIZE - 6;
        g2.fillOval(fx, fy, fs, fs);
        g2.setColor(new Color(251, 113, 133));
        g2.setStroke(new BasicStroke(1.5f));
        g2.drawOval(fx, fy, fs, fs);

        // Snake
        for (int i = 0; i < snake.size(); i++) {
            Point p = snake.get(i);
            int px = p.x * CELL_SIZE;
            int py = offsetY + p.y * CELL_SIZE;

            if (i == 0) {
                g2.setColor(COLOR_HEAD);
                g2.fillRoundRect(px + 1, py + 1, CELL_SIZE - 2, CELL_SIZE - 2, 8, 8);
                // Olhos
                g2.setColor(COLOR_BG);
                int ex = dx != 0 ? dx * 6 : 0;
                int ey = dy != 0 ? dy * 6 : 0;
                int cx = px + CELL_SIZE / 2 + ex;
                int cy = py + CELL_SIZE / 2 + ey;
                g2.fillOval(cx + dy * 4 - 2, cy - dx * 4 - 2, 4, 4);
                g2.fillOval(cx - dy * 4 - 2, cy + dx * 4 - 2, 4, 4);
            } else {
                float ratio = (float) i / snake.size();
                Color bodyColor = ratio < 0.5f ? COLOR_BODY : COLOR_TAIL;
                g2.setColor(bodyColor);
                g2.fillRoundRect(px + 2, py + 2, CELL_SIZE - 4, CELL_SIZE - 4, 6, 6);
            }
        }

        // Overlays
        if (state == GameState.IDLE || state == GameState.GAMEOVER || state == GameState.PAUSED) {
            g2.setColor(COLOR_OVERLAY);
            g2.fillRect(0, offsetY, WIDTH, HEIGHT);

            g2.setFont(new Font("Monospaced", Font.BOLD, 28));
            FontMetrics fmTitle = g2.getFontMetrics();

            String title = state == GameState.IDLE ? "SNAKE" :
                           state == GameState.PAUSED ? "PAUSADO" : "GAME OVER";
            Color titleColor = state == GameState.GAMEOVER ? new Color(248, 113, 113) :
                               state == GameState.PAUSED ? new Color(250, 204, 21) :
                               COLOR_HEAD;
            g2.setColor(titleColor);
            g2.drawString(title, (WIDTH - fmTitle.stringWidth(title)) / 2, offsetY + HEIGHT / 2 - 10);

            g2.setFont(new Font("Monospaced", Font.PLAIN, 13));
            FontMetrics fmSub = g2.getFontMetrics();
            String sub = state == GameState.GAMEOVER
                ? "Pontuação: " + score + "  |  ENTER para jogar novamente"
                : "Pressione ENTER para começar";
            g2.setColor(new Color(148, 163, 184));
            g2.drawString(sub, (WIDTH - fmSub.stringWidth(sub)) / 2, offsetY + HEIGHT / 2 + 20);
        }

        g2.dispose();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        //log("Tecla: " + KeyEvent.getKeyText(key));
        switch (key) {
            case KeyEvent.VK_UP:    case KeyEvent.VK_W: if (dy == 0) { nextDx = 0; nextDy = -1; } break;
            case KeyEvent.VK_DOWN:  case KeyEvent.VK_S: if (dy == 0) { nextDx = 0; nextDy = 1;  } break;
            case KeyEvent.VK_LEFT:  case KeyEvent.VK_A: if (dx == 0) { nextDx = -1; nextDy = 0; } break;
            case KeyEvent.VK_RIGHT: case KeyEvent.VK_D: if (dx == 0) { nextDx = 1; nextDy = 0;  } break;
            case KeyEvent.VK_ENTER:
            	log("ENTER pressionado");
                if (state == GameState.IDLE || state == GameState.GAMEOVER) {
                    resetGame();
                    state = GameState.PLAYING;
                    timer.start();
                } else if (state == GameState.PLAYING) {
                    state = GameState.PAUSED;
                    timer.stop();
                } else if (state == GameState.PAUSED) {
                    state = GameState.PLAYING;
                    timer.start();
                }
                repaint();
                break;
        }

        if (state == GameState.IDLE) {
            if (key == KeyEvent.VK_UP || key == KeyEvent.VK_DOWN ||
                key == KeyEvent.VK_LEFT || key == KeyEvent.VK_RIGHT ||
                key == KeyEvent.VK_W || key == KeyEvent.VK_S ||
                key == KeyEvent.VK_A || key == KeyEvent.VK_D) {
                state = GameState.PLAYING;
                timer.start();
                repaint();
            }
        }
    }

    @Override public void keyReleased(KeyEvent e) {}
    @Override public void keyTyped(KeyEvent e) {}
}