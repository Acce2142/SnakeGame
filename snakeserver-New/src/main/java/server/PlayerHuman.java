package server;

import tools.GameBoard;
import tools.MapDBUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class PlayerHuman extends Player implements Runnable {
	private PrintWriter mOut;
	private BufferedReader mIn;
	private MapDBUtil mDb;

	private Thread mPlayThread;
	private boolean mStarted;
	private AtomicInteger mCurrentIns;

	public PlayerHuman(String name, GameBoard board, Socket socket) {
		super(name, board);
		
		mDb = new MapDBUtil();
		try {
			mOut = new PrintWriter(socket.getOutputStream(), true);
			mIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		} catch (IOException e) {
			System.err.println("IO Error");
			System.exit(1);
		}

		mCurrentIns = new AtomicInteger(Player.LEFT);
		mPlayThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (!mDead) {
                    try {
                        if (mStarted && !mDead) {
                            instruction(mCurrentIns.get());
                            int state = mBoard.updateSnake(mSnake);
                            mDead = (state & 1) > 0;
                            if ((state & 2) > 0) {
                                mOut.println("Score");
                            }
                            if (mDead) {
                                mOut.println("Dead");
                            }
                        }
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                    	
                    }
                }
            }
        });
		mStarted = false;
	}


	@Override
	public void run() {
		
		try {
		    mPlayThread.start();
			String inputLine, outputLine;
			while ((inputLine = mIn.readLine()) != null) {
			    if (mDead) continue;
                if (inputLine.startsWith("Login:")) {
                    String username = inputLine.substring(7);
                    String password = mIn.readLine().substring(10);
                    System.out.println("username = " + username);
                    System.out.println("password = " + password);
                    boolean login = !username.isEmpty() & !password.isEmpty() 
                    		& mDb.getPlayerPassword(username).equals(password);
                    System.out.println(!username.isEmpty() & !password.isEmpty() 
                    		& mDb.getPlayerPassword(username).equals(password));
                    if (login) {
                        mStarted = true;
                        mOut.println("Login: success");
                    } else {
                        mOut.println("Login: failed");
                    }
                }

                if (inputLine.startsWith("Register:")) {
                    String username = inputLine.substring(10);
                    String password = mIn.readLine().substring(10);
                    boolean exist = !username.isEmpty() & !password.isEmpty()
                            & mDb.getPlayerPassword(username) != null;
                    if (!exist) {
                        mDb.storePlayer(username, password);
                        mOut.println("Register: success");
                    } else {
                        mOut.println("Register: failed");
                    }
                }
                if (inputLine.startsWith("Instruction:") && mStarted) {
                    final String instructions = inputLine.substring(13);
                    if (instructions.equals("UP")) {
                        mCurrentIns.set(Player.UP);
                    } else if (instructions.equals("DOWN")) {
                        mCurrentIns.set(Player.DOWN);
                    } else if (instructions.equals("LEFT")) {
                        mCurrentIns.set(Player.LEFT);
                    } else if (instructions.equals("RIGHT")) {
                        mCurrentIns.set(Player.RIGHT);
                    }
                }
			}
		} catch (IOException e) {
			System.out.println("player quit");
		} catch (IllegalThreadStateException e){
			System.out.println(e.getMessage());
			System.err.println("client error, please restart your client");
		}
	}
	@Override
    public void play() {
		
    }
}
