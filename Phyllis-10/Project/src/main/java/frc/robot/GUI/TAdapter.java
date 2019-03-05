package frc.robot.GUI;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class TAdapter extends KeyAdapter{
    private GPanel g;
    protected TAdapter(GPanel g){
        this.g=g;
    }
    @Override
    public void keyPressed(KeyEvent e){
        switch (e.getKeyCode()){

        }
    }
    public void keyReleased(KeyEvent e){

    }
}