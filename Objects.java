import java.awt.*;
import java.awt.Image;
public class Objects{
     int x=0, y=0;
     Image gameObject;
     boolean eaten;

    public Objects(Image i,int x,int y){
        this.x=x;
        this.y=y;
        this.gameObject=i;
        this.eaten=false; // initialising eaten to false
    }
    public void setEaten(){
        //setting eaten to true after collision  with pacman object
        this.eaten=true;
    }
    public boolean isEaten(){
        //Checking if object has been eaten by pacman
        return eaten;
    }
    public int getWidth(){
        //getting image width dimensions
        return gameObject.getWidth(null);
    }

    public int getHeight(){
        //getting image height dimensions
        return gameObject.getHeight(null);
    }


    //Collsion detection algorithm with pacman
    public boolean collisionCheck(Pacman pacman){
        if(((pacman.x < x && pacman.x + pacman.getWidth() > x) || (pacman.x > x && x + getWidth() > pacman.x)) && ((pacman.y < y && pacman.y + pacman.getHeight() > y || (y < pacman.y && y + getHeight() > pacman.y)))){
            setEaten();
            return true;
        }
            return false;
    }
    // Painting object to screen if not eaten
    public void paint(Graphics g){
        if(!eaten)
        g.drawImage(gameObject,x*20,y*20,null);
    }
}
