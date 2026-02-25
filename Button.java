package Little_Platformer;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;

public class Button{
    // could I also use this for regular text display????
    
    int x;
    int y;
    Font font;
    int[] root = new int[2];
    String text;
    FontMetrics fm;
    boolean clickch = false;
    String func;

    public Button(int X, int Y, int Size, String Text, int[] Root){
        // int[] Root: 0 means from top or left, 1 means from centre, 2 means from right or bottom
        this.x=X;
        this.y=Y;
        this.font= new Font("SansSerif", Font.PLAIN, Size);
        this.text=Text;
        this.root=Root;
    }

    public Button(int X, int Y, int Size, String Text, int[] Root, String Func){
        // second constructor which actually has the func
        // I don't think I need the func for anything now that I've moved statechange straight into the gameloop, and platform adding to Blockmanager
        this.x=X;
        this.y=Y;
        this.font= new Font("SansSerif", Font.PLAIN, Size);
        this.text=Text;
        this.root=Root;
        this.func=Func;
    }

    public void textdisp(Color c, Graphics2D g){
        if (fm == null){
            fm = g.getFontMetrics(font);
        }
        g.setFont(font);
        g.setColor(c);
        g.drawString(text,x-root[0]*fm.stringWidth(text)/2,(int)(y-(root[1]-1.5)*fm.getHeight()/2));
    }

    public void buttondisp(int[] colour, Clicked c, Graphics2D g, JPanel scr){
        if (fm == null){
            fm = g.getFontMetrics(font);
        }
        int[] mousepos = c.getmousepos(scr);
        boolean drawn = false;
        g.setFont(font);
        if (clickch){
            drawn=true;
            g.setColor(new Color((int) (colour[0]/2), (int) (colour[1]/2), (int) (colour[2]/2)));
            g.fillRect(x-root[0]*fm.stringWidth(text)/2,y-root[1]*fm.getHeight()/2,fm.stringWidth(text),fm.getHeight());
            g.setColor(new Color((int) (255/2),(int) (255/2),(int) (255/2)));
            g.drawString(text,x-root[0]*fm.stringWidth(text)/2,(int) (y-(root[1]-1.5)*fm.getHeight()/2));
        }
        if (mousepos != null && !drawn){
            if (
            mousepos[0]>x-root[0]*fm.stringWidth(text)/2 && mousepos[0]<x-(root[0]-2)*fm.stringWidth(text)/2 && 
            mousepos[1]>y-root[1]*fm.getHeight()/2 && mousepos[1]<y-(root[1]-2)*fm.getHeight()/2
            ){
                if (!clickch){
                    drawn=true;
                    g.setColor(new Color(colour[0],colour[1],colour[2]));
                    g.fillRect(x-root[0]*fm.stringWidth(text)/2,y-root[1]*fm.getHeight()/2,fm.stringWidth(text),fm.getHeight());
                    g.setColor(new Color(255,255,255));
                    g.drawString(text,x-root[0]*fm.stringWidth(text)/2,(int) (y-(root[1]-1.5)*fm.getHeight()/2));           
                } 
            } 
        }
        if (!drawn){
            g.setColor(new Color( (int) (colour[0]/1.5), (int) (colour[1]/1.5), (int) (colour[2]/1.5)));
            g.fillRect(x-root[0]*fm.stringWidth(text)/2,y-root[1]*fm.getHeight()/2,fm.stringWidth(text),fm.getHeight());
            g.setColor(new Color((int)(255/1.5),(int)(255/1.5),(int)(255/1.5)));
            g.drawString(text,x-root[0]*fm.stringWidth(text)/2,(int) (y-(root[1]-1.5)*fm.getHeight()/2));
        }
    }

    public void buttonclick(Clicked c, JPanel scr){
        if(fm != null && clickch==false){
            int[] mousepos = c.getmousepos(scr);
            if (mousepos != null){
                if (
                mousepos[0]>x-root[0]*fm.stringWidth(text)/2 && mousepos[0]<x-(root[0]-2)*fm.stringWidth(text)/2 && 
                mousepos[1]>y-root[1]*fm.getHeight()/2 && mousepos[1]<y-(root[1]-2)*fm.getHeight()/2
                ){
                    if (c.Mbuttons.contains(MouseEvent.BUTTON1)){
                        clickch=true;
                    }
                }
            }
        }
    }

    public boolean clicked(Clicked c, JPanel scr){
        if(fm != null && clickch==false){
            int[] mousepos = c.getmousepos(scr);
            if (mousepos != null){
                if (
                mousepos[0]>x-root[0]*fm.stringWidth(text)/2 && mousepos[0]<x-(root[0]-2)*fm.stringWidth(text)/2 && 
                mousepos[1]>y-root[1]*fm.getHeight()/2 && mousepos[1]<y-(root[1]-2)*fm.getHeight()/2
                ){
                    if (c.Mbuttons.contains(MouseEvent.BUTTON1)){
                        clickch=true;
                    }
                }
            }
        }
        if (clickch && !c.Mbuttons.contains(MouseEvent.BUTTON1)){
            clickch=false;
            return true;
        } else {
            return false;
        }
    }
}
