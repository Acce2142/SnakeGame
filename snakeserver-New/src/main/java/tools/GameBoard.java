package tools;
import server.Buffer;
import server.ServerThread;
import server.Snake;

import java.awt.GridLayout;

import javax.swing.JFrame;

/*
 * producers
 */
public class GameBoard {
    public static int DIE = 0;
    public static int GROW = 1;
    public static int MOVE = 2;
    public static int FOOD = 2;
	public static int HEIGHT = 100;
	public static int WIDTH = 100;
	private JFrame mainWindow;
    private NodeList nodeList = new NodeList(HEIGHT, WIDTH);
    private ServerThread mThread;
    private Buffer mQueue;
	public GameBoard(){
		mainWindow = new JFrame("Snake Game");
		mainWindow.setBounds(0, 0, 1000, 1000);
		mainWindow.setLayout(new GridLayout(100, 100, 1, 1));
		for (int v = 0; v < HEIGHT; v++) {
			for (int h = 0; h < WIDTH; h++) {
				mainWindow.add(nodeList.getNode(v, h).getNodePanel());
			}
		}
		mainWindow.setVisible(true);
		mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mQueue = new Buffer(100);
		mThread = new ServerThread(mQueue);
	}

	public void redraw() {
	    mainWindow.revalidate();
	    mainWindow.repaint();
    }
	synchronized public int updateSnake(Snake snake) throws InterruptedException {
	    Pair next = snake.nextPair();
	    Node node = nodeList.getNode(next);;
	    boolean dead = true;
	    int state = 0;
	    if (node.hasFood()) {
	        state = 2;
	        dead = false;
            nodeList.removeSnake(snake);
	        snake.move(true);
            nodeList.drawSnake(snake);
            nodeList.generateFood();
        }
        if (node.isEmpty()) {
	        dead = false;
            nodeList.removeSnake(snake);
	        snake.move(false);
            nodeList.drawSnake(snake);
        }

        if (dead) {
	        state = 1;
            nodeList.removeSnake(snake);
            System.out.println("player die");
        } else {
        	try {
				mQueue.put(new Runnable() {
					@Override
					public void run() {
						redraw();
					}
				});
			} catch (InterruptedException e) {
				System.out.println("player quit");
			}
			
        }
	    return state;
    }

	public void start() {
	    mThread.start();
    }

	public static void main(String[] args) throws Exception{
	}
}
