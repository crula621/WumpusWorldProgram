package LCAssignment2;
import java.util.*;

/**
 * @author Lachlan Cruickshank
 * the main class that sets up and initializes the wumpus dungeon with walls and 
 * wumpus', It then searches the dungeon for the closest wumpus and prints out the directions to get to it.
 */

public class WumpusDungeon{
   
   
   int N=5,S=-5,E=1,W=-1;
   int size =25;
   Room[] world;
   ArrayList<Integer> traveled;
   int[] haveVisited;
   int currentPosition;
 
    public class Room{
      int wall=0;
      int steps=-1,previousRoom=-1;
      boolean wumpus=false;     
      
      public void setWumpus(){
         this.wumpus=true;    
      }
      
       public boolean isWumpus(){
         return wumpus;
      }       
      
      public void setWall(int wall){
         this.wall=wall;
      }   
   } 
   
   
    
    /*WumpusDungeon method that initializes all of the dungeon to having Rooms per number in the array
     * it then sets each Room that needs a wall with it to the corresponding direction.
     */
   public WumpusDungeon(){
  System.out.println("You are entering a wumpus dungeon");
      haveVisited = new int[size];
        world = new Room[size]; 
      for(int i=0;i<size;i++){
         world[i] = new Room();
      }
      
      
      world[3].setWall(E);
      world[4].setWall(W);
      world[20].setWall(S);
      world[15].setWall(N);
      world[21].setWall(E);
      world[22].setWall(W);
      world[16].setWall(E);
      world[17].setWall(W);
      world[13].setWall(S);
      world[8].setWall(N);
      world[14].setWall(S);
      world[9].setWall(N);
      
      world[4].setWumpus();
      world[17].setWumpus();
      world[20].setWumpus();
      world[24].setWumpus();
      
      
      
      currentPosition = 0;   
      traveled = new ArrayList<Integer>();
      int closestWumpus = wumpusSearch(currentPosition);
      
      System.out.println("The closest Wumpus is in Room "+closestWumpus);
      
      for(int i=closestWumpus; world[i].previousRoom!=-1; i=world[i].previousRoom)
         
        traveled.add(i);     
      traveled.add(0);
 
   
      System.out.println("Directions to the closest wumpus are: ");
      
      for(int i = traveled.size()-1; i > 0; i--){
         int direction = traveled.get(i-1) - traveled.get(i);
        
         if(direction ==E)
            System.out.println("East");
         else if(direction ==N)
            System.out.println("North");
         else if(direction ==S)
            System.out.println("South");
         else if(direction ==W)
            System.out.println("West");
      }
      System.out.println("You have found a wumpus");
      
   }
   
   /**
    * wumpusSearch is used to progress through the rooms in the wumpus dungeon
    * by setting closestWumpus to the result of a DepthLimitedSearch
    */
   
   
   public int wumpusSearch(int position){
     int steps = 0,closestWumpus;
     while(steps<size){
       for(int i=0;i<size;i++){
         
         haveVisited[i]=0;
         world[i].steps=-1;
       }
       
       closestWumpus = DLS(position , steps);
       if(closestWumpus!=-1)
         
         if(world[closestWumpus].isWumpus()){
         
         return closestWumpus;
      }
       steps++;
     }
     return 0;
 }
   
   
   /*
    * DepthLimitedSearchMethod
    * I used some pseudocode i found on the internet to help me code this becouse i was previously
    * unfamiliar with it. 
    */
   
   public int DLS(int start,int steps){
     int sidestep = 5;
     ArrayList<Integer> stack = new ArrayList<Integer>();    
     stack.add((Integer)start);
     world[start].steps=0;
     haveVisited[start]=1;
     while(true){ 
        int end = stack.get(stack.size()-1);
       stack.remove((Integer)end);       
       currentPosition+=(end-currentPosition); //Successor State Function
       
       if(stack.isEmpty())
           if(currentPosition!=start){
            for(int i=2;i<size; i++){
                 haveVisited[i]=0;
            }
         }
          if(haveVisited[currentPosition]==1){
            haveVisited[currentPosition]=2;
            
            if(world[currentPosition].steps == -1)
              world[currentPosition].steps= world[world[currentPosition].previousRoom].steps+ 1;
            
            if(world[currentPosition].isWumpus()){
              return currentPosition;
            }    
            if(world[currentPosition].steps < steps){
              
               for(int i=-1;i<= 1;i+= 2){
                 int west = currentPosition-1;
                 int east = currentPosition+1;
                  int westEast = currentPosition+i;
                  int north= currentPosition+sidestep;
                  int south = currentPosition-sidestep;
                   int northSouth = currentPosition+i *sidestep;             
                    
                  if(currentPosition+i*sidestep>=0 && currentPosition+i*sidestep<size)
                      if( haveVisited[northSouth]==0) 
                    if (northSouth != currentPosition+world[currentPosition].wall){
                         
                     
                     world[northSouth].previousRoom=currentPosition;
                     stack.add(northSouth);
                     haveVisited[northSouth]=1;
                     
                  }
                  
                     if(westEast != currentPosition +world[currentPosition].wall) 
                   
                           if( westEast>=0 && westEast<size)
                                if(haveVisited[westEast]==0){
                       
                        stack.add(westEast);
                        haveVisited[westEast]=1;
                        world[westEast].previousRoom=currentPosition;
                     }
                             
               }
            }
         }
          if(stack.isEmpty())
        return -1;
       }
   }
}


