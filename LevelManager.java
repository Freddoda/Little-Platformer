package Little_Platformer;

import java.util.List;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import Little_Platformer.Blockmanager.Block;
import Little_Platformer.Blockmanager.BlockType;

public class LevelManager {
    Blockmanager blockMan = new Blockmanager();
    Player player = new Player(30, 50);
    boolean won = false;

    public enum Levels{
        TEST
    }

    public class Camera{
        int x;
        int y;
    }

    Camera cam = new Camera();

    public void gameupdate(List<Integer> keys){
        player.move(keys);
        player.momentum(keys);
        won = player.collision(blockMan);
    }

    public boolean won(){
        if (won){
            won=false;
            return true;
        } else {
            return false;
        }
    }

    public void levLoad(Levels lev){
        blockMan.BlockList.clear();
        if (lev == Levels.TEST){
            blockMan.BlockList.addAll(LevFileRead(lev));
            player.spawn(getplayercoords(lev));
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

    public void saveLevel (Keys k, Levels lev){
        if (k.keys.contains(java.awt.event.KeyEvent.VK_P)){
            File file = fileget(lev);
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
