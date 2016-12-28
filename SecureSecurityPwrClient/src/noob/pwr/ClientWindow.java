package noob.pwr;

import javax.swing.JFrame;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTextArea;
import java.io.*;
import java.awt.Color;
import javax.swing.BoxLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import javax.swing.border.MatteBorder;
import javax.swing.UIManager;

public class ClientWindow extends JFrame {
	
	JTextArea mainChatText;
	JTextArea sendText;
	
	public ClientWindow()
	{
		getContentPane().setBackground(Color.WHITE);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(565,300);
		
		JButton btnNewButton = new JButton("Connect");
		btnNewButton.setBorder(new MatteBorder(2, 2, 2, 2, (Color) new Color(227, 227, 227)));
		btnNewButton.setBounds(0, 216, 272, 45);
		btnNewButton.setBackground(Color.WHITE);
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ChatClient.instance.StartConnection(new String[]{"localhost","5000"});
			}
		});
		getContentPane().setLayout(null);
		getContentPane().add(btnNewButton);
		
		JButton btnSend = new JButton("Send");
		btnSend.setBorder(new MatteBorder(2, 2, 2, 2, (Color) new Color(227, 227, 227)));
		btnSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ChatClient.instance.WriteMessage(sendText.getText());
				sendText.setText("");
			}
		});
		btnSend.setBounds(271, 216, 278, 45);
		btnSend.setAlignmentX(Component.RIGHT_ALIGNMENT);
		btnSend.setAlignmentY(0.0f);
		btnSend.setBackground(Color.WHITE);
		getContentPane().add(btnSend);
		
		mainChatText = new JTextArea();
		mainChatText.setBorder(new MatteBorder(2, 2, 2, 2, (Color) new Color(227, 227, 227)));
		mainChatText.setEditable(false);
		mainChatText.setBounds(0, 0, 549, 167);
		getContentPane().add(mainChatText);
		setVisible(true);
		
		PrintStream printStream = new PrintStream(new AreaOutputStream(mainChatText));
		
		sendText = new JTextArea();
		sendText.setBorder(new MatteBorder(2, 2, 2, 2, (Color) new Color(227, 227, 227)));
		sendText.setBounds(0, 165, 549, 56);
		getContentPane().add(sendText);
		System.setOut(printStream);
		System.setErr(printStream);
	
	}
}
