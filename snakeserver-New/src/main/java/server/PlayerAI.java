package server;

import tools.GameBoard;
import tools.Pair;

import java.util.ArrayList;
import java.util.Random;

public class PlayerAI extends Player implements Runnable {
	private Thread mThread;
	public PlayerAI(String name, GameBoard board) {
		super(name, board);
		mThread = new Thread(this);
	}

	@Override
	public void run() {
		do {
		    Random random = new Random();
		    int old = mSnake.getDirection();
		    ArrayList<Integer> directions = new ArrayList<>();
		    for (int i = 0; i < 4; ++i) {
		    	mSnake.setDirection(i);
		    	Pair pair = mSnake.nextPair();
		    	if (!mSnake.contains(pair)) {
		    		directions.add(i);
				}
			}
			mSnake.setDirection(old);
			int ins = old;
		    if (!directions.isEmpty()) {
		    	ins = random.nextInt(directions.size());
			}
		    instruction(ins);
		    mDead = (mBoard.updateSnake(mSnake) & 1) > 0;
		    try {
		        Thread.sleep(800);
            } catch (InterruptedException e) {

            }
        } while (alive());
	}

	@Override
    public void play() {
	    mThread.start();
    }
}
