import java.awt.*;
import static java.lang.Math.abs;
import static java.lang.Math.min;

import java.util.ArrayList;
import java.util.Stack;
public class BadGuy {
    Image bad;
    int x=0,y=0;
    int prevX=0, prevY=0;

    /* Fileds for A* Pathfinding

     */

    ArrayList<Node> openList=new ArrayList<>(); // ArrayList to store nodes on open list
    ArrayList<Node> closedList=new ArrayList<>(); // ArrayList to store nodes that have been expanded already

    /* Used stack because of LIFO property so the ghost goes from parent node to parent node
       Till it reaches target(Pacman)
     */
    Stack<Node> finalPath= new Stack<>(); //Stack to store final path after being calculated path
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

        //Clearing stacks and arrayList each time a path is recalculated
        openList.clear();
        closedList.clear();
        finalPath.clear();

        //Instantiating map as node objects
        for(int x=0;x<40;x++){
            for(int y=0;y<40;y++){
                nodes[x][y]=new Node(x,y);
                if(map[x][y]){
                    closedList.add(nodes[x][y]);
                }
            }
        }
        //declare current position as start node and start calculation
        openList.add(startnode);

        while(!openList.isEmpty()){
            minF=Integer.MAX_VALUE;
            for(Node node: openList){
                // Use node with least F from open list as next node
                if(node.f<minF){
                    nextNode=node;
                    minF=node.f;
                }
            }

            if(nextNode.equals(targetNode)){
                /* If next node is the target node
                   start backtracking by storing each nodes parent node in stack
                   and store final path
                 */
                createPath(nextNode);
                solvable=true;
                break;
            }

            //Else expand nextnode and add to closed list
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
        //start backtracking by storing each nodes parent node in stack
        //and store final path
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

        /*expand by adding node -1 and +1 both x and y axis from current node
          and calculating F G and H of these nodes
         */
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
        //CalclateManhattanDistance using formula below
        return abs(node.x- target.x)+abs(node.y-target.y);
    }
    public void move(boolean map[][],int targetx,int targety ){

        /* here a new path is calculated everytime the pacman moves
           and if there is a path pop the final path calculated
           and the ghost uses it to move accordingly towards pacman until he is
           caught.
         */
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
