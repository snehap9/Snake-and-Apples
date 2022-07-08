import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener{

    static final int PANEL_WIDTH = 500;
    static final int PANEL_HEIGHT = 500;
    static final int UNIT_SIZE = 20;
    static final int GAME_UNITS = (PANEL_WIDTH*PANEL_HEIGHT)/(UNIT_SIZE*UNIT_SIZE);
    static final int DELAY = 200;
    final int x[] = new int[GAME_UNITS];
    final int y[] = new int[GAME_UNITS];
    int initSnake = 5;
    int applesConsumed;
    int appleX;
    int appleY;
    char direction = 'D';
    boolean running = false;
    Timer timer;
    Random random;

    GamePanel(){
        random = new Random();
        this.setPreferredSize(new Dimension(PANEL_WIDTH,PANEL_HEIGHT));
        this.setBackground(Color.green);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        startGame();
    }
    public void startGame() {
        newApple();
        running = true;
        timer = new Timer(DELAY,this);
        timer.start();
    }
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }
    public void draw(Graphics g) {

        if(running) {
            g.setColor(Color.red);
            g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);

            for(int i = 0; i < initSnake; i++) {
                if(i == 0) {
                    g.setColor(Color.black);
                    g.fillOval(x[i], y[i], UNIT_SIZE, UNIT_SIZE-5);
                }
                else {
                    g.setColor(Color.darkGray);
                    g.fillRoundRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE, 100, 100);
                }
            }
            g.setColor(Color.red);
            g.setFont( new Font("Serif",Font.BOLD, 35));
            FontMetrics metrics = getFontMetrics(g.getFont());
            g.drawString("Score: "+applesConsumed, (PANEL_WIDTH - metrics.stringWidth("Score: "+applesConsumed))/2, g.getFont().getSize());
        }
        else {
            gameOver(g);
        }

    }
    public void newApple(){
        appleX = random.nextInt((int)(PANEL_WIDTH/UNIT_SIZE))*UNIT_SIZE;
        appleY = random.nextInt((int)(PANEL_HEIGHT/UNIT_SIZE))*UNIT_SIZE;
    }
    public void move(){
        for(int i = initSnake; i > 0; i--) {
            x[i] = x[i-1];
            y[i] = y[i-1];
        }

        switch(direction) {
            case 'U':
                y[0] = y[0] - UNIT_SIZE;
                break;
            case 'D':
                y[0] = y[0] + UNIT_SIZE;
                break;
            case 'L':
                x[0] = x[0] - UNIT_SIZE;
                break;
            case 'R':
                x[0] = x[0] + UNIT_SIZE;
                break;
        }

    }
    public void checkApple() {
        if((x[0] == appleX) && (y[0] == appleY)) {
            initSnake++;
            applesConsumed++;
            newApple();
        }
    }
    public void checkCollisions() {
        //checks if head collides with body
        for(int i = initSnake;i>0;i--) {
            if((x[0] == x[i])&& (y[0] == y[i])) {
                running = false;
            }
        }
        //check if head touches left border
        if(x[0] < 0) {
            running = false;
        }
        //check if head touches right border
        if(x[0] > PANEL_WIDTH) {
            running = false;
        }
        //check if head touches top border
        if(y[0] < 0) {
            running = false;
        }
        //check if head touches bottom border
        if(y[0] > PANEL_HEIGHT) {
            running = false;
        }

        if(!running) {
            timer.stop();
        }
    }
    public void gameOver(Graphics g) {
        //Score
        g.setColor(Color.red);
        g.setFont( new Font("Serif",Font.BOLD, 50));
        FontMetrics metrics1 = getFontMetrics(g.getFont());
        g.drawString("Score: "+applesConsumed, (PANEL_WIDTH - metrics1.stringWidth("Score: "+applesConsumed))/2, g.getFont().getSize());
        //Game Over text
        g.setColor(Color.red);
        g.setFont( new Font("Dialog",Font.BOLD, 65));
        FontMetrics metrics2 = getFontMetrics(g.getFont());
        g.drawString("GAME OVER!", (PANEL_WIDTH - metrics2.stringWidth("GAME OVER!"))/2, PANEL_HEIGHT/2);
    }
    @Override
    public void actionPerformed(ActionEvent e) {

        if(running) {
            move();
            checkApple();
            checkCollisions();
        }
        repaint();
    }

    public class MyKeyAdapter extends KeyAdapter{
        @Override
        public void keyPressed(KeyEvent e) {
            switch(e.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    if(direction != 'R') {
                        direction = 'L';
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if(direction != 'L') {
                        direction = 'R';
                    }
                    break;
                case KeyEvent.VK_UP:
                    if(direction != 'D') {
                        direction = 'U';
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if(direction != 'U') {
                        direction = 'D';
                    }
                    break;
            }
        }
    }
}
