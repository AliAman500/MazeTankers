package entry;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Font;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.util.LinkedList;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class EndScreen extends JFrame {

	private static final long serialVersionUID = 1L;

	public static JTable table;
	public static DefaultTableModel model;
	public static JPanel roomPanel = new JPanel();
	
	public EndScreen(LinkedList<String> users) {
		super("Maze Tankers");
		Menu.changeLookAndFeel();
		setSize(1136, 640);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new CardLayout());
		Image windowIcon = Toolkit.getDefaultToolkit().getImage("res/textures/icon.png");
        setIconImage(windowIcon);

		ImageIcon icon = new ImageIcon("res/textures/menu.png");

		JLabel iconLabel = new JLabel(icon);
		iconLabel.setBounds(new Rectangle(0, 0, 1136, 640));
		
		roomPanel.setLayout(null);
		JLabel versionR = new JLabel(Menu.versionString);
		versionR.setBounds(new Rectangle(10, (560), 300, 46));
		roomPanel.add(versionR);
		
		String[] columnNames = { "Leaderboard" };
		model = new DefaultTableModel(columnNames, 0);
		table = new JTable(model);
		table.setBounds(new Rectangle(1136 / 2 - 200, 40, 400, 400));

		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setBounds(new Rectangle(1136 / 2 - 200, 40, 400, 400));
		roomPanel.add(scrollPane, BorderLayout.CENTER);
		
		ImageIcon iconr = new ImageIcon("res/textures/endScreen.png");
		JLabel iconLabelr = new JLabel();
		iconLabelr = new JLabel(iconr);
		iconLabelr.setBounds(new Rectangle(0, 0, 1136, 640));

		roomPanel.add(iconLabelr);

		getContentPane().add(roomPanel, "room");

		for(String username : users) {
			addUsername(username);
		}
		
		setVisible(true);
		setResizable(false);
	}
	public static void addUsername(String text) {
		int count = model.getRowCount();
		model.addRow(new Object[] { numberSuffix(count + 1) + " place: " + text });
		roomPanel.revalidate();
		roomPanel.repaint();
	}

	public static String numberSuffix(int num) {
	    if (num >= 11 && num <= 13) {
	        return num + "th";
	    }
	    
	    switch (num % 10) {
	        case 1:
	            return num + "st";
	        case 2:
	            return num + "nd";
	        case 3:
	            return num + "rd";
	        default:
	            return num + "th";
	    }
	}
}
