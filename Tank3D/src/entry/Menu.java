package entry;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;

import com.formdev.flatlaf.FlatDarkLaf;

import networking.Client;
import networking.PlayReqPacket;
import networking.User;
import tools.Util;

public class Menu extends JFrame {

	private static final long serialVersionUID = 1L;

	public static JPanel menuPanel;
	public static JPanel roomPanel;
	public static Thread gameStarterThread;
	public static JTable table;
	public static JLabel startingGameLabel;
	public static DefaultTableModel model;

	public static CardLayout cards;
	public static Container contentPane;

	public static Clip bgClip;

	public static String versionString = "Version 1.0.0-2023";
	public static JLabel iconLabelr;

	public Menu() {
		super("Maze Tankers");
		changeLookAndFeel();
		setSize(1136, 640);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new CardLayout());
		Image windowIcon = Toolkit.getDefaultToolkit().getImage("res/textures/icon.png");
		setIconImage(windowIcon);
		try {
			File soundFile = new File("res/audio/commandos_3.wav");
			AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile);
			bgClip = AudioSystem.getClip();
			bgClip.open(audioIn);
			bgClip.loop(Clip.LOOP_CONTINUOUSLY);
			FloatControl gainControl = (FloatControl) bgClip.getControl(FloatControl.Type.MASTER_GAIN);
			float volume = 0.12f;
			float dB = (float) (Math.log(volume) / Math.log(10.0) * 20.0);
			gainControl.setValue(dB);
		} catch (Exception e) {
			e.printStackTrace();
		}

		contentPane = getContentPane();

		cards = (CardLayout) getContentPane().getLayout();

		menuPanel = new JPanel();
		menuPanel.setLayout(null);

		int heightInc = 10;

		JLabel ipLabel = new JLabel("Enter Server IP");
		ipLabel.setBounds(new Rectangle(getWidth() / 2 - 40, (getHeight() / 2 - 32) - 44, 300, 32));
		menuPanel.add(ipLabel);

		JLabel uLabel = new JLabel("Enter Username");
		uLabel.setBounds(new Rectangle(getWidth() / 2 - 45, (getHeight() / 2 - 32) + 20, 300, 32));
		menuPanel.add(uLabel);

		RoundJTextField ipField = new RoundJTextField(20);
		ipField.setText(Client.serverIP);
		ipField.setFont(new Font("Calibri", Font.PLAIN, 16));
		ipField.setBounds(new Rectangle(getWidth() / 2 - 150, (getHeight() / 2 + 8) - 52, 300, 32));
		menuPanel.add(ipField);

		RoundJTextField uField = new RoundJTextField(20);
		uField.setFont(new Font("Calibri", Font.PLAIN, 16));
		uField.setBounds(new Rectangle(getWidth() / 2 - 150, (getHeight() / 2 + 8) + heightInc, 300, 32));
		menuPanel.add(uField);

		JButton playButton = new JButton("Multiplayer");
		playButton.setBounds(new Rectangle(getWidth() / 2 - 150, (getHeight() / 2 + 48) + heightInc, 300, 46));
		menuPanel.add(playButton);

		JButton playSingleButton = new JButton("Test (debugging)");
		playSingleButton.setBounds(new Rectangle(getWidth() / 2 - 150, (getHeight() / 2 + 104) + heightInc, 300, 46));
		menuPanel.add(playSingleButton);

		JButton quitButton = new JButton("Quit!");
		quitButton.setBounds(new Rectangle(getWidth() / 2 - 150, (getHeight() / 2 + 160) + heightInc, 300, 46));
		quitButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(1);
			}

		});
		menuPanel.add(quitButton);

		JLabel version = new JLabel(versionString);
		version.setBounds(new Rectangle(10, (560), 300, 46));
		menuPanel.add(version);

		JLabel loading = new JLabel("");
		loading.setFont(new Font("Calibri", Font.BOLD, 16));
		loading.setForeground(new Color(127, 218, 252));
		loading.setBounds(new Rectangle(getWidth() / 2 - 45, (getHeight() / 2 + 210) + heightInc + 5, 300, 46));
		menuPanel.add(loading);

		JCheckBox box = new JCheckBox("Music");
		box.setSelected(true);
		box.setBounds(new Rectangle(getWidth() - 110, getHeight() - 80, 100, 40));
		menuPanel.add(box);

		JCheckBox rbox = new JCheckBox("Music");
		rbox.setSelected(box.isSelected());
		rbox.setBounds(new Rectangle(getWidth() - 80, getHeight() - 80, 100, 40));

		JCheckBox cbox = new JCheckBox("Lock camera");
		cbox.setSelected(Game.lockCamera);
		cbox.setBounds(new Rectangle(getWidth() - 110, getHeight() - 120, 100, 40));

		box.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				rbox.setSelected(box.isSelected());
				if (!box.isSelected()) {
					bgClip.stop();
				} else {
					bgClip.start();
				}
			}
		});

		cbox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Game.lockCamera = !Game.lockCamera;
				cbox.setSelected(Game.lockCamera);
			}
		});
		menuPanel.add(cbox);

		ImageIcon icon = new ImageIcon("res/textures/menu.png");

		JLabel iconLabel = new JLabel(icon);
		iconLabel.setBounds(new Rectangle(0, 0, 1136, 640));

		menuPanel.add(iconLabel);
		menuPanel.setComponentZOrder(iconLabel, 11);
		getContentPane().add(menuPanel, "menu");

		roomPanel = new JPanel();
		roomPanel.setLayout(null);
		JLabel versionR = new JLabel(versionString);
		versionR.setBounds(new Rectangle(10, (560), 300, 46));
		roomPanel.add(versionR);
		roomPanel.add(rbox);

		rbox.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				if (!rbox.isSelected()) {
					bgClip.stop();
				} else {
					bgClip.start();
				}
			}

		});

		String[] columnNames = { "Waiting for Players..." };
		model = new DefaultTableModel(columnNames, 0);
		table = new JTable(model);
		table.setBounds(new Rectangle(1136 / 2 - 200, 40, 400, 400));

		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setBounds(new Rectangle(1136 / 2 - 200, 40, 400, 400));
		roomPanel.add(scrollPane, BorderLayout.CENTER);

		startingGameLabel = new JLabel();
		startingGameLabel.setFont(new Font("Calibri", Font.PLAIN, startingGameLabel.getFont().getSize()));
		startingGameLabel.setBounds(new Rectangle(1136 / 2 - 40, 440 + 120, 100, 32));

		ImageIcon iconr = new ImageIcon("res/textures/lobby.png");

		iconLabelr = new JLabel(iconr);
		iconLabelr.setBounds(new Rectangle(0, 0, 1136, 640));

		roomPanel.add(iconLabelr);

		roomPanel.add(startingGameLabel);
		roomPanel.setComponentZOrder(iconLabelr, 4);

		playButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Client.serverIP = ipField.getText();

				try {
					Game.client.clientAddress = InetAddress.getLocalHost().getHostAddress();
				} catch (UnknownHostException e1) {
					e1.printStackTrace();
				}
				Game.user = new User(uField.getText(), null, Game.client.clientAddress, Game.client.clientPort, null);
				Game.user.username = Game.user.username.replaceAll("\\s+", "");
				Game.client.sendData(new PlayReqPacket(Game.user.username));
			}
		});

		playSingleButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				ipField.setEnabled(false);
				uField.setEnabled(false);
				playSingleButton.setEnabled(false);
				playButton.setEnabled(false);
				quitButton.setEnabled(false);
				loading.setText("L O A D I N G . . .");
				menuPanel.revalidate();
				menuPanel.repaint();
				gameStarterThread.start();
			}

		});

		gameStarterThread = new Thread(new Runnable() {
			public void run() {
				Game.startGame();
				bgClip.stop();
				dispose();
			}
		}, "Game-Starter-Thread");

		getContentPane().add(roomPanel, "room");

		cards.show(getContentPane(), "menu");

		setVisible(true);
		setResizable(false);

		uField.requestFocus();
	}

	public static void clearRows() {
		model.setRowCount(0);
	}

	public static void addUsername(String text) {
		int count = model.getRowCount();
		model.addRow(new Object[] { "Player " + (count + 1) + ": " + text });
		roomPanel.revalidate();
		roomPanel.repaint();
	}

	public static void changeLookAndFeel() {
		try {
			UIManager.setLookAndFeel(new FlatDarkLaf());
			UIManager.put("Component.focusWidth", 0);
			UIManager.put("Component.arc", 40);
			UIManager.put("Table.arc", 40);
			UIManager.put("Button.arc", 40);
			UIManager.put("CheckBox.arc", 40);
			UIManager.put("TextField.arc", 40);
			UIManager.put("Button.background", new Color(31, 34, 42));
			UIManager.put("Button[Enabled].background", new Color(31, 34, 42));
			UIManager.put("Button.disabledBackground", new Color(31, 34, 42));
			UIManager.put("TextField.background", new Color(31, 34, 42));
			UIManager.put("TextField[Enabled].background", new Color(31, 34, 42));
			UIManager.put("TextField.disabledBackground", new Color(31, 34, 42));
			UIManager.put("Component.focusColor", new Color(127, 218, 252));
			UIManager.put("Button.font", new Font("Calibri", Font.PLAIN, 14));
			UIManager.put("Panel.background", new Color(41, 44, 52));
			UIManager.put("Table.background", new Color(40, 40, 40));
			UIManager.put("Table.font", new Font(Font.SANS_SERIF, Font.PLAIN, 12));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}