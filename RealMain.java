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

    Player P = new Player(30,50);
    Blockmanager bMan = new Blockmanager();

    Blockmanager.Levels level = Blockmanager.Levels.TEST;

    private enum State{
        START,
        GAME,
        EDIT
    }
    State gamestate = State.START;

    public RealMain(){
        this.setPreferredSize(new Dimension(1080,720));
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
                bMan.levLoad(level);
                P.spawn(level);
                gamestate=State.GAME;
            } else if (Edit.clicked(C,this)){
                bMan.levLoad(level);
                P.spawn(level);
                gamestate=State.EDIT;
            }
        } else if (gamestate == State.GAME){
            P.move(K.keys);
            P.momentum(K.keys);
            P.collision(bMan);
            if (P.won()){
                gamestate = State.START;
            }
        } else if (gamestate == State.EDIT){
            bMan.select(C,P,this);
            bMan.editMove(K,P);
            bMan.addBlock(K,P);
            bMan.saveLevel(K, level, P);
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
            P.draw(G);
            bMan.draw(G);
        } else if (gamestate == State.EDIT){
            bMan.draw(G);
            P.draw(G);
            bMan.draw_selected(P,G);
        }

        G.dispose();
    }
}   
