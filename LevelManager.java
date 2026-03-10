package Little_Platformer;

import java.util.List;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.awt.Color;
import java.awt.Graphics2D;
import javax.swing.JPanel;
import java.awt.event.KeyEvent;

import Little_Platformer.Blockmanager.Block;
import Little_Platformer.Blockmanager.BlockType;

public class LevelManager {
    Blockmanager blockMan = new Blockmanager();
    Player player = new Player(30, 50);
    boolean won = false;

    int editTimer = 30;

    Levels currentLevel = Levels.TEST;

    protected enum Levels{
        TEST
    }

    protected enum EditModes{
        NONE,
        PLATFORM,
        PLAYER,
        BOUNDING,
        ENEMY
    }

    EditModes editMode = EditModes.NONE;

    protected class BoundingBox{
        int[] topLeft = new int[2];
        int[] botRight = new int[2];

        BoundingBox(){
            this.topLeft = new int[]{0,0};
            this.botRight = new int[]{0,0};
        }

        void setPoints(int[] topLeft, int[] botRight){
            this.topLeft = topLeft;
            this.botRight = botRight;
        }

        void setPoints(int[][] points){
            this.topLeft = points[0];
            this.botRight = points[1];
        }

        void editMove(Keys k){
            if (!k.keys.contains(KeyEvent.VK_SHIFT)){
                if (k.keys.contains(KeyEvent.VK_W)){
                    topLeft[1]-=3;
                }
                if (k.keys.contains(KeyEvent.VK_A)){
                    topLeft[0]-=3;
                }
                if (k.keys.contains(KeyEvent.VK_S) && topLeft[1]<botRight[1]-3){
                    topLeft[1]+=3;
                }
                if (k.keys.contains(KeyEvent.VK_D) && topLeft[0]<botRight[0]-3){
                    topLeft[0]+=3;
                }
            } else {
                if (k.keys.contains(KeyEvent.VK_W) && botRight[1]>topLeft[1]+3){
                    botRight[1]-=3;
                }
                if (k.keys.contains(KeyEvent.VK_A) && botRight[0]>topLeft[0]+3){
                    botRight[0]-=3;
                }
                if (k.keys.contains(KeyEvent.VK_S)){
                    botRight[1]+=3;
                }
                if (k.keys.contains(KeyEvent.VK_D)){
                    botRight[0]+=3;
                }
            }
        }

        void draw(Graphics2D g, Camera cam){
            g.setColor(Color.WHITE);
            g.drawRect(topLeft[0]-cam.completeOffset[0],topLeft[1]-cam.completeOffset[1],botRight[0]-topLeft[0],botRight[1]-topLeft[1]);
        }
    }

    protected class Camera{
        int[] movedOffset = new int[]{0,0};
        int[] completeOffset = new int[]{0,0}; //offset increases, cam moves right, other things go left
        final int[] assumedScrSize = new int[]{1080,720};
        int[] realScrSize = new int[]{1080,720};

        void reset(){
            movedOffset = new int[]{0,0};
        }

        void screenSize(JPanel screen){
            realScrSize = new int[]{screen.getWidth(),screen.getHeight()};
        }

        void calcComplOffset(){
            completeOffset = new int[]{movedOffset[0]-(int)(realScrSize[0]-assumedScrSize[0])/2,
                                       movedOffset[1]-(int)(realScrSize[1]-assumedScrSize[1])/2 };
        }

        void editMove(Keys k){
            if (k.keys.contains(KeyEvent.VK_W)){
                movedOffset[1] -=3;
            }
            if (k.keys.contains(KeyEvent.VK_S)){
                movedOffset[1] +=3;
            }
            if (k.keys.contains(KeyEvent.VK_A)){
                movedOffset[0] -=3;
            }
            if (k.keys.contains(KeyEvent.VK_D)){
                movedOffset[0] +=3;
            }
        }

        void gameMove(Player plyr, BoundingBox bnd){
            if (bnd.botRight[0]-bnd.topLeft[0]<realScrSize[0]){
                movedOffset[0]=0;
            }
            if (bnd.botRight[1]-bnd.topLeft[1]<realScrSize[1]){
                movedOffset[1]=0;
            }

            // I should probably finish this at some point
        }
    }

    BoundingBox bound = new BoundingBox();
    Camera cam = new Camera();

    protected void gameupdate(List<Integer> keys, JPanel screen){
        cam.screenSize(screen);
        cam.calcComplOffset();
        cam.gameMove(player,bound);

        player.move(keys);
        player.momentum(keys);
        player.boundsCheck(bound);
        won = player.collision(blockMan);
    }

    protected void gamedraw(Graphics2D G){
        player.draw(G,cam);
        blockMan.draw(G, cam);
        bound.draw(G,cam);
    }

    protected boolean won(){
        if (won){
            won=false;
            return true;
        } else {
            return false;
        }
    }

    protected void levLoad(){
        blockMan.BlockList.clear();
        blockMan.BlockList.addAll(LevFileRead(currentLevel));
        blockMan.Amount = blockMan.BlockList.size();

        player.spawn(getplayercoords(currentLevel));
        bound.setPoints(getboundingbox(currentLevel));

        cam.reset();
    }

    private static File fileget(Levels lev){
        File levfile = null;
        switch (lev){
            case TEST:
                levfile = new File("Little_Platformer/Levels/TestLev.txt");
                break;
            default:
                break;
        }
        return levfile;
    }

    protected static ArrayList<Block> LevFileRead(Levels lev){
        File levfile = null;
        ArrayList<Block> blocks = new ArrayList<>();
        String cl1;
        String[] cl2;
        boolean isreading = true;
        levfile = fileget(lev);
        if (!(levfile==null)){
            try (java.util.Scanner reader = new java.util.Scanner(levfile)){
                reader.nextLine();
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

    protected static int[] getplayercoords(Levels lev){
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

    protected static int[][] getboundingbox(Levels lev){
        int[][] points = new int[][]{new int[]{0,0}, new int[]{0,0}};
        File levfile = null;      
        levfile = fileget(lev);
        String cl1;
        String[] cl2;
        if (!(levfile==null)){
            try (java.util.Scanner reader = new java.util.Scanner(levfile)){
                reader.nextLine();
                cl1 = reader.nextLine();
                cl2 = cl1.split(",");
                points = new int[][]{new int[]{Integer.valueOf(cl2[0]),Integer.valueOf(cl2[1])}, new int[]{Integer.valueOf(cl2[2]),Integer.valueOf(cl2[3])}};
            } catch(FileNotFoundException e){
                System.out.println(e);
            }
        }
        return points;
    }

    protected void select(Clicked c, Keys k, javax.swing.JPanel scr){
        if (k.keys.contains(KeyEvent.VK_B)){
            editMode = EditModes.BOUNDING;
            player.selected = false;
            for (Block b : blockMan.BlockList){
                b.selected=false;
            }
        }
        if (c.Mbuttons.contains(java.awt.event.MouseEvent.BUTTON1)){
            //unselect everything
            editMode = EditModes.NONE;
            player.selected = false;
            for (Block b : blockMan.BlockList){
                b.selected=false;
            }
            //find what was selected
            int[] mousepos = c.getmousepos(scr);
            if (mousepos[0]>player.x-player.w/2-cam.completeOffset[0] && mousepos[0]<player.x+player.w/2-cam.completeOffset[0] 
                && mousepos[1]>player.y-player.h/2-cam.completeOffset[1] && mousepos[1]<player.y+player.h/2-cam.completeOffset[1]){
                player.selected=true;
                editMode = EditModes.PLAYER;
            } else {
                for (Block b : blockMan.BlockList){
                    if ((mousepos[0]>b.x-b.w/2-cam.completeOffset[0] && mousepos[0]<b.x+b.w/2-cam.completeOffset[0] 
                        && mousepos[1]>b.y-b.h/2-cam.completeOffset[1] && mousepos[1]<b.y+b.h/2-cam.completeOffset[1])){
                        b.selected=true;
                        editMode = EditModes.PLATFORM;
                    }
                }
            }
        } 
    }

    protected void edit(Clicked c, Keys k, JPanel scr){
        select(c,k,scr);
        cam.screenSize(scr);
        cam.calcComplOffset();
        switch (editMode){
            case PLATFORM:
                blockMan.editMove(k);
                editTimer = blockMan.changeType(k, editTimer);
                break;
            case PLAYER:
                player.editMove(k);
                break;
            case BOUNDING:
                bound.editMove(k);
                break;
            case NONE:
                cam.editMove(k);
                break;
            default:
                break;
        }
        delete(k);
        if (blockMan.addBlock(k, editTimer)){
            editTimer = 0;
            editMode = EditModes.PLATFORM;
        }
        saveLevel(k);

        if (editTimer < 30){
            editTimer++;
        }
    }

    protected void delete(Keys k){
        if (k.keys.contains(KeyEvent.VK_M) && !(editMode == EditModes.NONE) && editTimer==30){
            switch (editMode){
                case PLATFORM:
                    blockMan.deleteBlock();
                case ENEMY:
                    break; //implement later
                default:
                    break;
            }
            editMode = EditModes.NONE;
            editTimer = 0;
        }
    }

    protected void draw_selected(Graphics2D g){
        g.setColor(Color.GREEN);
        if (player.selected){
            g.drawRect((int) player.x-player.w/2 - cam.completeOffset[0], (int) player.y-player.h/2 - cam.completeOffset[1], player.w, player.h);
        } else{
            for (Block b : blockMan.BlockList){
                if (b.selected){
                    g.drawRect((int) b.x-b.w/2 - cam.completeOffset[0], (int) b.y-b.h/2 - cam.completeOffset[1], b.w, b.h);
                }
            }
        }
    }

    protected void saveLevel (Keys k){
        if (k.keys.contains(java.awt.event.KeyEvent.VK_ENTER)){
            File file = fileget(currentLevel);
            try (java.io.FileWriter writer = new java.io.FileWriter(file)){
                writer.write(String.valueOf(player.x)+','+String.valueOf(player.y)+"\n");
                writer.write(String.valueOf(bound.topLeft[0])+','+String.valueOf(bound.topLeft[1])+','+String.valueOf(bound.botRight[0])+','+String.valueOf(bound.botRight[1])+"\n");
                for (Block b : blockMan.BlockList){
                    writer.write(String.valueOf(b.x)+','+String.valueOf(b.y)+','+String.valueOf(b.w)+','+String.valueOf(b.h)+','+String.valueOf(b.t)+"\n");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
