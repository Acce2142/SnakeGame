package server;

import tools.GameBoard;

import java.awt.*;
import java.util.Random;

public abstract class Player {
	//UP = 0, DOWN = 1, LEFT = 2, RIGHT = 3
	static protected int UP = 0;
	static protected int DOWN = 1;
	static protected int LEFT = 2;
	static protected int RIGHT = 3;
	static protected int LENGTH = 10;

	final protected String mName;
	final protected Snake mSnake;
	final protected GameBoard mBoard;
	protected boolean mDead;
	
	@SuppressWarnings("static-access")
	public Player(String name, GameBoard board) {
		mName = name;
		mBoard = board;
		Random random = new Random();
		int x = random.nextInt(mBoard.WIDTH);
		int y = random.nextInt(mBoard.HEIGHT);
		Color color = new Color(random.nextFloat(), random.nextFloat(), random.nextFloat());
		mSnake = Snake.create(x, y, mBoard.WIDTH, mBoard.HEIGHT, LENGTH, color);
		mDead = false;
	}

	public String getName() {
		return mName;
	}

	public void instruction(int direction) {
	    int currentDirection = mSnake.getDirection();
	    if (currentDirection / 2 == direction / 2) return;
	    mSnake.setDirection(direction);
    }

	public abstract void play();

	public synchronized boolean alive() {
	    return !mDead;
    }
}
