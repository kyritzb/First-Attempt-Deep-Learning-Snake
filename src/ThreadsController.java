import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;
//Controls all the game logic .. most important class in this project.
public class ThreadsController extends Thread {
	 public static ArrayList<ArrayList<DataOfSquare>> Squares= new ArrayList<ArrayList<DataOfSquare>>();
	 
	 int sizeSnake=7;
	 final int orgSize = 7;
	 long speed = 1;
	 public static int directionSnake;
	 public static long time = 0;
	 public static int mutationNum = 0;
	 public static boolean controlEnabled = true;
	 public static int generation= 0;
	 public static int gameCount = 0;
	 public static double averageOfGen = 0;
	 public static boolean playing = true;
	 public static int score;
	 public static double distance;
	 public static boolean hasDied = false;
	 public static boolean isTraining= false;
	 public static boolean needsRestart = false;
	 public static int highestScore = -1000;
	 public static int aiMove = 1;
	 ArrayList<Tuple> positions = new ArrayList<Tuple>();
	 Tuple foodPosition;
	 Tuple headSnakePos;
	 Tuple headPos;
	 private Tuple pos;
	 private Tuple positionDepart;
	 public static Data data;
	 
	 public static ArrayList<Network> nets = new ArrayList<Network>();
	 public static ArrayList<Double> scores = new ArrayList<Double>();
	 public static ArrayList<Integer> inputs = new ArrayList<Integer>();
	 
	 public static Matrix output;
	 final int amntSnakes = 20;
	 final int AMNTTOKILL = amntSnakes/2;
	 //--------------------------------------------------------------------------------------
	 //Constructor of ControlleurThread
	 //--------------------------------------------------------------------------------------
	 public ThreadsController(Tuple positionDepart){
		//Get all the threads
		Squares=Window.Grid;
		pos = positionDepart;
		headSnakePos=new Tuple(positionDepart.x,positionDepart.y);
		directionSnake = 1;
		this.positionDepart = positionDepart;
		//!!! Pointer !!!!
		headPos = new Tuple(headSnakePos.getX(),headSnakePos.getY());
		positions.add(headPos);
		score = 0;
		foodPosition= new Tuple(15,15);
		spawnFood(foodPosition);

		distance = 0;
	    data= new Data(); 
	    data.initialize();
	    
		for(int i = 0; i<amntSnakes; i++)
	    {
	    	nets.add(new Network(24, 
	    			new int[] {16,3 },  //Direction codes | 0:left 1:Right 2:Straight
	    			new Network.ActivationFunction[]{ 
	    						Network.ActivationFunction.TANH,    //both layers with ... 
	    						Network.ActivationFunction.TANH})  //... no activation function 
	    			);
	    	nets.get(i).seedWeights(-1, 1);
	    }
	    //System.out.println(nets.get(0));
	    
	 }
	 //--------------------------------------------------------------------------------------
	 //MAIN LOOP
	 //--------------------------------------------------------------------------------------
	 public void run() {
		 while(true)
		 {
			 data.update(positions.get(positions.size()-1), foodPosition, directionSnake, sizeSnake, time, positions);
			 data.draw();
			 
			 //game---------------------------
			 if(data.train.isClicked())
			 {
				isTraining = true;
				playing = false;
				hasDied = false;
				controlEnabled = false;
				gameCount = 0;
				restart();
			 }
			//--------------------------------------------------------------------------------------------
			 if(isTraining)
			 {
				 if(gameCount<amntSnakes)
				 { 
					 if(hasDied)
					 {
						 //System.out.println(score);
						 hasDied = false;
						 if(score > highestScore)
							 highestScore = score;
						 scores.add(new Double(score));
						 gameCount++;
						 restart();
						 
					 }
					 else //is still playing
					 {
						final Matrix input = new Matrix(1, 24);
						
					    for(int set=0; set<input.getWidth(); set++) 
					    {
					        input.set(0, set, inputs.get(set).intValue());
					    }
					    
					    output = nets.get(gameCount).forward(input);
						//System.out.println( output+ "\n"); 
						double highest = output.get(0,0);
						int index = 0;
						for(int i = 0; i<3; i++)
						{
							if(output.get(0, i) > highest)
							{
								highest = output.get(0, i);
								index = i;
							}
						}
						//--------------------------------------
						//Direction codes | 0:left 1:Right 2:Straight
						System.out.println(aiMove);
						if(aiMove == 0)
						{
							if(index == 0) //left
							{
								aiMove = 3;
							}
							else if(index == 1)//right 
							{
								aiMove = 4;
							}
							else if(index == 2)//straight
							{
								aiMove = 1;
							}
						}
						else if(aiMove == 1)
						{
							if(index == 0)//left
							{
								aiMove = 3;
							}
							else if(index == 1)//right
							{
								aiMove = 4;
							}
							else if(index == 2)//straight
							{
								aiMove = 2;
							}
						}
						else if(aiMove == 2)
						{
							
							if(index == 0)//left
							{
								aiMove = 0;
							}
							else if(index == 1)//right
							{
								aiMove = 1;
							}
							else if(index == 2)//straight
							{
								aiMove = 2;
							}
						}
						else if(aiMove == 3)
						{
							if(index == 0)//left
							{
								aiMove = 2;
							}
							else if(index == 1)//right
							{
								aiMove = 1;
							}
							else if(index == 2)//straight
							{
								aiMove = 4;
							}
						}
						double newDistance = Math.pow(Math.pow(headSnakePos.getX() - foodPosition.getX(), 2) + Math.pow(headSnakePos.getY() - foodPosition.getY(), 2),0.5);
						if(newDistance > distance)
							score-= 1.5;
						else
							score++;
						distance = newDistance;
						if(score<-250)
							stopTheGame();
					 }
				 }
				 else
				 {
					 sortScores();
					 kill();
					 reproduce();
					 scores.clear();
					 generation++;
				 }
				 //System.out.println(net.forward(trainInput) + "\n"); 
			 } 
			 if(playing || isTraining)
			 {
				 if(controlEnabled)
					 moveInterne(directionSnake); 
				 else
					 moveInterne(aiMove);
				 checkCollision();
				 moveExterne();
				 deleteTail();
		       // score += (time / 1000);
		        	
			 }
			 
			 if(data.restart.isClicked() && !playing)
			 {
				 restart();
			 }
			 if(needsRestart)//player used space key
			 {
				 needsRestart = false;
				 restart();
			 }
			 pauser();
		 }
	 }
	 public void writeData() 
	 {

		 
			try {

			    
				File file = new File("C:\\Users\\Bryan\\eclipse-workspace\\SnakeGame\\snakeData.txt");
				FileWriter fileWriter = new FileWriter(file,true);
			    BufferedWriter out;
			    out = new BufferedWriter(fileWriter);
				out.write(nets.get(nets.size()-1).toString());
				out.newLine();
				out.write("--------------------------------------------------------------------------------------------------------");
				out.newLine();
				out.write("out:" + output.toString());
				out.newLine();
				out.write("Decision: " + aiMove);
				out.newLine();
			
				out.flush();
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
	 }
	 //--------------------------------------------------------------------------------------
	 //uses the top 5 networks in order to generate a similar network, with mutations
	 //--------------------------------------------------------------------------------------
	 public void reproduce()
	 {
		 gameCount = 0;
		 final double mutationRate = 0.01; // 5 percent
		 final double mutationAmnt = 0.01; //amnt that changes
		 for(int k = 0; k<AMNTTOKILL; k++) //goes through each snake
		 {
			 //int parent = (int) (Math.random() *5);
			 int parent = nets.size()-1; //best
			 Network temp = new Network(nets.get(nets.size()-1));
			 //System.out.print("-------------------------------------");
			 for(int w = 0; w<temp.getWeights().length; w++)  //goes through layers of neuron weights(2)
			 {
			    for(int j=0; j<nets.get(parent).getWeights(w).getHeight(); j++)  //goes through rows of each matrix
			    {
			        for(int i =0; i<nets.get(parent).getWeights(w).getWidth(); i++)	//goes through each col of each matrix
			        {
			          if(Math.random() <= mutationRate) //if mutation rate is met
			          {
			        	  mutationNum++;
			        	  //System.out.print("\n" + mutationNum + ") mutation! ||| ");
			        	  if(Math.random() > 0.5) //50% add 50% subtract
			        	  {
			        		  //System.out.print("+");
			        		  if(temp.getWeights(w).get(j, i) + mutationAmnt > 1) //add
			        		  {
			        			  temp.getWeights(w).set(j, i, 1);
			        		  }
			        		  else
			        		  {
			        			  temp.getWeights(w).set(j, i, temp.getWeights(w).get(j, i) + mutationAmnt); //add
			        		  }
			        	  }
			        	  else //subtract
			        	  {
			        		  //System.out.print("-");
			        		  if(temp.getWeights(w).get(j, i) - mutationAmnt < -1) //subtract
			        		  {
			        			  temp.getWeights(w).set(j, i, -1);
			        		  }
			        		  else
			        		  {
			        			  temp.getWeights(w).set(j, i, temp.getWeights(w).get(j, i) - mutationAmnt); //subtract
			        		  }
			        	  }
			          }
			        }
			     }
			 }
			 nets.add(temp);
		 }
	 }
	 //--------------------------------------------------------------------------------------
	 //Deletes worst scores/networks
	 //--------------------------------------------------------------------------------------
     public void kill()
     {
     	for(int i = 0; i<AMNTTOKILL; i++)
     	{
     		nets.remove(0);
     	}
     }
	 //--------------------------------------------------------------------------------------
	 //Sorts scores from least to greatest
	 //--------------------------------------------------------------------------------------
	 public void sortScores()
	 {
		for (int index = 1; index < scores.size(); index++)
        {
              double key = scores.get(index).doubleValue();
              int position = index;

              // shift larger values to the right
              while (position > 0 && scores.get(position - 1).doubleValue() > key)
              {
              			  
              			  Network temp1 = nets.get(position-1);
              			  Network temp2 = nets.get(position);
              				
              			  nets.remove(position-1);
              			  nets.remove(position-1);
              			  nets.add(position -1, temp1);
              			  nets.add(position -1, temp2);
              			  
              			  double tempD1 = scores.get(position-1);
              			  double tempD2 = scores.get(position);
              			  
              			  scores.remove(position-1);
              			  scores.remove(position-1);
              			  scores.add(position -1, tempD1);
              			  scores.add(position -1, tempD2);
                          position--;
               }
        }
		for(int i = 0; i< scores.size(); i++)
		{
			averageOfGen+=scores.get(i);
		}
		averageOfGen/=scores.size();
		
		System.out.println("----------------------------------");
		/*
		for(int i = 0; i< scores.size(); i++)
		{
			System.out.println( i + ")" + scores.get(i));
		}
		*/
		writeData();
		         
	 }
	 //--------------------------------------------------------------------------------------
	 //delay between each move of the snake
	 //--------------------------------------------------------------------------------------
	 private void pauser(){
		 try {
				sleep(speed);
				if(playing || isTraining)
				{
					time+=speed;
					//score += (time/1000);
				}
		 } catch (InterruptedException e) {
				e.printStackTrace();
		 }
	 }
	 //--------------------------------------------------------------------------------------
     //Restarts game
     //--------------------------------------------------------------------------------------
	 public void restart()
	 {
		for(int r = 0; r < 21; r++)
		{
				for(int c = 1; c<21; c++)
				{
					Squares.get(r).get(c).lightMeUp(2);
				}
		}
		 sizeSnake = orgSize;
		 pos = positionDepart;
		 headSnakePos=new Tuple(positionDepart.x,positionDepart.y);
		 headPos = new Tuple(headSnakePos.getX(),headSnakePos.getY());
		 directionSnake = 1;
		 positions.clear();
		 
		 positions.add(headPos);
		 foodPosition= new Tuple(15,15);
		 score = 0;
		 spawnFood(foodPosition);
		 time = 0;
		 if(!isTraining)
			 playing = true;
	 }
	 //--------------------------------------------------------------------------------------
     //Checks if snake bites itself or eats or hits wall
     //--------------------------------------------------------------------------------------
	 private void checkCollision() {
		 Tuple posCritique = positions.get(positions.size()-1);
		 
		 //Checks if snake bites itself-------------------------------------------------------------
		 for(int i = 0;i<=positions.size()-2;i++){
			 boolean biteItself = posCritique.getX()==positions.get(i).getX() && posCritique.getY()==positions.get(i).getY();
			 if(biteItself){
				stopTheGame();
			 }
		 }
		 //Check if snake hits wall-----------------------------------------------------------------
		 boolean hitWall = posCritique.getX()==0 || posCritique.getX()==21 || posCritique.getY()==0 || posCritique.getY()==21;
		 if(hitWall)
		 {
			 stopTheGame();
		 }
		 //Checks if snake eats food----------------------------------------------------------------
		 boolean eatingFood = posCritique.getX()==foodPosition.y && posCritique.getY()==foodPosition.x;
		 if(eatingFood)
		 {
			 sizeSnake=sizeSnake+1;
			 foodPosition = getValAleaNotInSnake();
			 score+=1000;
			 spawnFood(foodPosition);	
		 }
	 }
	 //--------------------------------------------------------------------------------------
     //Stops the game- Player lost
     //--------------------------------------------------------------------------------------
	 private void stopTheGame()
	 {
		 playing = false;
		 hasDied = true;
	 }
	 //--------------------------------------------------------------------------------------
	 //Put food in a position and displays it
	 //--------------------------------------------------------------------------------------
	 private void spawnFood(Tuple foodPositionIn)
	 {
		 	Squares.get(foodPositionIn.x).get(foodPositionIn.y).lightMeUp(1);
	 }
	 //--------------------------------------------------------------------------------------
	 //return a position not occupied by the snake
	 //--------------------------------------------------------------------------------------
	 private Tuple getValAleaNotInSnake()
	 {
		 Tuple p ;
		 int ranX= 0 + (int)(Math.random()*19)+1; 
		 int ranY= 0 + (int)(Math.random()*19)+1; 
		 p=new Tuple(ranX,ranY);
		 for(int i = 0;i<=positions.size()-1;i++){
			 if(p.getY()==positions.get(i).getX() && p.getX()==positions.get(i).getY()){
				 ranX= 0 + (int)(Math.random()*19)+1; 
				 ranY= 0 + (int)(Math.random()*19)+1; 
				 p=new Tuple(ranX,ranY);
				 i=0;
			 }
		 }
		 return p;
	 }
	 //--------------------------------------------------------------------------------------
	 //Moves the head of the snake and refreshes the positions in the arraylist
	 //1:right 2:left 3:top 4:bottom 0:nothing
	 //--------------------------------------------------------------------------------------
	 private void moveInterne(int dir){
		 switch(dir){
		 	case 4:
				 headSnakePos.ChangeData(headSnakePos.x,(headSnakePos.y+1)%22);
				 positions.add(new Tuple(headSnakePos.x,headSnakePos.y));
		 		break;
		 	case 3:
		 		if(headSnakePos.y-1<0){
		 			 headSnakePos.ChangeData(headSnakePos.x,19);
		 		 }
		 		else{
				 headSnakePos.ChangeData(headSnakePos.x,Math.abs(headSnakePos.y-1)%22);
		 		}
				 positions.add(new Tuple(headSnakePos.x,headSnakePos.y));
		 		break;
		 	case 2:
		 		 if(headSnakePos.x-1<0){
		 			 headSnakePos.ChangeData(19,headSnakePos.y);
		 		 }
		 		 else{
		 			 headSnakePos.ChangeData(Math.abs(headSnakePos.x-1)%22,headSnakePos.y);
		 		 } 
		 		positions.add(new Tuple(headSnakePos.x,headSnakePos.y));

		 		break;
		 	case 1:
				 headSnakePos.ChangeData(Math.abs(headSnakePos.x+1)%22,headSnakePos.y);
				 positions.add(new Tuple(headSnakePos.x,headSnakePos.y));
		 		 break;
		 }
	 }
	 //--------------------------------------------------------------------------------------
	 //Refresh the squares that needs to be 
	 //--------------------------------------------------------------------------------------
	 private void moveExterne(){
		 for(Tuple t : positions){
			 int y = t.getX();
			 int x = t.getY();
			 Squares.get(x).get(y).lightMeUp(0);
			 
		 }
	 }
	 //--------------------------------------------------------------------------------------
	 //Refreshes the tail of the snake, by removing the superfluous data in positions arraylist
	 //and refreshing the display of the things that is removed
	 //--------------------------------------------------------------------------------------
	 private void deleteTail(){
		 int cmpt = sizeSnake;
		 for(int i = positions.size()-1;i>=0;i--){
			 if(cmpt==0){
				 Tuple t = positions.get(i);
				 Squares.get(t.y).get(t.x).lightMeUp(2);
			 }
			 else{
				 cmpt--;
			 }
		 }
		 cmpt = sizeSnake;
		 for(int i = positions.size()-1;i>=0;i--){
			 if(cmpt==0){
				 positions.remove(i);
			 }
			 else{
				 cmpt--;
			 }
		 }
	 }
}
