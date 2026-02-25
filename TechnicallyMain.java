package Little_Platformer;
import javax.swing.JFrame;

import java.awt.Dimension;
import java.awt.Toolkit;

public class TechnicallyMain {
    public static void main(String[] args){
        JFrame window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(true);
        window.setTitle("Little Platformer");

        Dimension scrsize = Toolkit.getDefaultToolkit().getScreenSize();
        int scr_w=(int) scrsize.getWidth();
        int scr_h=(int) scrsize.getHeight();
        window.setLocation(scr_w/2-540,scr_h/2-360);
        
        RealMain contents = new RealMain();
        window.add(contents);

        window.pack();
        window.setVisible(true);

        contents.startthread();
        contents.run();
    }
}
