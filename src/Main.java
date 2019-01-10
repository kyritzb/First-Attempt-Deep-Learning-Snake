import java.awt.List;

import javax.swing.JFrame;

public class Main {

	public static Window game;

	
	public static void main(String[] args) {

		//Creating the window with all its awesome snaky features
		game= new Window();
		
		//Setting up the window settings
		game.setTitle("Snake");
		game.setSize(1000,1000);
		game.setAlwaysOnTop(true);
		game.setVisible(true);
		game.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);   
	    

		
	}
}
