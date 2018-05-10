package tools;
import java.awt.Color;

import javax.swing.JPanel;

public class Node {
	private boolean hasBody = false;
	private boolean hasFood = false;
	private int x;
	private int y;
	public static final Color GREEN = Color.white;
	private JPanel cellPanel;
	
	/*
	 * Constructor for the Cell class Creates a queue for all the thread if thread has multiple
	 * threads running in it
	 */
	public Node(int x, int y) {
		this.x = x;
		this.y = y;
		cellPanel = new JPanel();
		cellPanel.setBackground(GREEN);
		cellPanel.setSize(1, 1);
	}
	
	public JPanel getNodePanel() {
		return cellPanel;
	}
	
	public void setNodePanel(JPanel cellPanel) {
		this.cellPanel = cellPanel;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public boolean isEmpty() {
		return !hasFood && !hasBody;
	}
	
	public boolean hasFood() {
		return hasFood;
	}
	
	public synchronized void placeFood() {
		hasFood = true;
		cellPanel.setBackground(Color.magenta);
	}
	
	/*
	 * breaks the thread out of queue to block
	 */
	public synchronized void beingUsed() {
		hasBody = false;
		hasFood = false;
		cellPanel.setBackground(Color.darkGray);
	}
	
	/*
	 * sets the colour of the snake's HEAD to match the player number or AI.
	 */
	public synchronized void setSnake(Color color) {
		hasBody = true;
		cellPanel.setBackground(color);
	}

	public synchronized void resetColor() {
		hasBody = false;
		hasFood = false;
		cellPanel.setBackground(GREEN);
	}
	
	/*
	 * stops the thread so it cannot be accessed
	 */
	
	
}
