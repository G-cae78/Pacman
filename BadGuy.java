import java.awt.*;
import static java.lang.Math.abs;
import static java.lang.Math.min;

import java.util.ArrayList;
import java.util.Stack;
public class BadGuy {
    Image bad;
    int x=0,y=0;
    int prevX=0, prevY=0;

    ArrayList<Node> openList=new ArrayList<>();
    ArrayList<Node> closedList=new ArrayList<>();
    Stack<Node> finalPath= new Stack<>();
    boolean haspath=false;


    public BadGuy(Image b, int x, int y){
        this.bad=b;
        this.x=x;
        this.y=y;
    }
    public boolean reCalcPath(boolean map[][],int targx, int targy){
        Node[][] nodes = new Node[map.length][map[0].length];
        Node startnode= new Node(x,y);
        Node targetNode= new Node(targx,targy);
        Node nextNode= startnode;
        int minF=Integer.MAX_VALUE;
        boolean solvable=false;

        openList.clear();
        closedList.clear();
        finalPath.clear();

        for(int x=0;x<40;x++){
            for(int y=0;y<40;y++){
                nodes[x][y]=new Node(x,y);
                if(map[x][y]){
                    closedList.add(nodes[x][y]);
                }
            }
        }
        openList.add(startnode);
        while(!openList.isEmpty()){
            minF=Integer.MAX_VALUE;
            for(Node node: openList){
                if(node.f<minF){
                    nextNode=node;
                    minF=node.f;
                }
            }

            if(nextNode.equals(targetNode)){
                createPath(nextNode);
                solvable=true;
                break;
            }

            expandNode(nodes,nextNode,targetNode);
            closedList.add(nextNode);
            openList.remove(nextNode);
        }
        if(solvable){
            return true;
        }
        return false;


    }

    public void createPath(Node nextStep){
        if(nextStep==null){
            return;
        }
        else{
            Node step=nextStep.parent;
            finalPath.push(step);
            createPath(nextStep.parent);
        }
    }

    public void expandNode(Node[][] nodes, Node nextNode, Node target){
        nodes[nextNode.x][nextNode.y].isExpanded=true;

            for(int i=-1;i<=1;i++){
                for(int j=-1;j<=1;j++){
                    if((i==0 && j==0) || (i!=0 && j!=0)){
                        continue;
                    }
                    int newX= nextNode.x+i;
                    int newY= nextNode.y+j;
                    if(newX>=0 && newX<nodes.length && newY>=0 && newY <nodes[0].length){
                        Node beside= nodes[newX][newY];
                        if(!closedList.contains(beside) && !openList.contains(beside)){
                            openList.add(beside);
                            beside.parent=nextNode;
                            beside.h=calculateManhattanDistance(beside,target);
                            beside.calculateG();
                            beside.calculateF();
                        }
                    }
                }
            }
    }

    private int calculateManhattanDistance(Node node, Node target){
        return abs(node.x- target.x)+abs(node.y-target.y);
    }
    public void move(boolean map[][],int targetx,int targety ){
         if(prevX!=targetx && prevY!=targety){
             if(reCalcPath(map,targetx,targety)){
                 haspath=true;
             }
             prevY=targety;
             prevX=targetx;
         }
          else if(haspath && !finalPath.isEmpty()){
             Node nextNode=finalPath.pop();
             if(nextNode!=null){
                 x=nextNode.x;
                 y=nextNode.y;
             }
         }
         else{
             int newx=x, newy=y;
             if(targetx<x)
                 newx--;
             else if(targetx>x)
                 newx++;
             if(targety<y)
                 newy--;
             else if (targety>y)
                 newy++;
             if(!map[newx][newy]){
                 x=newx;
                 y=newy;

             }
         }
    }

    public void paint(Graphics g){
        g.drawImage(bad,x*20,y*20,null);
    }
}
