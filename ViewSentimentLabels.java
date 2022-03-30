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
public class ViewSentimentLabels extends JFrame{
	
	JTable table;
	DefaultTableModel dtm;
	JScrollPane jsp;
	Font f1;
	JPanel p1;
	
public ViewSentimentLabels(){
	super("View Sentiment Labels");
	setLayout(new BorderLayout());
	f1 = new Font("Courier New",Font.BOLD,14);
	p1 = new JPanel();
	p1.setBackground(Color.white);
	p1.setLayout(new BorderLayout());
	dtm = new DefaultTableModel(){
		public boolean isCellEditable(int r,int c){
			return false;
		}
	};
	table = new JTable(dtm);
	jsp = new JScrollPane(table);
	table.setFont(f1);
	table.setRowHeight(30);
	dtm.addColumn("Rating");
	dtm.addColumn("Review");
	dtm.addColumn("Deep Learn Sentiment");
	table.getColumnModel().getColumn(0).setPreferredWidth(100);
	table.getColumnModel().getColumn(1).setPreferredWidth(540);
	table.getColumnModel().getColumn(2).setPreferredWidth(150);
	p1.add(jsp,BorderLayout.CENTER);

	add(p1,BorderLayout.CENTER);
}
}