package client;

import java.awt.EventQueue;

import javax.swing.JFrame;
import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JPasswordField;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JButton;

public class Client implements ActionListener {

	private static final String COMMAND_LOGIN = "Login";
	private static final String COMMAND_REGISTER = "Register";
	
	private JFrame mLoginFrame;
	private JFrame mDisplayFrame;
	private JTextField tfName;
	private JPasswordField tfPassword;
	private JButton btnLogin;
	private JButton btnRegister;
	private JLabel message;
	private JLabel mDisplayMessage;
	private JLabel mScoreMessage;

	private PrintWriter mOut;
	private BufferedReader command;

	private boolean mDead = false;
	private int mScore;
	/**
	 * Create the application.
	 */
	public Client() {
		initializeLogin();
		initializeDisplay();

		try {
            Socket socket = new Socket(InetAddress.getByName(null), 8080);
            mOut = new PrintWriter(socket.getOutputStream(), true);
            command = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		} catch (UnknownHostException e) {
		    System.err.println("Don't know about host");
		    System.exit(1);
        } catch (IOException e) {
		    System.err.println("I/O error Start socket");
		    System.exit(1);
        }

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                String fromServer;
                try {
                    while ((fromServer = command.readLine()) != null) {
                        if (fromServer.equals("Login: success")) {
                            mLoginFrame.setVisible(false);
                            mDisplayFrame.setVisible(true);
                        }
                        if (fromServer.equals("Login: failed")) {
                            message.setText("Login failed");
                        }
                        if (fromServer.equals("Register: success")) {
                            message.setText("Register succeed");
                        }
                        if (fromServer.equals("Register: failed")) {
                            message.setText("Register failed");
                        }
                        if (fromServer.equals("Score")) {
                            mScore ++;
                            mScoreMessage.setText("Score: " + mScore);
                        }
                        if (fromServer.equals("Dead")) {
                            mDead = true;
                            mDisplayMessage.setText("You are dead.");
                        }
                    }
                } catch (IOException e) {
                    System.err.println("I/O Error");
                }
            }
        });
		mScore = 0;
		thread.start();
		mDisplayFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

	
	private void initializeLogin() {
		mLoginFrame = new JFrame("Snake Login");
		mLoginFrame.setBounds(100, 100, 450, 300);
		mLoginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mLoginFrame.getContentPane().setLayout(new BorderLayout(0, 0));
		
		JPanel panel = new JPanel();
		mLoginFrame.getContentPane().add(panel, BorderLayout.CENTER);
		panel.setLayout(null);
		
		tfName = new JTextField();
		tfName.setBounds(193, 81, 115, 21);
		panel.add(tfName);
		tfName.setColumns(10);
		
		JLabel lblNewLabel = new JLabel("Name");
		lblNewLabel.setBounds(129, 84, 54, 15);
		panel.add(lblNewLabel);
		
		JLabel label = new JLabel("Password");
		label.setBounds(111, 122, 72, 15);
		panel.add(label);
		
		tfPassword = new JPasswordField();
		tfPassword.setColumns(10);
		tfPassword.setBounds(193, 119, 115, 21);
		panel.add(tfPassword);
		
		message = new JLabel("",JLabel.CENTER);
		message.setBounds(93, 207, 271, 21);
		panel.add(message);
		
		btnLogin = new JButton(COMMAND_LOGIN);
		btnLogin.setBounds(111, 169, 81, 23);
		panel.add(btnLogin);
		btnLogin.addActionListener(this);
		
		btnRegister = new JButton(COMMAND_REGISTER);
		btnRegister.setBounds(203, 169, 105, 23);
		panel.add(btnRegister);
		btnRegister.addActionListener(this);
	}

	private void initializeDisplay() {
        mDisplayFrame = new JFrame("Command");
        mDisplayFrame.setBounds(100, 100, 300, 300);
        mDisplayFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mDisplayFrame.getContentPane().setLayout(new BorderLayout(0, 0));

        JPanel panel = new JPanel();
        mDisplayFrame.getContentPane().add(panel, BorderLayout.CENTER);
        panel.setLayout(null);

        JLabel label = new JLabel("Press up/down/left/right to control", JLabel.CENTER);
        label.setBounds(20, 50, 260, 30);
        panel.add(label);

        mDisplayMessage = new JLabel("", JLabel.CENTER);
        mDisplayMessage.setBounds(100, 100, 80, 40);
        panel.add(mDisplayMessage);

        mScoreMessage = new JLabel("Score: " + mScore, JLabel.CENTER);
        mScoreMessage.setBounds(40, 150, 220, 40);
        panel.add(mScoreMessage);

        mDisplayFrame.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (mDead) return;
                if (e.getKeyCode() == KeyEvent.VK_UP) {
                    mOut.println("Instruction: UP");
                    mDisplayMessage.setText("UP");
                } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    mOut.println("Instruction: DOWN");
                    mDisplayMessage.setText("DOWN");
                } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                    mOut.println("Instruction: LEFT");
                    mDisplayMessage.setText("LEFT");
                } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    mOut.println("Instruction: RIGHT");
                    mDisplayMessage.setText("RIGHT");
                }
            }
            @Override
            public void keyReleased(KeyEvent e) {

            }
        });
    }
	@Override
	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();
		switch (command) {
			case COMMAND_LOGIN:
			    mOut.println("Login: " + tfName.getText());
			    mOut.println("Password: " + String.valueOf(tfPassword.getPassword()));
				break;
			case COMMAND_REGISTER:
			    mOut.println("Register: " + tfName.getText());
			    mOut.println("Passward: " + String.valueOf(tfPassword.getPassword()));
				break;
			default:
				break;
		}
	}

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
                    Client window = new Client();
					window.mLoginFrame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
