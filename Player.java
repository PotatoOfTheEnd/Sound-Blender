package shannon.arielle;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.HashSet;
import java.util.Set;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.SourceDataLine;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

public class Player {

	public static void main(String[] args) {
		Set<File> currentFiles = new HashSet<File>();
		FrequencyAnalyzer fa = new FrequencyAnalyzer();
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
		Matcher m = new Matcher();
		doneChoosing.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				panel.removeAll();
				panel.revalidate();
				panel.repaint();
				int[] keys = new int[currentFiles.size()];
				File[] files = new File[currentFiles.size()];
				int ind= 0;
				for(File f: currentFiles){
					double[] darr = ReadExample.go(f);
					keys[ind] = fa.key(darr);
					files[ind] = f;
					System.out.println(keys[ind]);
					ind++;
				}
				int[] order = m.arrange(keys);
				byte[] buffer = new byte[4096];
			    for (int i:order) {
			        try {
			            AudioInputStream is = AudioSystem.getAudioInputStream(files[i]);
			            AudioFormat format = is.getFormat();
			            SourceDataLine line = AudioSystem.getSourceDataLine(format);
			            line.open(format);
			            line.start();
			            while (is.available() > 0) {
			                int len = is.read(buffer);
			                line.write(buffer, 0, len);
			            }
			            line.drain(); 
			            line.close();
			        } catch (Exception e1) {
			            e1.printStackTrace();
			        }
			    }
				currentFiles.clear();
				
			}
		});
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
							try
						    {
						        Clip clip = AudioSystem.getClip();
						        clip.open(AudioSystem.getAudioInputStream(chooser.getSelectedFile()));
						        clip.start();
						    }
						    catch (Exception exc)
						    {
						        exc.printStackTrace(System.out);
						    }
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
