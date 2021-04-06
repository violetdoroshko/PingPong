import java.awt.*;

public class Paddle {

    public int paddleNumber;

    public int x, y;
    public final int WIDTH = 50, HEIGHT = 250;

    public int score;

    public Paddle(Pong pong, int paddleNumber) {
        this.paddleNumber = paddleNumber;

        if (paddleNumber == 1) {
            this.x = 0;
        }

        if (paddleNumber == 2) {
            this.x = pong.WIDTH - WIDTH;
        }

        this.y = pong.HEIGHT / 2 - this.HEIGHT / 2;
    }

    public void render(Graphics g) {
        g.setColor(Color.WHITE);
        g.fillRect(x, y, WIDTH, HEIGHT);
    }

    public void move(boolean up) {
        int speed = 15;

        if (up) {
            if (y - speed > 0) {
                y -= speed;
            } else {
                y = 0;
            }
        } else {
            if (y + HEIGHT + speed < Pong.pong.HEIGHT) {
                y += speed;
            } else {
                y = Pong.pong.HEIGHT - HEIGHT;
            }
        }
    }

}