package entry;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Container;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;

import networking.PlayReqPacket;
import networking.User;

public class Menu extends JFrame {
	
	private static final long serialVersionUID = 1L;
	
	public static JPanel menuPanel;
	public static JPanel roomPanel;
	public static Thread gameStarterThread;
	public static JTable table;
	public static DefaultTableModel model;
	
	public static CardLayout cards;
	public static Container contentPane;
	
    public Menu() {
        super("Maze Tankers");
        windowsLookFeel();
        setSize(1136, 640);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new CardLayout());

        contentPane = getContentPane();
        
        cards = (CardLayout) getContentPane().getLayout();
        
        menuPanel = new JPanel();
        menuPanel.setLayout(null);
        
        JTextField textField = new JTextField("", 20);
        textField.setFont(new Font("Calibri", Font.PLAIN, 14));
        textField.setBounds(new Rectangle(getWidth() / 2 - 150, getHeight() / 2 - 12, 300, 24));
        menuPanel.add(textField);
        
        JButton playButton = new JButton("Play!");
        playButton.setBounds(new Rectangle(getWidth() / 2 - 150, getHeight() / 2 + 12, 300, 24));
        menuPanel.add(playButton);

        JButton playSingleButton = new JButton("Single Player");
        playSingleButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                textField.setEnabled(false);
                playSingleButton.setText("Loading...");
                playSingleButton.setEnabled(false);
                playButton.setEnabled(false);
                gameStarterThread.start();
            }

        });
        playSingleButton.setBounds(new Rectangle(getWidth() / 2 - 150, getHeight() / 2 + 40, 300, 24));
        menuPanel.add(playSingleButton);
        
        JLabel uLabel = new JLabel("Enter username:");
        uLabel.setBounds(new Rectangle(getWidth() / 2 - 150, getHeight() / 2 - 32, 300, 24));
        menuPanel.add(uLabel);
        
        getContentPane().add(menuPanel, "menu");
        
        javaLookFeel();
        
        roomPanel = new JPanel();
        roomPanel.setLayout(null);
        String[] columnNames = {"Waiting for Players..."};
        model = new DefaultTableModel(columnNames, 0);
        table = new JTable(model);
        table.setBounds(new Rectangle(1136/2 - 200, 40, 400, 400));

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(new Rectangle(1136/2 - 200, 40, 400, 400));
        roomPanel.add(scrollPane, BorderLayout.CENTER);
        
        playButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	Game.user = new User(textField.getText(), Game.client.clientAddress, Game.client.clientPort, null);
            	Game.client.sendData(new PlayReqPacket(Game.user.username));
            }
        });

        gameStarterThread = new Thread(new Runnable() {
        	public void run() {
        		Game.startGame();
        		dispose();
        	}
        });
        
        
        getContentPane().add(roomPanel, "room");
        
        cards.show(getContentPane(), "menu");
        
        setVisible(true);
        setResizable(false);
    }
    
    public static void clearRows() {
    	model.setRowCount(0);
    }
    
    public static void addUsername(String text) {
        int count = model.getRowCount();
        model.addRow(new Object[] {"Player " + (count + 1) + ": " + text});
        roomPanel.revalidate();
        roomPanel.repaint();
    }
    
    public static void windowsLookFeel() {
    	try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
    public static void javaLookFeel() {
    	 try {
 			UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
 		} catch (Exception e) {
 			e.printStackTrace();
 		}
    }
}