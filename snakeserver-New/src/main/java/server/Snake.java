package server;

import tools.GameBoard;
import tools.Pair;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class Snake {
    static private int[] dx = new int[]{-1, 1, 0, 0};
    static private int[] dy = new int[]{0, 0, -1, 1};
    private int direction;
    private ArrayList<Pair> mBody;
    private Color mColor;
    private Snake(Color color) {
        direction = Player.LEFT;
        mBody = new ArrayList<>();
        mColor = color;
    }

    public static Snake create(int x, int y, int width, int height, int length, Color color) {
        Random random = new Random();
        Snake snake = new Snake(color);
        int curX = x;
        int curY = y;
        for (int i = 0; i < length; ++i) {
            Pair key = new Pair(curY, curX);
            snake.mBody.add(key);
            int k = random.nextInt(4);
            curX = (curX + dx[k] + width) % width;
            curY = (curY + dy[k] + height) % height;
        }
        return snake;
    }

    public void move(boolean grow) {
        Pair newPair = nextPair();
        mBody.add(0, newPair);
        if (!grow)
            mBody.remove(mBody.size() - 1);
    }

    public Pair nextPair() {
        Pair pair = mBody.get(0);
        int y = (pair.getKey1() + dy[direction] + GameBoard.HEIGHT) % GameBoard.HEIGHT;
        int x = (pair.getKey2() + dx[direction] + GameBoard.WIDTH) % GameBoard.WIDTH;
        return new Pair(y, x);
    }

    public void setDirection(int dir) {
        direction = dir;
    }

    public int getDirection() {
        return direction;
    }

    public ArrayList<Pair> getBody() {
        return mBody;
    }

    public Color getColor() {
        return mColor;
    }

    public boolean contains(Pair pair) {
        for (int i = 0; i < mBody.size(); ++i) {
            if (mBody.get(i).equals(pair)) {
                return true;
            }
        }
        return false;
    }
}
