import java.awt.*;

public class Pacman {
    Image pacman;
    int x=0,y=0;
    int xSpeed=0, ySpeed=0;
    public Pacman(Image p,int x,int y){
        this.pacman=p;
        this.x=x;
        this.y=y;
    }
    public void setXSpeed(int speed){
        // Indicating movement along x axis by changing xspeed by -1 or 1
        xSpeed=speed;
    }
    public void setYSpeed(int speed){
        // Indicating movement along y axis by changing yspeed by -1 or 1
        ySpeed=speed;
    }

    public void move(boolean map[][]) {
        //moving pacman each time an arrow key is pressed
        int newx=x+xSpeed;
        int newy=y+ySpeed;
        if (!map[newx][newy]) {
            x=newx;
            y=newy;
        }
    }

    public int getWidth(){
        //getting dimension of pacman object width
        return pacman.getWidth(null);
    }

    public int getHeight(){
        //getting dimension of pacman object height
        return pacman.getHeight(null);
    }
    public void paint(Graphics g){
        g.drawImage(pacman,x*20,y*20,null);
    }

}
