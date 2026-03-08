package Little_Platformer;

import java.util.List;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.awt.Color;
import java.awt.Graphics2D;

import Little_Platformer.Blockmanager.Block;
import Little_Platformer.Blockmanager.BlockType;

public class LevelManager {
    Blockmanager blockMan = new Blockmanager();
    Player player = new Player(30, 50);
    boolean won = false;

    int editTimer = 30;

    Levels currentLevel = Levels.TEST;

    public enum Levels{
        TEST
    }

    public enum EditModes{
        NONE,
        PLATFORM,
        PLAYER
    }

    EditModes editMode = EditModes.NONE;

    public class Camera{
        int x; //centre X
        int y; //centre Y
    }

    Camera cam = new Camera();

    public void gameupdate(List<Integer> keys){
        player.move(keys);
        player.momentum(keys);
        won = player.collision(blockMan);
    }

    public void gamedraw(Graphics2D G){
        player.draw(G);
        blockMan.draw(G);
    }

    public boolean won(){
        if (won){
            won=false;
            return true;
        } else {
            return false;
        }
    }

    public void levLoad(){
        blockMan.BlockList.clear();
        if (currentLevel == Levels.TEST){
            blockMan.BlockList.addAll(LevFileRead(currentLevel));
            player.spawn(getplayercoords(currentLevel));
        }
        blockMan.Amount = blockMan.BlockList.size();
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

    public void select(Clicked c, javax.swing.JPanel scr){
        if (c.Mbuttons.contains(java.awt.event.MouseEvent.BUTTON1)){
            //unselect everything
            editMode = EditModes.NONE;
            player.selected = false;
            for (Block b : blockMan.BlockList){
                b.selected=false;
            }
            //find what was selected
            int[] mousepos = c.getmousepos(scr);
            if (mousepos[0]>player.x-player.w/2 && mousepos[0]<player.x+player.w/2 && mousepos[1]>player.y-player.h/2 && mousepos[1]<player.y+player.h/2){
                player.selected=true;
                editMode = EditModes.PLAYER;
            } else {
                for (Block b : blockMan.BlockList){
                    if ((mousepos[0]>b.x-b.w/2 && mousepos[0]<b.x+b.w/2 && mousepos[1]>b.y-b.h/2 && mousepos[1]<b.y+b.h/2)){
                        b.selected=true;
                        editMode = EditModes.PLATFORM;
                    }
                }
            }
        } 
    }

    public void edit(Clicked c, Keys k, javax.swing.JPanel scr){
        select(c,scr);
        switch (editMode){
            case PLATFORM:
                blockMan.editMove(k);
                editTimer = blockMan.changeType(k, editTimer);
                break;
            case PLAYER:
                player.editMove(k);
                break;
            default:
                break;
        }
        editTimer = blockMan.addBlock(k, editTimer);
        saveLevel(k);

        if (editTimer < 30){
            editTimer++;
        }
    }

    public void draw_selected(Graphics2D g){
        g.setColor(Color.GREEN);
        if (player.selected){
            g.drawRect((int) player.x-player.w/2, (int) player.y-player.h/2, player.w, player.h);
        } else{
            for (Block b : blockMan.BlockList){
                if (b.selected){
                    g.drawRect((int) b.x-b.w/2, (int) b.y-b.h/2, b.w, b.h);
                }
            }
        }
    }

    public void saveLevel (Keys k){
        if (k.keys.contains(java.awt.event.KeyEvent.VK_M)){
            File file = fileget(currentLevel);
            try (java.io.FileWriter writer = new java.io.FileWriter(file)){
                writer.write(String.valueOf(player.x)+','+String.valueOf(player.y)+"\n");
                for (Block b : blockMan.BlockList){
                    writer.write(String.valueOf(b.x)+','+String.valueOf(b.y)+','+String.valueOf(b.w)+','+String.valueOf(b.h)+','+String.valueOf(b.t)+"\n");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
