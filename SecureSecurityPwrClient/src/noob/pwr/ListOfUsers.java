package noob.pwr;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JScrollPane;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.JLabel;
import java.awt.Font;

public class ListOfUsers extends JFrame {
	private JTable table;
	public JLabel nameLabel;
	public ListOfUsers() {
		getContentPane().setBackground(Color.WHITE);
		getContentPane().setLayout(null);
		this.setSize(365,500);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JButton btnMain = new JButton("Main");
		btnMain.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ChatClient.instance.SetVisibleChat("broadcast");
			}
		});
		btnMain.setBackground(Color.WHITE);
		btnMain.setBounds(0, 29, 349, 23);
		getContentPane().add(btnMain);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 63, 329, 387);
		getContentPane().add(scrollPane);
		
		DefaultTableModel dm = new DefaultTableModel();
	    dm.setDataVector(new Object[][] { {  },
	        { } }, new Object[] { });

	    table = new JTable(dm);
		scrollPane.setViewportView(table);
		
		nameLabel = new JLabel("New label");
		nameLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
		nameLabel.setBounds(10, 4, 185, 23);
		getContentPane().add(nameLabel);
	}
	
	public void SetUsers(String usersNames)
	{
		String[] split = usersNames.split(":");
		Object[][] toSet = new Object[split.length][2];
		
		for(int i = 0;i<split.length;i++)
		{
			toSet[i] = new Object[]{split[i],split[i] + ": History"};
		}
		
		((DefaultTableModel) table.getModel()).setDataVector(toSet, new Object[] { "Users","Hist"});
		table.getColumn("Users").setCellRenderer(new ButtonRenderer());
	    table.getColumn("Users").setCellEditor(
	        new ButtonEditor(new JCheckBox()));
		((DefaultTableModel) table.getModel()).fireTableDataChanged();
		
		table.getColumn("Hist").setCellRenderer(new ButtonRenderer());
	    table.getColumn("Hist").setCellEditor(
	        new HistoryButton(new JCheckBox()));
		((DefaultTableModel) table.getModel()).fireTableDataChanged();
	}
	
	class ButtonRenderer extends JButton implements TableCellRenderer {

		private static final long serialVersionUID = 1L;

		public ButtonRenderer() {
		  }

		  public Component getTableCellRendererComponent(JTable table, Object value,
		      boolean isSelected, boolean hasFocus, int row, int column) {
		    setText((value == null) ? "" : value.toString());
		    return this;
		  }
		}
		class HistoryButton extends ButtonEditor {

			public HistoryButton(JCheckBox checkBox) {
				super(checkBox);
				// TODO Auto-generated constructor stub
			}
			
		    public Object getCellEditorValue() {
			    if (isPushed) {	
			    	String[] value = label.split(":");
			    	ChatClient.instance.WriteMessage(ComConst.HISTORY,value[0],ComConst.EMPTY);
			    }
			    isPushed = false;
			    return new String(label);
			  }
		
		}
		
		class ButtonEditor extends DefaultCellEditor {
		  protected JButton button;

		  protected String label;

		  protected boolean isPushed;

		  public ButtonEditor(JCheckBox checkBox) {
		    super(checkBox);
		    button = new JButton();
		    button.setBackground(Color.WHITE);
		    button.addActionListener(new ActionListener() {
		      public void actionPerformed(ActionEvent e) {
		        fireEditingStopped();
		      }
		    });
		  }

		  public Component getTableCellEditorComponent(JTable table, Object value,
		      boolean isSelected, int row, int column) {

		    label = (value == null) ? "" : value.toString();
		    button.setText(label);
		    isPushed = true;
		    return button;
		  }

		  public Object getCellEditorValue() {
		    if (isPushed) {	
		    	ChatClient.instance.SetVisibleChat(label);
		    }
		    isPushed = false;
		    return new String(label);
		  }

		  public boolean stopCellEditing() {
		    isPushed = false;
		    return super.stopCellEditing();
		  }

		  protected void fireEditingStopped() {
		    super.fireEditingStopped();
		  }
	}
}
