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
import javax.swing.JScrollPane;

public class ClientWindow extends JFrame {
	
	JTextArea mainChatText;
	JTextArea sendText;
	
	public ClientWindow()
	{
		getContentPane().setBackground(Color.WHITE);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(565,300);
		
		getContentPane().setLayout(null);
		
		JButton btnSend = new JButton("Send");
		btnSend.setBorder(new MatteBorder(2, 2, 2, 2, (Color) new Color(227, 227, 227)));
		btnSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ChatClient.instance.WriteMessage(sendText.getText());
				sendText.setText("");
			}
		});
		btnSend.setBounds(0, 216, 549, 45);
		btnSend.setAlignmentX(Component.RIGHT_ALIGNMENT);
		btnSend.setAlignmentY(0.0f);
		btnSend.setBackground(Color.WHITE);
		getContentPane().add(btnSend);
		setVisible(true);
		
		sendText = new JTextArea();
		sendText.setBorder(new MatteBorder(2, 2, 2, 2, (Color) new Color(227, 227, 227)));
		sendText.setBounds(0, 165, 549, 56);
		getContentPane().add(sendText);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBorder(new MatteBorder(1, 1, 1, 1, (Color) Color.LIGHT_GRAY));
		scrollPane.setOpaque(false);
		scrollPane.setBounds(0, 11, 549, 156);
		getContentPane().add(scrollPane);
		
		mainChatText = new JTextArea();
		scrollPane.setViewportView(mainChatText);
		mainChatText.setBorder(new MatteBorder(2, 2, 2, 2, (Color) new Color(227, 227, 227)));
		mainChatText.setEditable(false);
		
		PrintStream printStream = new PrintStream(new AreaOutputStream(mainChatText));
		System.setOut(printStream);
	}
}
