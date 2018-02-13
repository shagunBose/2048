import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Index {
	
	public static void main (String[] args) {
		Game newGame = new Game();
//		start = new JButton("START");
//		start.setBounds(342, 100, 80, 30);
//		start.setBackground(backgroundColor);
//		start.setFocusPainted(false);
//		this.add(start);
//		start.addActionListener(new ActionListener(){
//
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				start();
//			}
//			
//		});
		
		JFrame frame = new JFrame("2048");
		frame.setSize(470, 640);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());
		frame.add(newGame, BorderLayout.CENTER);
		frame.setVisible(true);

	}
	
	

}
