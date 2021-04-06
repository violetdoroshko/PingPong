import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

public class Pong implements ActionListener, KeyListener, Observable {

    public static Pong pong;

    public final int WIDTH = 700, HEIGHT = 700;

    public Renderer renderer;

    public Paddle player1;
    public Paddle player2;
    public Ball ball;

    public boolean w, s, up, down;

    public int gameStatus = 0;  //0 = Menu, 1 = Playing, 2 = Over
    public int scoreLimit = 7;
    public int playerWon;

    FirstPlayer firstPlayer;
    SecondPlayer secondPlayer;

    private java.util.List<Observer> observers;

    public Pong() {
        Timer timer = new Timer(20, this);

        JFrame jframe = new JFrame("Pong");

        jframe.setSize(WIDTH + 15, HEIGHT + 35);
        jframe.setVisible(true);
        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        renderer = new Renderer();

        jframe.add(renderer);
        jframe.addKeyListener(this);
        observers = new ArrayList<>();

        firstPlayer = new FirstPlayer();
        secondPlayer = new SecondPlayer();

        this.attach(firstPlayer);
        this.attach(secondPlayer);

        timer.start();
    }

    public static void main(String[] args) {
        pong = new Pong();
    }

    public void start() {
        gameStatus = 1;
        player1 = new Paddle(this, 1);
        player2 = new Paddle(this, 2);
        ball = new Ball(this);
    }


    public void render(Graphics2D g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, WIDTH, HEIGHT);
        //сглаживание всех отрисовываемых в дальнейшем фигур
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (gameStatus == 0) {

            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", 1, 30));
            g.drawString("Press Space to Play", WIDTH / 2 - 150, HEIGHT / 2 - 25);
            g.drawString("<< Score Limit: " + scoreLimit + " >>", WIDTH / 2 - 150, HEIGHT / 2 + 75);

        }

        if (gameStatus == 1) {

            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", 1, 50));
            g.drawString(String.valueOf(player1.score), WIDTH / 2 - 90, 50);
            g.drawString(String.valueOf(player2.score), WIDTH / 2 + 65, 50);

            player1.render(g);
            player2.render(g);
            ball.render(g);
        }

        if (gameStatus == 2) {

            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", 1, 50));
            g.drawString("PONG", WIDTH / 2 - 75, 50);
            g.setFont(new Font("Arial", 1, 30));

            if (playerWon == 1)
                g.drawString("First Player Won!", WIDTH / 2 - 130, HEIGHT / 2 - 75);
            else
                g.drawString("Second Player Won!", WIDTH / 2 - 140, HEIGHT / 2 - 75);
            notifyObservers();
            playerWon = 0;
            g.drawString("Press Space to Play Again", WIDTH / 2 - 185, HEIGHT / 2 - 25);
            g.drawString("Press ESC for Menu", WIDTH / 2 - 140, HEIGHT / 2 + 25);
            g.drawString("First Player : Second Player", WIDTH / 2 - 200, HEIGHT / 2 + 75);
            g.drawString(firstPlayer.score + " : " + secondPlayer.score, WIDTH / 2 - 30, HEIGHT / 2 + 125);
        }
    }

    void update() {
        if (player1.score >= scoreLimit) {
            playerWon = 1;
            gameStatus = 2;
        }
        if (player2.score >= scoreLimit) {
            playerWon = 2;
            gameStatus = 2;
        }

        if (w) {
            player1.move(true);
        }
        if (s) {
            player1.move(false);
        }

        if (up) {
            player2.move(true);
        }
        if (down) {
            player2.move(false);
        }

        Pong.pong.ball.update(Pong.pong.player1, Pong.pong.player2);
    }

    //Controller
    @Override
    public void actionPerformed(ActionEvent e) {
        if (gameStatus == 1)
            update();
        renderer.repaint();
    }

    @Override
    public void keyTyped(KeyEvent keyEvent) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        int id = e.getKeyCode();

        if (id == KeyEvent.VK_W) {
            w = true;
        } else if (id == KeyEvent.VK_S) {
            s = true;
        } else if (id == KeyEvent.VK_UP) {
            up = true;
        } else if (id == KeyEvent.VK_DOWN) {
            down = true;


        } else if (id == KeyEvent.VK_RIGHT) {
            if (gameStatus == 0) {
                scoreLimit++;
            }
        } else if (id == KeyEvent.VK_LEFT) {
            if (gameStatus == 0 && scoreLimit > 1) {
                scoreLimit--;
            }
        } else if (id == KeyEvent.VK_ESCAPE && (gameStatus == 1 || gameStatus == 2)) {
            gameStatus = 0;
        } else if (id == KeyEvent.VK_SPACE) {
            if (gameStatus == 0 || gameStatus == 2) {
                start();
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int id = e.getKeyCode();

        if (id == KeyEvent.VK_W) {
            w = false;
        } else if (id == KeyEvent.VK_S) {
            s = false;
        } else if (id == KeyEvent.VK_UP) {
            up = false;
        } else if (id == KeyEvent.VK_DOWN) {
            down = false;
        }
    }

    @Override
    public void attach(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void detach(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers() {
        for (Observer ob : observers) {
            ob.update();
        }
    }
}