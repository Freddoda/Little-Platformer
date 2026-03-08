package Little_Platformer;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.awt.event.KeyEvent;
//import java.util.Arrays;

public class Blockmanager {

    public static enum BlockType{
        NORMAL,
        WIN,
        HARM
    }
    
    /*
    public record Block(int x, int y, int w, int h, Color c, BlockType t){
        public Block(int x, int y, int w, int h){
            this(x,y,w,h,Color.WHITE,BlockType.NORMAL);
        }

        public Block(int x, int y, int w, int h,Color c){
            this(x,y,w,h,c,BlockType.NORMAL);
        }

        public Block(int x, int y, int w, int h, BlockType t){
            Color c;
            switch (t){
                case BlockType.WIN:
                    c=Color.YELLOW;
                    break;
                case BlockType.HARM:
                    c=Color.RED;
                    break;
                default:
                    c=Color.WHITE;
                    break;
            }
            this(x,y,w,h,c,t);
        }
    }
    */
    // commented it out because edit mode requires Block values to be mutable

    public static class Block{
        int x;
        int y;
        int w;
        int h;
        Color c;
        BlockType t;
        Boolean selected;

        public Block(int x, int y, int w, int h, Color c, BlockType t, Boolean selected){
            this.x = x;
            this.y = y;
            this.w = w;
            this.h = h;
            this.c = c;
            this.t = t;
            this.selected = selected;
        }

        public Block(int x, int y, int w, int h){
            this(x,y,w,h,Color.WHITE,BlockType.NORMAL,false);
        }

        public Block(int x, int y, int w, int h,Color c){
            this(x,y,w,h,c,BlockType.NORMAL,false);
        }

        public Block(int x, int y, int w, int h, BlockType t){
            Color c;
            switch (t){
                case BlockType.WIN:
                    c=Color.YELLOW;
                    break;
                case BlockType.HARM:
                    c=Color.RED;
                    break;
                default:
                    c=Color.WHITE;
                    break;
            }
            this(x,y,w,h,c,t,false);
        }

        public Block(int x, int y, int w, int h, boolean selected){
            this(x,y,w,h,Color.WHITE,BlockType.NORMAL,selected);
        }
    }

    //Block[] testlev = new Block[]{new Block(200,500,30,70,BlockType.HARM),new Block(600,600,50,140),new Block(540,700,1120,50),new Block(900,480,20,20,BlockType.WIN)};

    ArrayList<Block> BlockList = new ArrayList<>();
    int Amount = 0;

    /*
    public void levLoad(Levels lev){
        BlockList.clear();
        if (lev == Levels.TEST){
            BlockList.addAll(Arrays.asList(testlev));
        }
        Amount = BlockList.size();
    }
    */

    public Block get(int index){
        if (index<Amount && index>=0){
            return BlockList.get(index);
        } else {
            return null;
        }
    }

    public void draw(Graphics2D g){
        for (Block b : BlockList){
            g.setColor(b.c);
            g.fillRect(b.x-b.w/2,b.y-b.h/2,b.w,b.h);
        }
    }

    public void editMove(Keys k){
        try {
            for (Block b: BlockList){
                if (b.selected){
                    if (k.keys.contains(java.awt.event.KeyEvent.VK_W)){
                        b.y-=5;
                    }
                    if (k.keys.contains(java.awt.event.KeyEvent.VK_S)){
                        b.y+=5;
                    }
                    if (k.keys.contains(java.awt.event.KeyEvent.VK_A)){
                        b.x-=5;
                    }
                    if (k.keys.contains(java.awt.event.KeyEvent.VK_D)){
                        b.x+=5;
                    }
                    if (k.keys.contains(java.awt.event.KeyEvent.VK_SHIFT)){
                        if (k.keys.contains(java.awt.event.KeyEvent.VK_E)){
                            b.w-=5;
                        }
                        if (k.keys.contains(java.awt.event.KeyEvent.VK_Q)){
                            b.h-=5;
                        }
                    } else {
                        if (k.keys.contains(java.awt.event.KeyEvent.VK_E)){
                            b.w+=5;
                        }
                        if (k.keys.contains(java.awt.event.KeyEvent.VK_Q)){
                            b.h+=5;
                        }
                    }
                    if (k.keys.contains(java.awt.event.KeyEvent.VK_M)){
                        BlockList.remove(b);
                    }
                }
            }
        } catch (Exception e) {

        }
    }

    public int addBlock (Keys k, int timer){
        if (k.keys.contains(java.awt.event.KeyEvent.VK_1) && timer==30){
            for (Block b : BlockList) {
                b.selected = false;
            }
            BlockList.add(new Block(0,0,50,50,BlockType.NORMAL));
            timer=0;
        }
        return timer;
    }

    public int changeType(Keys k, int timer){
        if ((k.keys.contains(KeyEvent.VK_O) ^ k.keys.contains(KeyEvent.VK_P)) && timer==30){
            timer=0;
            for (Block b : BlockList){
                if (b.selected){
                    if (k.keys.contains(KeyEvent.VK_O)){
                        if (b.t.ordinal() == 0){
                            b.t = BlockType.values()[BlockType.values().length-1];
                        } else {
                            b.t = BlockType.values()[b.t.ordinal()-1];
                        }
                    } else {
                        if (b.t.ordinal() == BlockType.values().length-1){
                            b.t = BlockType.values()[0];
                        } else {
                            b.t = BlockType.values()[b.t.ordinal()+1];
                        }
                    }
                }
            }
        }
        return timer;
    }
}
