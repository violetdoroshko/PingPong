public class SecondPlayer implements Observer {

    static int score = 0;

    @Override
    public void update() {
        if (Pong.pong.playerWon == 2)
            ++score;
    }
}
