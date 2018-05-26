package tools;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;

import server.Snake;

public class NodeList {

	final private ConcurrentMap<Pair, Node> gameMap = new ConcurrentHashMap<>();
	final private int height;
	final private int width;
	public NodeList(int h, int w) {
		height = h;
		width = w;
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				Pair key = new Pair(i,j);
				gameMap.put(key, new Node(i, j));
			}
		}

		for (int i = 0; i < 3; ++i)
			generateFood();
	}

	public Node getNode(int x, int y) {
		Pair key = new Pair(y, x);
		return getNode(key);
	}

	public Node getNode(Pair key) {
	    return gameMap.get(key);
    }

    public void removeSnake(Snake snake) {
	    CopyOnWriteArrayList<Pair> body = snake.getBody();
        for (int i = 0; i < body.size(); ++i) {
            getNode(body.get(i)).resetColor();
        }
    }

    public void drawSnake(Snake snake) {
        CopyOnWriteArrayList<Pair> body = snake.getBody();
        for (int i = 0; i < body.size(); ++i) {
            getNode(body.get(i)).setSnake(snake.getColor());
        }
    }

	public void generateFood() {
	    Random random = new Random();
	    while (true) {
	        int x = random.nextInt(width);
	        int y = random.nextInt(height);
	        Node node = getNode(x, y);
	        if (node.isEmpty()) {
	            node.placeFood();
	            break;
            }
        }
	}

	public void updateNodeList(int snakeLength, Pair snakeHead){
		for(int i = 0; i <snakeLength; i++){
			Pair biKey= new Pair(snakeHead.getKey1(),snakeHead.getKey2()+i);
			gameMap.get(biKey).beingUsed();
		}
	}
}
