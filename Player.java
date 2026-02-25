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
    boolean won = false;
    boolean selected = false;

    public Player(int X, int Y, int W, int H){
        this.x = X;
        this.y = Y;
        this.w = W;
        this.h = H;
        xd=X;
        yd=Y;
    }

    public Player(int W, int H){
        this.w = W;
        this.h = H;
    }

    public void spawn(int[] coords){
        x=coords[0];
        xd=coords[0];
        y=coords[1];
        yd=coords[1];
        xspeed=0;
        yspeed=0;
    }

    public void spawn(Blockmanager.Levels lev){
        this.spawn(Blockmanager.getplayercoords(lev));
    }

    public void draw(Graphics2D g){
        g.setColor(Color.BLUE);
        g.fillRect(x-w/2,y-h/2,w,h);
        g.fillRect(x-1080-w/2,y-h/2,w,h);
        g.fillRect(x+1080-w/2,y-h/2,w,h);
    }

    public void move(List<Integer> keys){
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

    public void momentum(List<Integer> keys){
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

    /*
    public void collision(ArrayList<Block2> pfs){
        floored = false;
        for (Block2 i : pfs){
            if (i.y-i.h/2<y+h/2 && y+h/2<=i.y-i.h/2+yspeed+1 && i.x-i.w/2<x+w/2 && i.x+i.w/2>x-w/2){
                floored = true;
                y=i.y-i.h/2-h/2;
                if (yspeed>0){yspeed=0;}
            } else if (i.y+i.h/2>=y-h/2 && y-h/2>=i.y+i.h/2+yspeed-1 && i.x-i.w/2<x+w/2 && i.x+i.w/2>x-w/2 && yspeed<0){
                y=i.y+i.h/2+h/2;
                if (yspeed<0){yspeed=0;}
            } else if (i.y-i.h/2<y+h/2 && i.y+i.h/2>y-h/2){
                if (i.x-i.w/2<=x+w/2 && i.x-i.w/2+xspeed>=x+w/2){
                    x=i.x-i.w/2-w/2;
                    if (xspeed>0){xspeed=0;}
                } else if (i.x+i.w/2>=x-w/2 && i.x+i.w/2+xspeed<=x-w/2){
                    x=i.x+i.w/2+w/2;
                    if (xspeed<0){xspeed=0;}
                }
            }
        }
    }
    */

    public void collision(Blockmanager bm){
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
    }

    public boolean won(){
        if (won){
            won=false;
            return true;
        } else {
            return false;
        }
    }
}