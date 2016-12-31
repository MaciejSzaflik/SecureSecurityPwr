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

public class ListOfUsers extends JFrame {
	private JTable table;
	public ListOfUsers() {
		getContentPane().setBackground(Color.WHITE);
		getContentPane().setLayout(null);
		this.setSize(365,500);
		
		JButton btnMain = new JButton("Main");
		btnMain.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ChatClient.SetVisibleChat();
			}
		});
		btnMain.setBackground(Color.WHITE);
		btnMain.setBounds(0, 0, 349, 52);
		getContentPane().add(btnMain);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 63, 329, 387);
		getContentPane().add(scrollPane);
		
		DefaultTableModel dm = new DefaultTableModel();
	    dm.setDataVector(new Object[][] {}, new Object[] { "Users" });

	    table = new JTable(dm);
	    table.getColumn("Users").setCellRenderer(new ButtonRenderer());
	    table.getColumn("Users").setCellEditor(
	        new ButtonEditor(new JCheckBox()));
		scrollPane.setViewportView(table);
	}
	
	public void SetUsers(String usersNames)
	{
		String[] split = usersNames.split(";");
		Object[][] toSet = new Object[split.length][1];
		System.err.println(split.length);
		for(int i = 0;i<split.length;i++)
			toSet[i] = new Object[]{split[i]};
		
		((DefaultTableModel) table.getModel()).setDataVector(toSet, new Object[] { "Users" });
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

		class ButtonEditor extends DefaultCellEditor {
		  protected JButton button;

		  private String label;

		  private boolean isPushed;

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
		      // 
		      // 
		    	ChatClient.SetVisibleChat();
		      // System.out.println(label + ": Ouch!");
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
