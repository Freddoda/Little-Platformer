package Little_Platformer;

import java.awt.Graphics2D;
import java.awt.Color;
import java.util.*;

import java.awt.event.KeyEvent;

public class Player {
    int x;
    int xd;
    int y;
    int yd;
    int w;
    int h;
    float xspeed;
    float yspeed;
    boolean floored = true;
    boolean selected = false;

    protected Player(int X, int Y, int W, int H){
        this.x = X;
        this.y = Y;
        this.w = W;
        this.h = H;
        xd=X;
        yd=Y;
    }

    protected Player(int W, int H){
        this.w = W;
        this.h = H;
    }

    protected void spawn(int[] coords){
        x=coords[0];
        xd=coords[0];
        y=coords[1];
        yd=coords[1];
        xspeed=0;
        yspeed=0;
    }

    protected void draw(Graphics2D g, LevelManager.Camera cam){
        g.setColor(Color.BLUE);
        g.fillRect(x-w/2 - cam.completeOffset[0], y-h/2 - cam.completeOffset[1],w,h);
    }

    protected void move(List<Integer> keys){
        this.y+=this.yspeed;
        this.x+=this.xspeed;
        if (x>1080){
            x-=1080;
        } else if (x<0){
            x+=1080;
        }
        if (y>720){
            y-=720;
        } else if (y<0){
            y+=720;
        }
    }

    protected void momentum(List<Integer> keys){
        if (floored == false){
            this.yspeed+=0.8;
        } else {
            this.yspeed=0;
        }
        if (keys.contains(KeyEvent.VK_W) || keys.contains(KeyEvent.VK_UP) || keys.contains(KeyEvent.VK_SPACE)){
            if (floored){
                this.yspeed-=18;
                y-=1;
            }
        }
        if ((keys.contains(KeyEvent.VK_A) || keys.contains(KeyEvent.VK_LEFT))){
            if (floored){
                this.xspeed-=1.2;
            } else {
                this.xspeed-=0.5;
            }
            if (this.xspeed<-9){
                this.xspeed=-9;
            }
        }
        if ((keys.contains(KeyEvent.VK_D) || keys.contains(KeyEvent.VK_RIGHT))){
            if (floored){
                this.xspeed+=1.2;
            } else {
                this.xspeed+=0.5;
            }
            if (this.xspeed>9){
                this.xspeed=9;
            }
        }
        if (!(keys.contains(KeyEvent.VK_A) || keys.contains(KeyEvent.VK_LEFT) || keys.contains(KeyEvent.VK_D) || keys.contains(KeyEvent.VK_RIGHT))){
            if (floored){
                if (this.xspeed<0){
                    if (xspeed<-0.6){
                    this.xspeed+=0.6;
                    } else{
                        xspeed=0;
                    }
                } else if (this.xspeed>0){
                    if (xspeed>0.6){
                    this.xspeed-=0.6;
                    } else {
                        xspeed=0;
                    }
                }
            } else {
                if (this.xspeed<0){
                    if (xspeed<-0.2){
                    this.xspeed+=0.2;
                    } else{
                        xspeed=0;
                    }
                } else if (this.xspeed>0){
                    if (xspeed>0.2){
                    this.xspeed-=0.2;
                    } else {
                        xspeed=0;
                    }
                }
            }
        }
    }


    protected boolean collision(Blockmanager bm){
        boolean won = false;
        floored = false;
        boolean collide;
        Blockmanager.Block i;
        for (int n=0; n<bm.Amount; n++){
            i=bm.get(n);
            collide = false;
            if (i.y-i.h/2<y+h/2 && y+h/2<=i.y-i.h/2+yspeed+1 && i.x-i.w/2<x+w/2 && i.x+i.w/2>x-w/2){
                floored = true;
                collide = true;
                y=i.y-i.h/2-h/2;
                if (yspeed>0){yspeed=0;}
            } else if (i.y+i.h/2>=y-h/2 && y-h/2>=i.y+i.h/2+yspeed-1 && i.x-i.w/2<x+w/2 && i.x+i.w/2>x-w/2 && yspeed<0){
                collide = true;
                y=i.y+i.h/2+h/2;
                if (yspeed<0){yspeed=0;}
            } else if (i.y-i.h/2<y+h/2 && i.y+i.h/2>y-h/2){
                if (i.x-i.w/2<=x+w/2 && i.x-i.w/2+xspeed>=x+w/2){
                    collide = true;
                    x=i.x-i.w/2-w/2;
                    if (xspeed>0){xspeed=0;}
                } else if (i.x+i.w/2>=x-w/2 && i.x+i.w/2+xspeed<=x-w/2){
                    collide = true;
                    x=i.x+i.w/2+w/2;
                    if (xspeed<0){xspeed=0;}
                }
            }
            if (collide){
                switch (i.t){
                    case HARM:
                        x=xd;
                        y=yd;
                        xspeed=0;
                        yspeed=0;
                        break;
                    case WIN:
                        won=true;
                        break;
                    default:
                        break;
                }
            }
        }
        return won;
    }

    protected void editMove(Keys k){
        if (selected){
            if (k.keys.contains(java.awt.event.KeyEvent.VK_W)){
                y-=5;
                yd-=5;
            }
            if (k.keys.contains(java.awt.event.KeyEvent.VK_S)){
                y+=5;
                yd+=5;
            }
            if (k.keys.contains(java.awt.event.KeyEvent.VK_A)){
                x-=5;
                xd-=5;
            }
            if (k.keys.contains(java.awt.event.KeyEvent.VK_D)){
                x+=5;
                xd+=5;
            }
        }
    }
}