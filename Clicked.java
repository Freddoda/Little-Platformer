package Little_Platformer;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.*;
import javax.swing.JPanel;

public class Clicked implements MouseListener {
    public List<Integer> Mbuttons = new ArrayList<>();

    @Override
    public void mouseClicked(MouseEvent e) {}

    @Override
    public void mousePressed(MouseEvent e) {
        int button = e.getButton();
        if (!Mbuttons.contains(button)){
            Mbuttons.add(button);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        int button = e.getButton();
        Mbuttons.remove(Mbuttons.indexOf(button));
    }

    @Override
    public void mouseEntered(MouseEvent e){}

    @Override
    public void mouseExited(MouseEvent e) {}

    public int[] getmousepos(JPanel scr){
        try{
            return new int[]{ (int) scr.getMousePosition().x, (int) scr.getMousePosition().y};
        } catch (Exception e){
            return null;
        }
    }
}
