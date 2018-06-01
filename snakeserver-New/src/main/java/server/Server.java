package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import tools.GameBoard;

public class Server {
    private ServerThread mServerThread;
    private Thread mLoginThread;
    private Buffer mQueue;
    private GameBoard mBoard;
    private final ExecutorService AIPool = Executors.newFixedThreadPool(4);
    private final ExecutorService PlayerPool = Executors.newFixedThreadPool(1);
    private Server() {
        mQueue = new Buffer(100);
        mServerThread = new ServerThread(mQueue);
        mLoginThread = new Thread(new Runnable() {
            @Override
            public void run() {
                boolean listening= true;
                try (ServerSocket socket = new ServerSocket(8080)) {
                	// while the server is still online.
                	// creates new player when a new client connects with it
                	// server creates a thread to answer the new created client's request
                    while (listening) {
                    	PlayerHuman player = new PlayerHuman("player", mBoard, socket.accept());
                        PlayerPool.submit(player);
                    }
                } catch (IOException e) {
                    System.err.println("server failed");
                    System.exit(1);
                }
            }
        });
        mBoard = new GameBoard();
    }
    private void start() {
        mServerThread.start();
        mLoginThread.start();
        mBoard.start();
        for (int i = 0; i < 5; ++i) {
        	Runnable myAIRunnable = new PlayerAI("AI", mBoard);
        	AIPool.submit(myAIRunnable);
        }
    }

    public static void main(String[] argv) {
        (new Server()).start();
    }
}
