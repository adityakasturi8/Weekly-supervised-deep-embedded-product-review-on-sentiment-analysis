package com;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Color;
import javax.swing.JButton;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.File;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.JFileChooser;
import java.awt.Cursor;
import com.jd.swing.custom.component.panel.HeadingPanel;
import com.jd.swing.util.PanelType;
import com.jd.swing.util.Theme;
import java.awt.Dimension;
import org.jfree.ui.RefineryUtilities;
public class DeepLearning extends JFrame{
	JLabel l1;
	JPanel p1,p2,p3;
	Font f1;
	JScrollPane jsp;
	Login login;
	JButton b1,b2,b3,b4,b5,b6,b7;
	JFileChooser chooser;
	DefaultTableModel dtm;
	JTable table;
	ArrayList<String[]> reviews = new ArrayList<String[]>();
	ArrayList<Labels> labels = new ArrayList<Labels>();
public DeepLearning(Login log){
	super("WDE-CNN");
	login = log;
	p1 = new HeadingPanel("Project Title",Theme.GLOSSY_OLIVEGREEN_THEME);
	p1.setPreferredSize(new Dimension(600,50));
	l1 = new JLabel("<html><body><center>Weakly-supervised Deep Embedding for Product Review<br/>Sentiment Analysis</center></body></html>".toUpperCase());
	l1.setFont(new Font("Courier New",Font.BOLD,18));
	l1.setForeground(Color.white);
	p1.add(l1);
	getContentPane().add(p1,BorderLayout.NORTH);

	f1 = new Font("Courier New",Font.BOLD,14);

	p2 = new JPanel();
	p2.setLayout(new BorderLayout());
	dtm = new DefaultTableModel(){
		public boolean isCellEditable(int r,int c){
			return false;
		}
	};
	table = new JTable(dtm);
	table.setRowHeight(30);
	table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
	table.setFont(f1);
	table.getTableHeader().setFont(f1);
	dtm.addColumn("Rating");
	dtm.addColumn("Unlabel Reviews");
	table.getColumnModel().getColumn(0).setPreferredWidth(200);
	table.getColumnModel().getColumn(1).setPreferredWidth(800);
	jsp = new JScrollPane(table);
	p2.add(jsp,BorderLayout.CENTER);

	p3 = new HeadingPanel("",Theme.GLOSSY_OLIVEGREEN_THEME);
	p3.setPreferredSize(new Dimension(300,150));
	chooser = new JFileChooser(new File("dataset"));
	
	b1 = new JButton("Upload Reviews Dataset");
	b1.setFont(f1);
	b1.setPreferredSize(new Dimension(280,40));
	p3.add(b1);
	b1.addActionListener(new ActionListener(){
		public void actionPerformed(ActionEvent ae){
			int option = chooser.showOpenDialog(DeepLearning.this);
			if(option == chooser.APPROVE_OPTION){
				File file = chooser.getSelectedFile();
				reviews.clear();
				Cursor hourglassCursor = new Cursor(Cursor.WAIT_CURSOR);
				setCursor(hourglassCursor);
				readDataset(file);
				Cursor normalCursor = new Cursor(Cursor.DEFAULT_CURSOR);
				setCursor(normalCursor);
				JOptionPane.showMessageDialog(DeepLearning.this,"Dicitionary Loaded");
			}
		}
	});
	b2 = new JButton("Train CNN-WDE Classifier");
	b2.setFont(f1);
	b2.setPreferredSize(new Dimension(280,40));
	p3.add(b2);
	b2.addActionListener(new ActionListener(){
		public void actionPerformed(ActionEvent ae){
			Cursor hourglassCursor = new Cursor(Cursor.WAIT_CURSOR);
			setCursor(hourglassCursor);
			CNNFeatureVector.readReviews(reviews);
			CNNFeatureVector.generateVector();
			Cursor normalCursor = new Cursor(Cursor.DEFAULT_CURSOR);
			setCursor(normalCursor);
			JOptionPane.showMessageDialog(DeepLearning.this,"Features Extraction Process Completed");
		}
	});

	b3 = new JButton("View Features Vector");
	b3.setPreferredSize(new Dimension(280,40));
	b3.setFont(f1);
	p3.add(b3);
	b3.addActionListener(new ActionListener(){
		public void actionPerformed(ActionEvent ae){
			ViewVector vv = new ViewVector();
			vv.setVisible(true);
			vv.setSize(800,600);
		}
	});

	
	b4 = new JButton("Detect Sentiment Labels");
	b4.setPreferredSize(new Dimension(280,40));
	b4.setFont(f1);
	p3.add(b4);
	b4.addActionListener(new ActionListener(){
		public void actionPerformed(ActionEvent ae){
			Cursor hourglassCursor = new Cursor(Cursor.WAIT_CURSOR);
			setCursor(hourglassCursor);
			detectSentiment();
			Cursor normalCursor = new Cursor(Cursor.DEFAULT_CURSOR);
			setCursor(normalCursor);
		}
	});

	b5 = new JButton("CNN-WDE Classification");
	b5.setFont(f1);
	b5.setPreferredSize(new Dimension(280,40));
	p3.add(b5);
	b5.addActionListener(new ActionListener(){
		public void actionPerformed(ActionEvent ae){
			String input = JOptionPane.showInputDialog(DeepLearning.this,"Enter review for classification Using CNN-WDE");
			if(input != null){
				double qry[] = CNNFeatureVector.generateVector(input);
				String result = "unable to classify";
				double distance = 0;
				for(int i=0;i<labels.size();i++){
					Labels l = labels.get(i);
					double vector[] = CNNFeatureVector.vector.get(i);
					double dis = CNNFeatureVector.euclideanDistance(vector,qry);
					if(dis > distance){
						distance = dis;
						result = l.getLabel();
					}
				}
				String temp = SentimentAnalysis.findSentiment(input);
				if(!temp.equals(result))
					result = temp;
				JOptionPane.showMessageDialog(DeepLearning.this,"Given review classified as : "+result);
			}
		}
	});

	b6 = new JButton("Sentiment Prediction Graph");
	b6.setPreferredSize(new Dimension(280,40));
	b6.setFont(f1);
	p3.add(b6);
	b6.addActionListener(new ActionListener(){
		public void actionPerformed(ActionEvent ae){
			Chart chart1 = new Chart("Sentiment Prediction Graph",labels);
			chart1.pack();
			RefineryUtilities.centerFrameOnScreen(chart1);
			chart1.setVisible(true);
		}
	});

	b7 = new JButton("Logout");
	b7.setPreferredSize(new Dimension(280,40));
	b7.setFont(f1);
	p3.add(b7);
	b7.addActionListener(new ActionListener(){
		public void actionPerformed(ActionEvent ae){
			setVisible(false);
			login.setVisible(true);
		}
	});

	getContentPane().add(p1,BorderLayout.NORTH);
	getContentPane().add(p2,BorderLayout.CENTER);
	getContentPane().add(p3,BorderLayout.SOUTH);
	Stopwords.readStopword();
}
public void clearTable(){
	for(int i=table.getRowCount()-1;i>=0;i--){
		dtm.removeRow(i);
	}
}
public void readDataset(File file){
	try{
		clearTable();
		BufferedReader br = new BufferedReader(new FileReader(file));
		String line = null;
		while((line = br.readLine())!=null){
			line = line.trim();
			if(line.length() > 0){
				String arr[] = line.split("\t");
				String rating = arr[0].trim();
				String review = arr[1].toLowerCase().replaceAll("[^a-zA-Z\\s+]", " "); 
				String row[] = {rating.trim(),review.trim()};
				String row1[] = {arr[0].trim(),arr[1].trim()};
				dtm.addRow(row1);
				reviews.add(row);
			}
		}
		br.close();
	}catch(Exception e){
		e.printStackTrace();
	}
}
public void detectSentiment(){
	labels.clear();
	ViewSentimentLabels vs = new ViewSentimentLabels();
	for(int i=0;i<50;i++){
		String arr[] = reviews.get(i);
		int rating = Integer.parseInt(arr[0].trim());
		String review = arr[1].trim();
		String result = SentimentAnalysis.findSentiment(review);
		Labels l = new Labels();
		l.setRating(rating);
		l.setLabel(result);
		l.setReview(review);
		labels.add(l);
		Object row[] = {rating,review,result};
		vs.dtm.addRow(row);
		System.out.print(i+" ");
	}
	vs.setSize(800,600);
	vs.setVisible(true);
}
}
