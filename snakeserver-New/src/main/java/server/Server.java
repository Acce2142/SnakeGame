package server;

import tools.GameBoard;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private ServerThread mServerThread;
    private Thread mLoginThread;
    private Buffer mQueue;
    private GameBoard mBoard;
    private final ExecutorService pool = Executors.newFixedThreadPool(100);
    private Server() {
        mQueue = new Buffer(100);
        mServerThread = new ServerThread(mQueue);
        mLoginThread = new Thread(new Runnable() {
            @Override
            public void run() {
                boolean listening= true;
                try (ServerSocket socket = new ServerSocket(8080)) {
                    while (listening) {
                    	pool.submit(new PlayerHuman("Human", mBoard, socket.accept()));
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
        	pool.submit(myAIRunnable);
        }
    }

    public static void main(String[] argv) {
        (new Server()).start();
    }
}
