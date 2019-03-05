package frc.robot.GUI;

import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;

//Graphical user interface thread
public class GUIT extends Thread{
    private GPanel gPanel;
    @Override
    public void run(){
        JFrame f=new JFrame("Tractor Tech GUI");
        gPanel=new GPanel();
        f.setPreferredSize(new Dimension(300,300));
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setLocationRelativeTo(null);
        f.add(gPanel);
        f.pack();
        f.setVisible(true);
    }
    public GPanel getGPanel(){
        return gPanel;
    }

}
