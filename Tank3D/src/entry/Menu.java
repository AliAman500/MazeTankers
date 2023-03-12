package entry;

import java.awt.CardLayout;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Menu extends JFrame {
	
	private static final long serialVersionUID = 1L;
	
	private JPanel menuPanel;
	private Thread gameStarterThread;
	
    public Menu() {
        super("Maze Tankers");
        setSize(1136, 640);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new CardLayout());

        menuPanel = new JPanel();
        menuPanel.setLayout(null);
        
        JTextField textField = new JTextField("", 20);
        textField.setFont(new Font("Calibri", Font.PLAIN, 14));
        textField.setBounds(new Rectangle(getWidth() / 2 - 150, getHeight() / 2 - 12, 300, 24));
        menuPanel.add(textField);
        
        JButton playButton = new JButton("Play!");
        playButton.setBounds(new Rectangle(getWidth() / 2 - 150, getHeight() / 2 + 12, 300, 24));
        menuPanel.add(playButton);
        
        JLabel uLabel = new JLabel("Enter username:");
        uLabel.setBounds(new Rectangle(getWidth() / 2 - 150, getHeight() / 2 - 32, 300, 24));
        menuPanel.add(uLabel);
        
        getContentPane().add(menuPanel, "menu");
        playButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	textField.setEditable(false);
            	playButton.setText("Loading...");
            	playButton.setEnabled(false);
        		gameStarterThread.start();
            }
        });

        gameStarterThread = new Thread(new Runnable() {
        	public void run() {
        		Game.startGame();
        		dispose();
        	}
        });
        
        setVisible(true);
        setResizable(false);
    }
}