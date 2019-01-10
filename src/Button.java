

import java.awt.image.BufferedImage; 
import javax.swing.JFrame; 
import java.awt.*; 

public class Button
{
	private final int border = 10;
	private final int highlightIntensity = 50;
	private int x;
	private int y;
	private int height;
	private int width;
	private String text;
	private Color col;
	private Color highlight;
	private boolean enabled;
	private final int fontSize = 45;
	private int LMouseX;
	private int LMouseY;
	private int blueHigh;
	private int redHigh;
	private int greenHigh;
	public Button(int nX, int nY, int nH, int nW, String nT, Color nC)
	{
		x = nX;
		y = nY;
		height = nH;
		width = nW;
		text = nT;
		col = nC;
		LMouseX = 0;
		LMouseY = 0;
		enabled = false;
		//-------------------------------------------------Gets highlight color
		if(col.getRed() + highlightIntensity > 255)
			redHigh = 255;
		else
			redHigh = col.getRed() + highlightIntensity;
		if(col.getBlue() + highlightIntensity > 255)
			blueHigh = 255;
		else
			blueHigh = col.getBlue() + highlightIntensity;
		if(col.getGreen() + highlightIntensity > 255)
			greenHigh = 255;
		else
			greenHigh = col.getGreen() + highlightIntensity;
		highlight = new Color(redHigh, greenHigh, blueHigh);
	}
	
	public void draw(Graphics g, Graphics bbg)
	{
		bbg.setColor(Color.black);
		bbg.fillRect(x-border, y-border, width + 2* border, height + 2*border);
		if(isHovering())
			bbg.setColor(highlight);
		else
			bbg.setColor(col);	
		bbg.fillRect(x,y,width,height);
		bbg.setColor(Color.black);

		bbg.drawString(text, x + width/4, y +height/2 +10);
	}
	public void enable()
	{
		enabled = true;
	}
	public void disable()
	{
		enabled = false;
	}
	public boolean isEnabled()
	{
		return enabled;
	}
	public boolean isClicked()
	{
		
		
		int mouseX = Data.clickX;
		int mouseY = Data.clickY;
		if(mouseX != LMouseX && mouseY != LMouseY)
		{
			/*
			System.out.println("MX:" + mouseX);
			System.out.println("MY:" + mouseY);
			System.out.println("LX:" + LMouseX);
			System.out.println("LY:" + LMouseY);
			*/
			LMouseX = mouseX;
			LMouseY = mouseY;
			if( (mouseX >= x && mouseX <= x+width && mouseY >= y && mouseY <= y+height))
				System.out.println("clicked");
			return (mouseX >= x && mouseX <= x+width && mouseY >= y && mouseY <= y+height);
		}	
		return false;
		
	}
	public boolean isHovering()
	{
		int mouseHoverX = Data.hoverX;
		int mouseHoverY = Data.hoverY;
		//LMouseX = mouseHoverX;
		//LMouseY = mouseHoverY;
		return (mouseHoverX >= x && mouseHoverX <= x+width && mouseHoverY >= y && mouseHoverY <= y+height);
		
	}
}