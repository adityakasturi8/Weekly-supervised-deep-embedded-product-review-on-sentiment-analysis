package com;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import javax.swing.JPanel;
import java.util.ArrayList;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.JFrame;
public class ViewVector extends JFrame{
	
	JTable table;
	MyTableModel dtm;
	JScrollPane jsp;
	Font f1;
	JPanel p1;
	
public ViewVector(){
	super("View Vector");
	//clearTable();
	setLayout(new BorderLayout());
	f1 = new Font("Times New Roman",Font.BOLD,13);
	p1 = new JPanel();
	p1.setBackground(Color.white);
	p1.setLayout(new BorderLayout());
	dtm = new MyTableModel(){
		public boolean isCellEditable(int r,int c){
			return false;
		}
	};
	table = new JTable(dtm);
	table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
	jsp = new JScrollPane(table);
	table.setFont(f1);
	table.setRowHeight(30);
	p1.add(jsp,BorderLayout.CENTER);

	add(p1,BorderLayout.CENTER);
	showVector();
}
public void showVector(){
	clearTable();
	for(int i=0;i<CNNFeatureVector.uniquewords.size();i++){
		dtm.addColumn(CNNFeatureVector.uniquewords.get(i));
		table.getColumnModel().getColumn(i).setPreferredWidth(100);
	}
	for(int i=0;i<CNNFeatureVector.vector.size();i++){
		double d[] = CNNFeatureVector.vector.get(i);
		String arr[] = new String[d.length];
		for(int j=0;j<d.length;j++){
			arr[j] = Double.toString(d[j]);
		}
		dtm.addRow(arr);
	}
}
public void clearTable(){
	for(int i=table.getRowCount()-1;i>=0;i--){
		dtm.removeRow(i);
	}
	for(int i=table.getColumnCount()-1;i>=0;i--){
		dtm.removeColumn(i);
	}
}
}
class MyTableModel extends DefaultTableModel {
    public void removeColumn(int column){
		columnIdentifiers.remove(column);
		for(Object row: dataVector){
			((java.util.Vector) row).remove(column);
		}
		fireTableStructureChanged();
    }	
}