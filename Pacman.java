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
        xSpeed=speed;
    }
    public void setYSpeed(int speed){
        ySpeed=speed;
    }

    public void move(boolean map[][]) {
        int newx=x+xSpeed;
        int newy=y+ySpeed;
        if (!map[newx][newy]) {
            x=newx;
            y=newy;
        }
    }

    public int getWidth(){
        return pacman.getWidth(null);
    }

    public int getHeight(){
        return pacman.getHeight(null);
    }
    public void paint(Graphics g){
        g.drawImage(pacman,x*20,y*20,null);
    }

}
