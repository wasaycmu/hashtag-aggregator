package Package;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class UI {
	private static void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("FrameDemo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
 
        JLabel emptyLabel = new JLabel("#Aggregator");
        emptyLabel.setPreferredSize(new Dimension(1400, 800));
        frame.getContentPane().add(emptyLabel, BorderLayout.BEFORE_FIRST_LINE);
 
        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }
 
    public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {createAndShowGUI();}
            });
    }
}


