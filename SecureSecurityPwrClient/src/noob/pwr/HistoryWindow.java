package noob.pwr;

import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;

public class HistoryWindow extends JFrame {
	public HistoryWindow(String[] informations) {
		getContentPane().setLayout(null);
		this.setVisible(true);
		this.setSize(445, 300);
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 11, 414, 239);
		getContentPane().add(scrollPane);
		
		JTextArea textArea = new JTextArea();
		scrollPane.setViewportView(textArea);
		textArea.setText(parseInformation(informations));
	}
	
	private String parseInformation(String[] data)
	{
		if(data == null)
			return "";
		
		StringBuilder sb = new StringBuilder();
		for(int i = 0;i<data.length;i++)
		{
			sb.append(data[i]);
			sb.append("\n");
		}
		return sb.toString();
	}
}
