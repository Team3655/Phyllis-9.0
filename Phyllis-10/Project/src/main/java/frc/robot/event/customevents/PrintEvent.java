package frc.robot.event.customevents;

import frc.robot.event.*;

public class PrintEvent extends Event{
    private String text;
    private boolean err;
    public PrintEvent(String s){
        super();
        text=s;
        err=false;
    }
    public PrintEvent(String s,boolean err){
        super();
        text=s;
        this.err=err;
    }
    public PrintEvent(int i){
        super();
        text=String.valueOf(i);
        err=false;
    }
    public PrintEvent(Object o){
        super();
        text=String.valueOf(o);
        err=false;
    }
    public PrintEvent(long l){
        super();
        text=String.valueOf(l);
        err=false;
    }
    public PrintEvent(double d){
        super();
        text=String.valueOf(d);
        err=false;
    }
    public PrintEvent(float f){
        super();
        text=String.valueOf(f);
        err=false;
    }
    public PrintEvent(boolean b){
        super();
        text=String.valueOf(b);
        err=false;
    }
    public PrintEvent(char c){
        super();
        text=String.valueOf(c);
    }
    public PrintEvent(char[] c){
        super();
        text=String.valueOf(c);
        err=false;
    }
    public void task(){
        System.out.print(text);
    }
}