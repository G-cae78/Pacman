import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferStrategy;
import java.io.*;

public class Game extends JFrame implements Runnable, KeyListener, MouseListener, MouseMotionListener {

    private BufferStrategy strategy; //using buffer to prevent flickering
    private boolean map[][]= new boolean[40][40]; // initialising 40x40 grid map for game
    /* Initialisjng objects*/
    private Pacman pacman;
    private BadGuy yellow;
    private BadGuy red;
    private BadGuy blue;
    private Objects[] coin= new Objects[10000];
    private boolean isGameRunning= false;
    boolean isInitialised=false;

    private BadGuy pink;
    private Graphics offscreenBuffer;
    ImageIcon icon= new ImageIcon("/Users/georgea.e/Documents/SecondYear2/Next Gen game dev/Pacman/src/Pacman.jpeg");
    public Game(){
        /* Initialing pop up window for game to run on screen*/
        Dimension screensize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        int x= screensize.width/2 -400;
        int y= screensize.height/2-400;
        setBounds(x,y,800,800);
        setVisible(true);
        this.setTitle("Pacman");


        /* Instantiating game objects and positioning them on accordingly on map*/
        Image img1= icon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH); // Resize the image;
        pacman=new Pacman(img1,18,18);

        icon= new ImageIcon("/Users/georgea.e/Documents/SecondYear2/Next Gen game dev/Pacman/src/yellow.png");
        Image img2= icon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH); // Resize the image;
        yellow=new BadGuy(img2,1,36);

        icon= new ImageIcon("/Users/georgea.e/Documents/SecondYear2/Next Gen game dev/Pacman/src/red.png");
        Image img3= icon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH); // Resize the image;;
        red=new BadGuy(img3,36,36);

        icon= new ImageIcon("/Users/georgea.e/Documents/SecondYear2/Next Gen game dev/Pacman/src/pink.png");
        Image img4= icon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH); // Resize the image;;
        pink=new BadGuy(img4,1,3);

        icon= new ImageIcon("/Users/georgea.e/Documents/SecondYear2/Next Gen game dev/Pacman/src/blue.png");
        Image img5= icon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH); // Resize the image;;
        blue=new BadGuy(img5,36,3);


        //Starting Thread
        Thread t= new Thread(this);
        t.start();

        createBufferStrategy(2);
        strategy= getBufferStrategy();
        offscreenBuffer= strategy.getDrawGraphics();

        // Initialising mouse and key listeners for controlling game characters and communicating with game interface
        addMouseListener(this);
        addMouseMotionListener(this);
        addKeyListener(this);

        //Initialising the front buffer to false so map is not showing till game is initialised
        for(x=0;x<40;x++){
            for(y=0;y<40;y++){
                map[x][y]=false;
            }
        }

       //System.out.println(count);
         isInitialised=true;
    }

    public void run(){
        long loops=0;
        while(true) {
            try {
                Thread.sleep(200);//Moving game objects every 200 milliseconds
            } catch (InterruptedException e) {
            }

            if (isGameRunning) {
                /*Everytime pacman moves use A* pathfinding to recalculate shortest distance to pacman
                for all four ghosts as long as game is started
                 */
                pacman.move(map);
                if (loops % 5 == 0) {
                    red.move(map, pacman.x, pacman.y);
                    blue.move(map, pacman.x, pacman.y);
                }
                if (loops % 6 == 0) {
                    yellow.move(map, pacman.x, pacman.y);
                    pink.move(map, pacman.x, pacman.y);
                }
            }
//            for(Objects obj : coin){
//                if(obj!=null && !obj.isEaten()){
//                    obj.collisionCheck(pacman);
//                }
//            }
           this.repaint();
        }
    }
    public void load(){
        String textInput=null;
        /*Use buffer file reader to regenerate map saved to file previously

         */
        try{
            BufferedReader reader=new BufferedReader(new FileReader("/Users/georgea.e/Documents/SecondYear2/Next Gen game dev/Pacman/src/mazeLoad.txt"));
            textInput=reader.readLine();
            reader.close();
        }
        catch (IOException e){}

            if(textInput!=null){
                for(int x=0;x<40;x++){
                    for(int y=0;y<40;y++){
                        map[x][y]=(textInput.charAt(x*40+y)=='1'); //set map front buffer to true(wall) if 1 has been saved to file
                    }
                }
            }

        // Loading coins after map has been displayed on front buffer to Game user
        int count=0;
        for(int x=0;x<40;x++){
            for(int y=0;y<40;y++){
                icon= new ImageIcon("/Users/georgea.e/Documents/SecondYear2/Next Gen game dev/Pacman/src/coins.png");
                Image img6=icon.getImage().getScaledInstance(3,3,Image.SCALE_SMOOTH);
                if(!map[x][y]){
                    coin[count]=new Objects(img6,x,y);
                    count++;
                }
            }
        }
        System.out.println("num coins "+count);

    }

    public void save(){
        String outputText="";
        //Saving the presence of a wall to file as a 1 and absence as a 0 to outputText String
        //traversing map and saving figures to output string
        for(int x=0;x<40;x++){
            for(int y=0;y<40;y++){
                if(map[x][y]){
                    outputText+="1";
                }
                else{
                    outputText+="0";
                }
            }
        }
        // Using Buffer file writer to write content of Output string to file
        try{
            BufferedWriter writer=new BufferedWriter(new FileWriter("/Users/georgea.e/Documents/Second Year 2/Next Gen game dev/Pacman/src/mazeLoad.txt"));
            writer.write(outputText);
            writer.close();
        }
        catch(IOException e){

        }
    }
    int prevx=-1, prevy=-1;
    public void mousePressed(MouseEvent e){
        if (!isGameRunning) {
            // was the click on the 'start button'?
            int x = e.getX();
            int y = e.getY();
            if (x>=15 && x<=85 && y>=40 && y<=70) {
                isGameRunning=true;
                return;
            }
            // or the 'load' button?
            if (x>=315 && x<=385 && y>=40 && y<=70) {
                load();
                return;
            }
            // or the 'save' button?
            if (x>=415 && x<=485 && y>=40 && y<=70) {
                save();
                return;
            }
        }
        /* Each square is 20x20 so this makes sure we are interacting with the right square on the
           map by dividing by 20
         */
        int x=e.getX()/20;
        int y=e.getY()/20;

        map[x][y]=!map[x][y];
        this.repaint();
        prevx=x;
        prevy=y;
    }

    public void mouseDragged(MouseEvent e){
        /* Enabling user to drag across squares to create walls instead of having to click on all of them
           individually
         */
        int x=e.getX()/20;
        int y=e.getY()/20;
        if(x!=prevx || y!=prevy){
            map[x][y]=!map[x][y];
            this.repaint();
            prevx=x;
            prevy=y;
        }
    }
    public void mouseReleased(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
    public void mouseClicked(MouseEvent e) {}
    //
    // mouse events which must be implemented for MouseMotionListener
    public void mouseMoved(MouseEvent e) {}


    public void keyPressed(KeyEvent e){
        /* Controlling movement of pacman object

         */
        if(e.getKeyCode()==KeyEvent.VK_LEFT)
            pacman.setXSpeed(-1);
        else if (e.getKeyCode()==KeyEvent.VK_RIGHT)
            pacman.setXSpeed(1);
        else if (e.getKeyCode()==KeyEvent.VK_UP)
            pacman.setYSpeed(-1);
        else if (e.getKeyCode()==KeyEvent.VK_DOWN)
            pacman.setYSpeed(1);

        //Checking for collision with coins and giving points per coin
        for(Objects obj : coin){
            if(obj!=null && !obj.isEaten()){
                obj.collisionCheck(pacman);
            }
        }

    }
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode()==KeyEvent.VK_LEFT || e.getKeyCode()==KeyEvent.VK_RIGHT)
            pacman.setXSpeed(0);
        else if (e.getKeyCode()==KeyEvent.VK_UP || e.getKeyCode()==KeyEvent.VK_DOWN)
            pacman.setYSpeed(0);
    }
    public void keyTyped(KeyEvent e) { }

    public void paint(Graphics g){
        if(!isInitialised){
            return;
        }
        g=offscreenBuffer;
        g.setColor(Color.BLACK);
        g.fillRect(0,0,800,800);
        g.setColor(Color.BLUE);
        for(int x=0; x<40;x++){
            for(int y=0;y<40;y++){
                if(map[x][y]){
                    g.fillRect(x*20,y*20,20,20);
                }
            }
        }
        // Painting image of objects to the screen
        pacman.paint(g);
        pink.paint(g);
        red.paint(g);
        yellow.paint(g);
        blue.paint(g);

        /* removing eaten coins from screen*/
       for(Objects obj:coin){
           if(obj!=null) {
               if(!obj.isEaten())
               obj.paint(g);
           }
           else{
               break;
           }
       }

       // Creating buttons to interact with game
        g.setColor(Color.GREEN);
        g.fillRect(15, 30, 70, 30);
        g.fillRect(315, 30, 70, 30);
        g.fillRect(415, 30, 70, 30);
        g.setFont(new Font("Times", Font.PLAIN, 24));
        g.setColor(Color.BLACK);
        g.drawString("Start", 22, 60);
        g.drawString("Load", 322, 60);
        g.drawString("Save", 422, 60);

        //displaying front buffer to screen
        strategy.show();
    }
    public static void main(String[] args) {
        Game start= new Game();
    }
}