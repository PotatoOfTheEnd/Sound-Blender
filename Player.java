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
		Set<File> currentFiles = new HashSet<File>();
		JFrame window = new JFrame("Song Blender");
		JButton chooseMore = new JButton("Add Another Song");
		JButton doneChoosing = new JButton("Blend the Songs");
		JPanel panel = new JPanel();
		JPanel biggerPanel = new JPanel();
		JPanel buttonsPanel = new JPanel();
		
		panel.setSize(200, 200);
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		biggerPanel.setLayout(new BoxLayout(biggerPanel, BoxLayout.Y_AXIS));
		JScrollPane scrollPane = new JScrollPane(panel);
		scrollPane.setPreferredSize( new Dimension( 200, 200 ) );
		biggerPanel.add(new JLabel("Click on a song to remove it"));
		biggerPanel.add(scrollPane);
		window.setLayout(new FlowLayout());
		window.add(biggerPanel, BorderLayout.CENTER);
		buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.Y_AXIS));
		
		window.add(buttonsPanel);
		window.add(doneChoosing);
		window.pack();
		window.setVisible(true);
		buttonsPanel.add(chooseMore);
		buttonsPanel.add(doneChoosing);
		JFileChooser chooser = new JFileChooser();
		
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Wav files", "wav");
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
