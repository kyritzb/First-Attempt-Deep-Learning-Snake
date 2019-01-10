	import java.util.ArrayList;
	import java.awt.*; 
	import java.awt.event.KeyEvent; 
	import java.awt.image.BufferedImage;

import javax.swing.JButton;
import javax.swing.JFrame; 
	import java.util.Random;

	import java.awt.event.MouseEvent;
	import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

	public class Data extends JFrame implements MouseListener , MouseMotionListener
	{      	
	    	public static int clickX; //mouse x
	    	public static int clickY; //mouse y
	    	public static int hoverX;
	    	public static int hoverY;
	    	private final int fontSize = 30;
	        private boolean isRunning = true; 
	        private final int fps = 30;
	        public static int windowWidth = 1800; 
			public static int windowHeight = 1000; 
	        public BufferedImage backBuffer; 
	        public Insets insets;
			private Random rand = new Random();
			
			public static Button restart = new Button(1587,790,80,190,"Restart", Color.cyan);
			public static Button start = new Button(1587,900,80,190,"Start", Color.GREEN);
			public static Button train = new Button(1387,790,80,190,"Train", Color.YELLOW);
			public static Button best = new Button(1387,900,80,190,"Best", Color.RED);
		
			
			//-------------------------------------------------------------------------------------
			//Data variables
			//--------------------------------------------------17-----------------------------------
			private int snakeSize;
			private Tuple food;
			private Tuple snakeHead;
			private int direction;	        	//Direction codes | 1:right 2:left 3:top 4:bottom 0:nothing
			private long time;
			public static double score;
			private ArrayList<Tuple> positions; 
			//distance from walls
			private int distanceFromWallLeft;
			private int distanceFromWallRight;
			private int distanceFromWallUp;
			private int distanceFromWallDown;
			private int distanceFromWallDownLeft;
			private int distanceFromWallDownRight;
			private int distanceFromWallUpLeft;
			private int distanceFromWallUpRight;
			//distance from food
			private int distanceFromFoodLeft;
			private int distanceFromFoodRight;
			private int distanceFromFoodUp;
			private int distanceFromFoodDown;
			private int distanceFromFoodUpLeft;
			private int distanceFromFoodUpRight;
			private int distanceFromFoodDownLeft;
			private int distanceFromFoodDownRight;
			//distance from tails
			private int distanceFromTailLeft;
			private int distanceFromTailRight;
			private int distanceFromTailUp;
			private int distanceFromTailDown;
			private int distanceFromTailDownLeft;
			private int distanceFromTailDownRight;
			private int distanceFromTailUpLeft;
			private int distanceFromTailUpRight;
			
			//-------------------------------------------------------------------------------------
			
			public boolean showSight = true;
			
			public void updateSnakeSize(int newSize)
			{
				snakeSize = newSize;
			}
			public void data()
			{
				
			}
	        //--------------------------------------------------------------------------------------
	        //Sets up Game state
	        //--------------------------------------------------------------------------------------
	        void initialize() 
	        { 
					setTitle("Data"); 
	                setSize(windowWidth, windowHeight); 
	                setResizable(false); 
	                setDefaultCloseOperation(EXIT_ON_CLOSE); 
	                setLocationRelativeTo(null);
	                addMouseListener(this);
	                setVisible(true); 
	                setLocation(1200, 400); 
	                insets = getInsets(); 
	        		addMouseListener(this);
	        		addMouseMotionListener(this);
	                setSize(insets.left + windowWidth + insets.right, 
	                                insets.top + windowHeight + insets.bottom);
	                
	   
	                backBuffer = new BufferedImage(windowWidth, windowHeight, BufferedImage.TYPE_INT_RGB); 
	        } 
	        
	        //--------------------------------------------------------------------------------------
	        //Updates game state
	        //--------------------------------------------------------------------------------------
	        void update(Tuple headSnakePos, Tuple foodPos, int directionSnake, int newSize, long time, ArrayList<Tuple> positions) 
	        { 	    
	        	snakeSize = newSize;
	        	food = foodPos;
	        	direction = directionSnake;
	        	this.positions = positions;
	        	snakeHead = headSnakePos; 
	        	int widthGame = 22; // with 1 wall on both sides making the moveable area 20x20
	        	int heightGame = 22;
	        	//------------------------------------------------------------------------------
	    		distanceFromWallLeft = headSnakePos.x;
	    		distanceFromWallRight = widthGame - headSnakePos.x  ;
	    		distanceFromWallUp = headSnakePos.y;
	    		distanceFromWallDown = heightGame - headSnakePos.y;
	    		
	    		distanceFromWallUpLeft = (int) Math.pow(Math.pow(headSnakePos.getX() - 0, 2) + Math.pow(headSnakePos.getY()-0, 2), 0.5);
	    		distanceFromWallUpRight = (int) Math.pow(Math.pow(headSnakePos.getX() - widthGame, 2) + Math.pow(headSnakePos.getY()-0, 2), 0.5);
	    		distanceFromWallDownLeft = (int) Math.pow(Math.pow(headSnakePos.getX() - 0, 2) + Math.pow(headSnakePos.getY()-heightGame, 2), 0.5);
	    		distanceFromWallDownRight = (int) Math.pow(Math.pow(headSnakePos.getX() - widthGame, 2) + Math.pow(headSnakePos.getY()-heightGame, 2), 0.5);
	    		//------------------------------------------------------------------------------------------------------
	    		//Updates distances from food
	    		//food x and y is flipped compared to snake...
	    		//        |same x|                is infront of snake
	    		
	    		//left
	    		if(foodPos.getX() == headSnakePos.getY() && foodPos.getY() < headSnakePos.getX()){
	    			
	    			distanceFromFoodLeft =  Math.abs(headSnakePos.getX() - foodPos.getY());
	    		}
	    		else{
	    			distanceFromFoodLeft = 0; //food isnt left
	    		}
	    		//right
	    		if(foodPos.getX() == headSnakePos.getY() && foodPos.getY() > headSnakePos.getX() ){
	    			
	    			distanceFromFoodRight = Math.abs(headSnakePos.getX() - foodPos.getY());
	    		}
	    		else{
	    			distanceFromFoodRight = 0; //food isnt infront
	    		}
	    		//top
	    		if(foodPos.getY() == headSnakePos.getX() && foodPos.getX() < headSnakePos.getY()){
	    			
	    			distanceFromFoodUp =  Math.abs(headSnakePos.getY() - foodPos.getX());
	    		}
	    		else{
	    			distanceFromFoodUp = 0; //food isnt left
	    		}
	    		//down
	    		if(foodPos.getY() == headSnakePos.getX() && foodPos.getX() > headSnakePos.getY()){
	    			
	    			distanceFromFoodDown =  Math.abs(headSnakePos.getY() - foodPos.getX());
	    		}
	    		else{
	    			distanceFromFoodDown = 0; //food isnt left
	    		}
	    		//--------------------------------------------------------------------------------------
	    		distanceFromFoodUpLeft = 0;
	    		distanceFromFoodUpRight = 0;
	    		distanceFromFoodDownLeft = 0;
	    		distanceFromFoodDownRight = 0;
	    		
	    		//redraw white background and walls
	    		for(int i=0;i<22;i++){
	    			for(int j=0;j<22;j++){
	    				if(j == 0 || j == 22-1 || i == 0 || i == 22 -1) //makes wall
	    				{
	    					ThreadsController.Squares.get(i).get(j).lightMeUp(3);
	    				}
	    				else //makes white space
	    				{
	    					if(ThreadsController.Squares.get(i).get(j).getColNum() != 2	)
	    					{
	    						ThreadsController.Squares.get(i).get(j).lightMeUp(0);	
	    					}
	    				}
	    			}
	    		}
	    		//-------------------------------------------------------------------------------
	    		//Up left
	    		int counter = 0;
	    		for(int i = headSnakePos.getX(); i>0; i--)
	    		{
	    			if(headSnakePos.getX() - counter== foodPos.getY() && headSnakePos.getY() - counter == foodPos.getX())
	    			{
	    				distanceFromFoodUpLeft = (int) Math.pow(Math.pow(headSnakePos.getX() - foodPos.getY(), 2) + Math.pow(headSnakePos.getY() - foodPos.getX(), 2), 0.5);
	    			}
	    			counter++;
	    		}
	    		//Up Right
	    		counter = 0;
	    		for(int i = headSnakePos.getX(); i<22; i++)
	    		{
	    			if(headSnakePos.getX() +counter== foodPos.getY() && headSnakePos.getY() - counter == foodPos.getX())
	    			{
	    				distanceFromFoodUpRight = (int) Math.pow(Math.pow(headSnakePos.getX() - foodPos.getY(), 2) + Math.pow(headSnakePos.getY() - foodPos.getX(), 2), 0.5);
	    			}
	    			counter++;
	    		}
	    		//Down left
	    		counter = 0;
	    		for(int i = headSnakePos.getX(); i>0; i--)
	    		{
	    			if(headSnakePos.getX() - counter== foodPos.getY() && headSnakePos.getY() + counter == foodPos.getX())
	    			{
	    				distanceFromFoodDownLeft = (int) Math.pow(Math.pow(headSnakePos.getX() - foodPos.getY(), 2) + Math.pow(headSnakePos.getY() - foodPos.getX(), 2), 0.5);
	    			}
	    			counter++;
	    		}
	    		//Down Right
	    		counter = 0;
	    		for(int i = headSnakePos.getX(); i<22; i++)
	    		{
	    			if(headSnakePos.getX() + counter== foodPos.getY() && headSnakePos.getY() + counter == foodPos.getX())
	    			{
	    				distanceFromFoodDownRight = (int) Math.pow(Math.pow(headSnakePos.getX() - foodPos.getY(), 2) + Math.pow(headSnakePos.getY() - foodPos.getX(), 2), 0.5);
	    			}
	    			counter++;
	    		}
	    		
	    		ThreadsController.Squares.get(foodPos.getX()).get(foodPos.getY()).lightMeUp(1);

	    		//----------------------------------------------------------------------------------------
	    		//checks distances from tails
	    		//left
	    		distanceFromTailLeft = 0;
	    		distanceFromTailRight = 0;
	    		distanceFromTailUp = 0;
	    		distanceFromTailDown = 0;
	    		distanceFromTailUpLeft = 0;
	    		distanceFromTailUpRight = 0;
	    		distanceFromTailDownLeft = 0;
	    		distanceFromTailDownRight = 0;
	    		for(int i = positions.size()-1; i> 0; i--) // last one is tail
	    		{
	    			//if tail is on the same height(y axis) and left of the snakeHead
	    			if(positions.get(i).getY() == headSnakePos.getY() && positions.get(i).getX() < headSnakePos.getX())
	    			{
	    				distanceFromTailLeft = Math.abs(headSnakePos.getX() - positions.get(i).getX());
	    				break;
	    			}
	    		}
	    		//Right
	    		for(int i = positions.size()-1; i> 0; i--) // last one is tail
	    		{
	    			//if tail is on the same height(y axis) and left of the snakeHead
	    			if(positions.get(i).getY() == headSnakePos.getY() && positions.get(i).getX() > headSnakePos.getX())
	    			{
	    				distanceFromTailRight = Math.abs(headSnakePos.getX() - positions.get(i).getX());
	    				break;
	    			}
	    		}
	    		//Up
	    		for(int i = positions.size()-1; i> 0; i--) // last one is tail
	    		{
	    			//if tail is on the same width(X axis) and left of the snakeHead
	    			if(positions.get(i).getX() == headSnakePos.getX() && positions.get(i).getY() < headSnakePos.getY())
	    			{
	    				distanceFromTailUp = Math.abs(headSnakePos.getY() - positions.get(i).getY());
	    				break;
	    			}
	    		}
	    		//down
	    		for(int i = positions.size()-1; i> 0; i--) // last one is tail
	    		{
	    			//if tail is on the same width(X axis) and left of the snakeHead
	    			if(positions.get(i).getX() == headSnakePos.getX() && positions.get(i).getY() > headSnakePos.getY())
	    			{
	    				distanceFromTailDown = Math.abs(headSnakePos.getY() - positions.get(i).getY());
	    				break;
	    			}
	    		}
	    		//top left
	    		for(int i = positions.size()-1; i> 0; i--) // last one is tail
	    		{
	    			
	    			//if tail is top left
	    			if(positions.get(i).getX() < headSnakePos.getX() && positions.get(i).getY() < headSnakePos.getY())
	    			{
	    				distanceFromTailUpLeft = Math.abs(headSnakePos.getY() / positions.get(i).getX());
	    				break;
	    			}
	    		}
	    		//top right
	    		for(int i = positions.size()-1; i> 0; i--) // last one is tail
	    		{
	    			
	    			//if tail is top left
	    			if(positions.get(i).getX() > headSnakePos.getX() && positions.get(i).getY() < headSnakePos.getY())
	    			{
	    				distanceFromTailUpRight = Math.abs(headSnakePos.getY() / positions.get(i).getX());
	    				break;
	    			}
	    		}
	    		//bottom left
	    		for(int i = positions.size()-1; i> 0; i--) // last one is tail
	    		{
	    			
	    			//if tail is top left
	    			if(positions.get(i).getX() < headSnakePos.getX() && positions.get(i).getY() > headSnakePos.getY())
	    			{
	    				distanceFromTailDownLeft = Math.abs(headSnakePos.getY() / positions.get(i).getX());
	    				break;
	    			}
	    		}
	    		//bottom right
	    		for(int i = positions.size()-1; i> 0; i--) // last one is tail
	    		{
	    			
	    			//if tail is top left
	    			if(positions.get(i).getX() > headSnakePos.getX() && positions.get(i).getY() > headSnakePos.getY())
	    			{
	    				distanceFromTailDownRight = Math.abs(headSnakePos.getY() / positions.get(i).getX());
	    				break;
	    			}
	    		}
	    		
	    		
	        } 
	        //--------------------------------------------------------------------------------------
	        //Draws Thickine
	        //--------------------------------------------------------------------------------------
	        void drawThickLine(Graphics g, int x1, int y1, int x2, int y2, int thickness) 
	        {
	        		  int dX = x2 - x1;
	        		  int dY = y2 - y1;
	        		  // line length
	        		  double lineLength = Math.sqrt(dX * dX + dY * dY);
	        		  double scale = (double)(thickness) / (2 * lineLength);

	        		  // The x,y increments from an endpoint needed to create a rectangle...
	        		  double ddx = -scale * (double)dY;
	        		  double ddy = scale * (double)dX;
	        		  ddx += (ddx > 0) ? 0.5 : -0.5;
	        		  ddy += (ddy > 0) ? 0.5 : -0.5;
	        		  int dx = (int)ddx;
	        		  int dy = (int)ddy;

	        		  // Now we can compute the corner points...
	        		  int xPoints[] = new int[4];
	        		  int yPoints[] = new int[4];

	        		  xPoints[0] = x1 + dx; yPoints[0] = y1 + dy;
	        		  xPoints[1] = x1 - dx; yPoints[1] = y1 - dy;
	        		  xPoints[2] = x2 - dx; yPoints[2] = y2 - dy;
	        		  xPoints[3] = x2 + dx; yPoints[3] = y2 + dy;

	        		  g.fillPolygon(xPoints, yPoints, 4);
	        }
	        //--------------------------------------------------------------------------------------
	        //Draws game state
	        //--------------------------------------------------------------------------------------
	        void draw() 
	        {
	        		Graphics g = getGraphics(); 
	        		Graphics bbg = backBuffer.getGraphics(); 
	        		bbg.setColor(Color.white);
	        		bbg.fillRect(0, 0,  windowWidth,  windowHeight);
	        		//data text----------------------------------------------
	                Font f = new Font("Futura", Font.BOLD, fontSize);
	                bbg.setFont(f);
	        		bbg.setColor(Color.BLACK);
	        		bbg.drawRect(35, 10, 1300, 100);
	        		bbg.drawString("Size:"+snakeSize, 50, 75);
	        		bbg.drawString("Food Pos: ("+ food.getY()+  "," + food.getX() + ")", 160, 75);
	        		bbg.drawString("Head Pos: ("+ snakeHead.getX()+  "," + snakeHead.getY() + ")", 425, 75);
	        		bbg.drawString("Direction: " + direction, 700, 75);
	        		bbg.drawString("Time: " + (ThreadsController.time /100), 875, 75);
	        		bbg.drawString("Score: " +  ThreadsController.score, 1050, 75);
	        		//------------------------------------WALL
	                f = new Font("Futura", Font.BOLD, fontSize);
	                bbg.setFont(f);
	        		bbg.setColor(Color.BLACK);
	        		bbg.drawRect(35, 120, 400, 250);
	        		bbg.drawString("Distance from Walls", 50, 150);
	        		bbg.drawString("Up: " + (distanceFromWallUp - 2), 50, 200);
	        		bbg.drawString("Down: " + (distanceFromWallDown - 2), 50, 250);
	        		bbg.drawString("Left: " + (distanceFromWallLeft - 2), 50, 300);
	        		bbg.drawString("Right: " + (distanceFromWallRight - 2), 50, 350);
	        		bbg.drawString("Up Left: " + (distanceFromWallUpLeft -2), 200, 200);
	        		bbg.drawString("Up Right: " + (distanceFromWallUpRight -2), 200, 250);
	        		bbg.drawString("Down Left: " + (distanceFromWallDownLeft -2), 200, 300);
	        		bbg.drawString("Down Right: " + (distanceFromWallDownRight -2), 200, 350);
	        		//------------------------------------FOOD
	        		bbg.setColor(Color.BLACK);
	        		bbg.drawRect(35, 365, 400, 250);
	        		bbg.drawString("Distance from Food", 50, 400);
	        		bbg.drawString("Up:" + distanceFromFoodUp, 50,450);
	        		bbg.drawString("Down:" + distanceFromFoodDown , 50, 500);
	        		bbg.drawString("Left: " + distanceFromFoodLeft, 50, 550);
	        		bbg.drawString("Right:" + distanceFromFoodRight, 50, 600);
	        		bbg.drawString("Up Left:" + distanceFromFoodUpLeft, 200,450);
	        		bbg.drawString("Up Right:" + distanceFromFoodUpRight , 200, 500);
	        		bbg.drawString("Down Left: " + distanceFromFoodDownLeft, 200, 550);
	        		bbg.drawString("Down Right:" + distanceFromFoodDownRight, 200, 600);
	        		//------------------------------------TAIL
	        		bbg.setColor(Color.BLACK);
	        		bbg.drawRect(35, 615, 400, 250);
	        		bbg.drawString("Distance from Tail", 50, 650);
	        		bbg.drawString("Up:" + distanceFromTailUp, 50, 700);
	        		bbg.drawString("Down:" + distanceFromTailDown, 50, 750);
	        		bbg.drawString("Left:" + distanceFromTailLeft, 50, 800);
	        		bbg.drawString("Right:" + distanceFromTailRight, 50, 850);
	        		bbg.drawString("Up Left:" + distanceFromTailUpLeft, 200, 700);
	        		bbg.drawString("Up Right:" + distanceFromTailUpRight, 200, 750);
	        		bbg.drawString("Down Left:" + distanceFromTailDownLeft, 200, 800);
	        		bbg.drawString("Down Right:" + distanceFromTailDownRight, 200, 850);
			        //--------------------------------------------------------------------------------------
			        //Draws Neural Network
			        //--------------------------------------------------------------------------------------
	        		bbg.setColor(Color.BLACK);
	        		bbg.drawRect(490, 140, 875, 835); //bounding box
	        		//Draws input Neurons-------------------------------------------------------------------
					int inputSpace = 33;
					int inputRadius = 20;
					int inputStartY = 160;
					int numInputNeurons = 24;
					int inputInputX = 500;

					for(int i = 0; i<numInputNeurons; i++)
					{
						bbg.fillOval(inputInputX,inputStartY + (inputSpace * i), inputRadius, inputRadius);
					}
					//Draws Hidden Layer Neurons-------------------------------------------------------------
					int numHiddenLayer = 16;
					int hiddenLayerX = 950;
					int hiddenLayerY = 150;
					int hiddenSpace = 52;
					int hiddenRadius = 35;
					
					for(int i = 0; i<numHiddenLayer; i++)
					{
						bbg.fillOval(hiddenLayerX, hiddenLayerY + (hiddenSpace * i), hiddenRadius, hiddenRadius);
					}
					//Draws Output Neurons-------------------------------------------------------------------
					int numOutputLayer = 4;
					int outputLayerX = 1250;
					int outputLayerY = 275;
					int outputLayerSpace = 150;
					int outputLayerRadius = 75;
					
					/*
					if(ThreadsController.output != null)
					{
						for(int i = 0; i<4; i++)
						{
							if(ThreadsController.output.get(0, i) > 0)
							{
								bbg.setColor(new Color((int)(255*ThreadsController.output.get(0, i)),(int)(255*ThreadsController.output.get(0, i)),(int)(255*ThreadsController.output.get(0, i))));
							}
							else
							{
								bbg.setColor(Color.red);
							}
						}
					}
					*/
					for(int i = 0; i<numOutputLayer; i++)
					{
						bbg.fillOval(outputLayerX, outputLayerY + (outputLayerSpace * i), outputLayerRadius, outputLayerRadius);
					}
					//Draws Lines from Input to Hidden Neurons------------------------------------------------
					/*
					for(int inputNeuron = 0; inputNeuron< numInputNeurons; inputNeuron++)
					{
						for(int line = 0; line < numHiddenLayer; line++)
						{
							drawThickLine(g,inputInputX + (inputRadius/2),inputStartY + 50 + (inputSpace * inputNeuron), hiddenLayerX + (hiddenRadius / 2), hiddenLayerY + (hiddenSpace * line + hiddenSpace) , 3);
						}
					}
					*/
					//Draws Lines from Hidden Neurons to Output Neurons----------------------------------------
					/*
					for(int hiddenNeuron = 0; hiddenNeuron< numHiddenLayer; hiddenNeuron++)
					{
						for(int line = 0; line < numOutputLayer; line++)
						{
							drawThickLine(g,hiddenLayerX + (hiddenRadius/2), hiddenLayerY  +50+ (hiddenSpace * hiddenNeuron), outputLayerX + (outputLayerRadius / 2), outputLayerY + (outputLayerSpace * line + outputLayerSpace) - 60 , 3);
						}
					}
					*/
					//Draw Buttons------------------------------------------------------------------------------
					bbg.drawString("Network Data", 1500, 75);
					bbg.drawString("Generation: " + ThreadsController.generation, 1400, 150);
					bbg.drawString("Current Snake: " + ThreadsController.gameCount, 1400, 200);
					bbg.drawString("Average of Gen: " + ThreadsController.averageOfGen, 1400, 250);
					bbg.drawString("Highest: " + ThreadsController.highestScore, 1400, 300);
					for(int i = 0; i<ThreadsController.scores.size(); i++)
					{
						bbg.drawString("Scores " + ThreadsController.scores.get(i).doubleValue(), 1400, 400 + (i*30));
					}
					restart.draw(g, bbg);
					start.draw(g, bbg);
					train.draw(g, bbg);
					best.draw(g, bbg);
					
					ThreadsController.inputs.clear();
					//Wall Inputs-------------------------------------------------------------------------------
					ThreadsController.inputs.add(new Integer(distanceFromWallLeft));
					ThreadsController.inputs.add(new Integer(distanceFromWallRight));
					ThreadsController.inputs.add(new Integer(distanceFromWallUp));
					ThreadsController.inputs.add(new Integer(distanceFromWallDown));
					ThreadsController.inputs.add(new Integer(distanceFromWallUpLeft));
					ThreadsController.inputs.add(new Integer(distanceFromWallUpRight));
					ThreadsController.inputs.add(new Integer(distanceFromWallDownLeft));
					ThreadsController.inputs.add(new Integer(distanceFromWallDownRight));
		    		//Food inputs--------------------------------------------------------------------------------
					ThreadsController.inputs.add(new Integer(distanceFromFoodLeft));
					ThreadsController.inputs.add(new Integer(distanceFromFoodRight));
					ThreadsController.inputs.add(new Integer(distanceFromFoodUp));
					ThreadsController.inputs.add(new Integer(distanceFromFoodDown));
					ThreadsController.inputs.add(new Integer(distanceFromFoodUpLeft));
					ThreadsController.inputs.add(new Integer(distanceFromFoodUpRight));
					ThreadsController.inputs.add(new Integer(distanceFromFoodDownLeft));
					ThreadsController.inputs.add(new Integer(distanceFromFoodDownRight));
		    		//Tail Inputs--------------------------------------------------------------------------------
					ThreadsController.inputs.add(new Integer(distanceFromTailLeft));
					ThreadsController.inputs.add(new Integer(distanceFromTailRight));
					ThreadsController.inputs.add(new Integer(distanceFromTailUp));
					ThreadsController.inputs.add(new Integer(distanceFromTailDown));
					ThreadsController.inputs.add(new Integer(distanceFromTailUpLeft));
					ThreadsController.inputs.add(new Integer(distanceFromTailUpRight));
					ThreadsController.inputs.add(new Integer(distanceFromTailDownLeft));
					ThreadsController.inputs.add(new Integer(distanceFromTailDownRight));
		    		
		    		
					g.drawImage(backBuffer, insets.left, insets.top, this); 
	        }
	        //--------------------------------------------------------------------------------------
	        //Gets Mouse Data
	        //--------------------------------------------------------------------------------------
		    public void mouseClicked(MouseEvent e) {
		    }
		    public void mouseEntered(MouseEvent e) {
		    }
		    public void mouseExited(MouseEvent e) {
		    }
		    public void mousePressed(MouseEvent e) {
		    }
		    public void mouseReleased(MouseEvent e) {
		        clickX = e.getX();
		        clickY = e.getY();
		    }
			public void mouseDragged(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			public void mouseMoved(MouseEvent e) {
			     hoverX= e.getX();
			     hoverY = e.getY();
				
			}

	        
	} 

