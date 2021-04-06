public class FirstPlayer implements Observer {

    public static int score = 0;

    @Override
    public void update() {
        if (Pong.pong.playerWon == 1)
            ++score;
    }

}
