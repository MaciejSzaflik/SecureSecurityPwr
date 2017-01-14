package noob.pwr;

import javax.swing.JFrame;
import javax.swing.JButton;
import java.awt.BorderLayout;
import javax.swing.BoxLayout;
import java.awt.Component;
import javax.swing.JTextField;
import javax.swing.JFormattedTextField;
import java.awt.Color;
import java.awt.Font;
import java.awt.SystemColor;
import javax.swing.JLabel;
import javax.swing.JPasswordField;

import java.awt.event.ActionListener;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.awt.event.ActionEvent;

public class LoginWindow extends JFrame {
	public JTextField loginField;
	public JPasswordField passwordField;
	public JLabel lblStatus;
	public LoginWindow() {
		getContentPane().setBackground(Color.WHITE);
		getContentPane().setLayout(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(446,311);
		
		JButton btnLogin = new JButton("Login");
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(!loginField.getText().isEmpty() && !passwordField.getText().isEmpty())
				{
					try {
						ChatClient.instance.myNick = loginField.getText();
						String message = loginField.getText() + ":";
						MessageDigest sha = MessageDigest.getInstance("SHA-1");
						String password = new String(sha.digest(passwordField.getText().getBytes()));
						ChatClient.instance.WriteMessage(ComConst.PASS_AND_NICK, message+password,ComConst.EMPTY);
					} catch (NoSuchAlgorithmException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
			}
		});
		btnLogin.setBackground(Color.WHITE);
		btnLogin.setBounds(92, 214, 240, 47);
		btnLogin.setAlignmentX(Component.CENTER_ALIGNMENT);
		btnLogin.setAlignmentY(Component.BOTTOM_ALIGNMENT);
		getContentPane().add(btnLogin);
		
		loginField = new JTextField();
		loginField.setBackground(SystemColor.inactiveCaptionBorder);
		loginField.setFont(new Font("Tahoma", Font.PLAIN, 25));
		loginField.setBounds(27, 70, 380, 52);
		getContentPane().add(loginField);
		loginField.setColumns(10);
		
		passwordField = new JPasswordField();
		passwordField.setBackground(SystemColor.inactiveCaptionBorder);
		passwordField.setFont(new Font("Tahoma", Font.PLAIN, 25));
		passwordField.setColumns(10);
		passwordField.setBounds(27, 133, 380, 52);
		getContentPane().add(passwordField);
		
		lblStatus = new JLabel("Status:");
		lblStatus.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblStatus.setBounds(10, 11, 229, 22);
		getContentPane().add(lblStatus);
		
		lblStatus.setText("Trying to connect..");
		lblStatus.setForeground(Color.RED);
		
		JButton button = new JButton("Try to connect");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ChatClient.instance.StartConnection();
			}
		});
		button.setBackground(Color.WHITE);
		button.setAlignmentY(1.0f);
		button.setAlignmentX(0.5f);
		button.setBounds(313, 0, 117, 22);
		getContentPane().add(button);
	}
}
