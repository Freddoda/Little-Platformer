package Little_Platformer;

import javax.swing.JPanel;
import java.awt.*;
import java.awt.event.KeyEvent;

public class RealMain extends JPanel implements Runnable{

    Thread gamethread;

    Keys K = new Keys();
    Clicked C = new Clicked();

    Button Title = new Button(540,200,50,"Little Platformer", new int[]{1,1});
    Button Start = new Button(540,400,50,"Start", new int[]{1,1});
    Button Edit = new Button(0,300,30,"Edit", new int[]{0,1});

    LevelManager levelMan = new LevelManager();

    private enum State{
        START,
        GAME,
        EDIT
    }
    State gamestate = State.START;

    int[] scrSize = new int[]{1080,720};

    public RealMain(){
        this.setPreferredSize(new Dimension(scrSize[0],scrSize[1]));
        this.setBackground(new Color(0,0,0));
        this.setDoubleBuffered(true);
        this.setFocusable(true);
        this.addKeyListener(K);
        this.addMouseListener(C);
    }

    public void startthread(){
        gamethread = new Thread(this);
        gamethread.start();
    }

    double drawinter = 1000000000/60;

    public void run(){
        while (gamethread != null){
            double nextdraw = (System.nanoTime()+drawinter);


            update();
            repaint();
            
            double remtime = (nextdraw - System.nanoTime())/1000000;
            try {
                if (remtime>0){
                    Thread.sleep((long) remtime);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (K.keys.contains(KeyEvent.VK_ESCAPE)){
                System.exit(0);
            }
        }
    }

    private void update(){

        if (gamestate == State.START){
            if (Start.clicked(C,this)){
                levelMan.levLoad();
                gamestate=State.GAME;
            } else if (Edit.clicked(C,this)){
                levelMan.levLoad();
                gamestate=State.EDIT;
            }
        } else if (gamestate == State.GAME){
            levelMan.gameupdate(K.keys);
            if (levelMan.won()){
                gamestate = State.START;
            }
        } else if (gamestate == State.EDIT){
            levelMan.edit(C,K,this);
        }
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D G= (Graphics2D) g;

        if (gamestate == State.START){
            Title.textdisp(Color.WHITE,G);
            Start.buttondisp(new int[]{255,0,0},C,G,this);
            Edit.buttondisp(new int[]{255,0,0},C,G,this);
        } else if (gamestate == State.GAME){
            levelMan.gamedraw(G);
        } else if (gamestate == State.EDIT){
            levelMan.gamedraw(G);
            levelMan.draw_selected(G);
        }

        G.dispose();
    }
}   
