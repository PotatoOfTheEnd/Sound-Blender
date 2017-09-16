package shannon.arielle;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.HashSet;
import java.util.Set;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

public class Player {

	public static void main(String[] args) {
		FrequencyAnalyzer fa = new FrequencyAnalyzer();
		Set<File> currentFiles = new HashSet<File>();
		JFrame window = new JFrame("Poop");
		JButton chooseMore = new JButton("Add Another Song");
		JPanel panel = new JPanel();
		panel.setSize(200, 200);
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		JScrollPane scrollPane = new JScrollPane(panel);
		scrollPane.setPreferredSize( new Dimension( 200, 200 ) );
		window.setLayout(new FlowLayout());
		window.add(scrollPane, BorderLayout.CENTER);
		window.add(chooseMore);
		window.pack();
		window.setVisible(true);
		JFileChooser chooser = new JFileChooser();
		
		FileNameExtensionFilter filter = new FileNameExtensionFilter(null, "wav");
		chooser.setFileFilter(filter);
		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		chooseMore.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				int returnVal = chooser.showOpenDialog(window);
				if(returnVal == JFileChooser.APPROVE_OPTION) {
					if (!filter.accept(chooser.getSelectedFile()) || currentFiles.contains(chooser.getSelectedFile())){
						return;
					}
					JButton tpButton = new JButton(chooser.getSelectedFile().getName());
					panel.add(tpButton);
					currentFiles.add(chooser.getSelectedFile());
					tpButton.addActionListener(new ActionListener(){
						public void actionPerformed(ActionEvent e){
							tpButton.setVisible(false);
							panel.remove(tpButton);
							currentFiles.remove(chooser.getSelectedFile());
							System.out.println("You chose to open this file: " +
									chooser.getSelectedFile());
							panel.revalidate();
						}
					});
					
					
					panel.revalidate();
					
				}
			}
		});
	}
}
