package Little_Platformer;

import java.awt.Color;
import java.awt.Graphics2D;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
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

    public enum Levels{
        TEST
    }

    /*
    public void levLoad(Levels lev){
        BlockList.clear();
        if (lev == Levels.TEST){
            BlockList.addAll(Arrays.asList(testlev));
        }
        Amount = BlockList.size();
    }
    */

    public void levLoad(Levels lev){
        BlockList.clear();
        if (lev == Levels.TEST){
            BlockList.addAll(LevFileRead(lev));
        }
        Amount = BlockList.size();
    }

    private static File fileget(Levels lev){
        File levfile = null;
        switch (lev){
            case TEST:
                levfile = new File("Little_Platformer/TestLev.txt");
                break;
            default:
                break;
        }
        return levfile;
    }

    public ArrayList<Block> LevFileRead(Levels lev){
        File levfile = null;
        ArrayList<Block> blocks = new ArrayList<>();
        String cl1;
        String[] cl2;
        boolean isreading = true;
        levfile = fileget(lev);
        if (!(levfile==null)){
            try (java.util.Scanner reader = new java.util.Scanner(levfile)){
                reader.nextLine();
                while (isreading){
                    if (reader.hasNextLine()){
                        cl1 = reader.nextLine();
                        cl2 = cl1.split(",");
                        blocks.add(new Block(Integer.valueOf(cl2[0]),Integer.valueOf(cl2[1]),Integer.valueOf(cl2[2]),Integer.valueOf(cl2[3]),BlockType.valueOf(cl2[4])));
                    } else {
                        isreading = false;
                    }
                }
            } catch (FileNotFoundException e){
                System.out.println(e);
            }
        }
        return blocks;
    }

    public static int[] getplayercoords(Levels lev){
        int[] pCoords = new int[2];
        File levfile = null;      
        levfile = fileget(lev);
        String cl1;
        String[] cl2;
        if (!(levfile==null)){
            try (java.util.Scanner reader = new java.util.Scanner(levfile)){
                cl1 = reader.nextLine();
                cl2 = cl1.split(",");
                pCoords = new int[]{Integer.valueOf(cl2[0]),Integer.valueOf(cl2[1])};
            } catch(FileNotFoundException e){
                System.out.println(e);
            }
        }
        return pCoords;
    }

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

    public void select(Clicked c, Player p, javax.swing.JPanel scr){
        if (c.Mbuttons.contains(java.awt.event.MouseEvent.BUTTON1)){
            int[] mousepos = c.getmousepos(scr);
            if (mousepos[0]>p.x-p.w/2 && mousepos[0]<p.x+p.w/2 && mousepos[1]>p.y-p.h/2 && mousepos[1]<p.y+p.h/2){
                p.selected=true;
                for (Block b : BlockList){
                    b.selected=false;
                }
            } else {
                p.selected = false;
                for (Block b : BlockList){
                    if ((mousepos[0]>b.x-b.w/2 && mousepos[0]<b.x+b.w/2 && mousepos[1]>b.y-b.h/2 && mousepos[1]<b.y+b.h/2)){
                        b.selected=true;
                    } else {
                        b.selected=false;
                    }
                }
            }
        } 
    }

    public void draw_selected(Player p, Graphics2D g){
        g.setColor(Color.GREEN);
        if (p.selected){
            g.drawRect((int) p.x-p.w/2, (int) p.y-p.h/2, p.w, p.h);
        } else{
            for (Block b : BlockList){
                if (b.selected){
                    g.drawRect((int) b.x-b.w/2, (int) b.y-b.h/2, b.w, b.h);
                }
            }
        }
    }

    public void editMove(Keys k, Player p){
        if (p.selected){
            if (k.keys.contains(java.awt.event.KeyEvent.VK_W)){
                p.y-=5;
                p.yd-=5;
            }
            if (k.keys.contains(java.awt.event.KeyEvent.VK_S)){
                p.y+=5;
                p.yd+=5;
            }
            if (k.keys.contains(java.awt.event.KeyEvent.VK_A)){
                p.x-=5;
                p.xd-=5;
            }
            if (k.keys.contains(java.awt.event.KeyEvent.VK_D)){
                p.x+=5;
                p.xd+=5;
            }
        } else {
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
    }

    public void addBlock (Keys k, Player p){
        if (k.keys.contains(java.awt.event.KeyEvent.VK_N)){
            p.selected = false;
            for (Block b : BlockList) {
                b.selected = false;
            }
            BlockList.add(new Block(0,0,50,50,true));
        }
    }

    public void saveLevel (Keys k, Levels lev, Player p){
        if (k.keys.contains(java.awt.event.KeyEvent.VK_P)){
            File file = fileget(lev);
            try (java.io.FileWriter writer = new java.io.FileWriter(file)){
                writer.write(String.valueOf(p.x)+','+String.valueOf(p.y)+"\n");
                for (Block b : BlockList){
                    writer.write(String.valueOf(b.x)+','+String.valueOf(b.y)+','+String.valueOf(b.w)+','+String.valueOf(b.h)+','+String.valueOf(b.t)+"\n");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
