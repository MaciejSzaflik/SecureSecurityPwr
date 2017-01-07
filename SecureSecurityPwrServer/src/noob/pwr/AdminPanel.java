package noob.pwr;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextPane;
import javax.swing.JTextArea;
import java.awt.Color;
import java.util.HashMap;

public class AdminPanel extends JFrame {
	
	private static AdminPanel instance;
	public static AdminPanel getInstance()
	{
		if(instance == null)
			instance = new AdminPanel();
		instance.setVisible(true);
		return instance;
	}
	private JTextArea textArea;
	
	public AdminPanel() {
		getContentPane().setBackground(Color.WHITE);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(null);
		
		JLabel lblCurrentConnections = new JLabel("Current connections:");
		lblCurrentConnections.setBounds(0, 0, 119, 14);
		getContentPane().add(lblCurrentConnections);
		
		textArea = new JTextArea();
		textArea.setEditable(false);
		textArea.setBounds(0, 25, 268, 225);
		getContentPane().add(textArea);
	}
	
	public void SetCurrentConnections(HashMap<String,TalkThread> connections)
	{
		StringBuilder stringBuilder = new StringBuilder();
		connections.forEach((k,v) ->  stringBuilder.append(k + " " + (v.isAlive()?"Alive":"Dead")+ "\n"));
		textArea.setText(stringBuilder.toString());
	}
}
