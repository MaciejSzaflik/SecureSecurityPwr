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
import javax.swing.JLabel;
import java.awt.Font;

public class ClientWindow extends JFrame {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JTextArea mainChatText;
	JTextArea sendText;
	JLabel nameLabel;
	String user;
	
	public ClientWindow(String user)
	{
		this.user = user;
		getContentPane().setBackground(Color.WHITE);
		this.setSize(565,336);
		
		getContentPane().setLayout(null);
		
		JButton btnSend = new JButton("Send");
		btnSend.setBorder(new MatteBorder(2, 2, 2, 2, (Color) new Color(227, 227, 227)));
		btnSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				AppendTextToMain(ChatClient.instance.myNick + ": " + sendText.getText());
				if(user.equals("broadcast"))
				{
					ChatClient.instance.WriteMessage(ComConst.BROADCAST,ComConst.EMPTY,sendText.getText());
				}
				else
				{
					ChatClient.instance.WriteMessage(ComConst.WHISPER,user,sendText.getText());
				}	
				sendText.setText("");
			}
		});
		btnSend.setBounds(0, 252, 549, 45);
		btnSend.setAlignmentX(Component.RIGHT_ALIGNMENT);
		btnSend.setAlignmentY(0.0f);
		btnSend.setBackground(Color.WHITE);
		getContentPane().add(btnSend);
		setVisible(true);
		
		sendText = new JTextArea();
		sendText.setBorder(new MatteBorder(2, 2, 2, 2, (Color) new Color(227, 227, 227)));
		sendText.setBounds(0, 195, 549, 56);
		getContentPane().add(sendText);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBorder(new MatteBorder(1, 1, 1, 1, (Color) Color.LIGHT_GRAY));
		scrollPane.setOpaque(false);
		scrollPane.setBounds(0, 41, 549, 156);
		getContentPane().add(scrollPane);
		
		mainChatText = new JTextArea();
		scrollPane.setViewportView(mainChatText);
		mainChatText.setBorder(new MatteBorder(2, 2, 2, 2, (Color) new Color(227, 227, 227)));
		mainChatText.setEditable(false);
		
		nameLabel = new JLabel(ChatClient.instance.myNick + " -> " + user);
		nameLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
		nameLabel.setBounds(0, 0, 297, 30);
		getContentPane().add(nameLabel);
	}
	
	public void AppendTextToMain(String toAppend)
	{
		mainChatText.append(System.lineSeparator() + toAppend);
	}
}
