package mainGame;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import libs.AudioSample;
import libs.ImageUtil;
import libs.Keyboard;
import libs.Sprite;

/**
 * Start sprite. This sprite displays the back story,
 * main game controls and player controls
 * 
 * @author Eli Powell
 * 
 */
public class StartSprite implements Sprite
{
	private int xPos;
	private int yPos;
	private Rectangle startShape;
	private Rectangle storyTrimShape;
	private long nextTime;
	private Keyboard keyboard;
	private AudioSample bgMusic;
	private static BufferedImage start = null;
	private static BufferedImage story = null;
	private static Font f;

	/**
	 * Constructor
	 * 
	 */
	public StartSprite()
	{
		xPos = 64;
		yPos = 354;

		if( start == null )
		{
			try
			{
				start = ImageUtil.loadBufferedImage( this, "images/Start.png" );
			}
			catch( IOException e )
			{
				e.printStackTrace();
			}
		}
		if( story == null )
		{
			try
			{
				story = ImageUtil.loadBufferedImage( this, "images/text.png" );
			}
			catch( IOException e )
			{
				e.printStackTrace();
			}
		}

		/**
		 * Create the background music
		 */
		if( bgMusic == null )
		{
			try
			{
				bgMusic = new AudioSample( this, "audio/IntroBeat.wav" );
				bgMusic.setVolume( ( float ) 6.5 );
				bgMusic.loop( 5 );
				bgMusic.play();
			}
			catch( IOException e )
			{
				e.printStackTrace();
			}
			catch( UnsupportedAudioFileException e )
			{
				e.printStackTrace();
			}
			catch( LineUnavailableException e )
			{
				e.printStackTrace();
			}
		}

		GraphicsEnvironment e = GraphicsEnvironment
				.getLocalGraphicsEnvironment();
		Font[] fonts = e.getAllFonts(); // Get the fonts
		for( Font font : fonts )
		{
			if( font.getFamily().equals( "Brush Script MT" )
					&& font.getName().equals( "BrushScriptMT" ) )
			{
				f = new Font( "BrushScriptMT", Font.PLAIN, 24 );
				break;
			}
			else if( font.getFontName().equals( "Rage Italic" ) )
			{
				f = new Font( "Rage Italic", Font.BOLD, 24 );
				break;
			}
			else
			{
				f = new Font( "Sans Serif", Font.BOLD, 24 );
			}
		}

		startShape = new Rectangle( 0, 0, 875, 640 );
		storyTrimShape = new Rectangle( 64, 354, 748, 244 );

		keyboard = new Keyboard();
	}

	@Override
	public void checkCollision( Sprite obj )
	{

	}

	@Override
	public void draw( Graphics2D g )
	{
		g.fill( startShape );
		g.drawImage( start, null, 0, 0 );
		g.setFont( f );
		g.setColor( Color.BLACK );
		g.drawString( "Press X to skip story...", 70, 347 );

		/**
		 * Draw the main controls for the game
		 */
		g.setFont( new Font( "Sans Serif", Font.BOLD, 14 ) );
		g.drawString( "Main controls:", 10, 70 );
		g.drawString( "Press S to start the game", 10, 100 );
		g.drawString( "Press P to pause the game", 10, 130 );
		g.drawString( "Press R to restart the game", 10, 160 );
		g.drawString( "Press ESC to quit the game", 10, 190 );
		g.drawString( "Playing Instructions:", 550, 70 );
		g.drawString( "Use the left & right arrows to move", 550, 100 );
		g.drawString( "OR the A & D keys to move", 550, 130 );
		g.drawString( "Use the up arrow or W key to jump", 550, 160 );
		g.drawString( "Use the mouse to shoot", 550, 190 );
		g.drawString( "Line the mouse up with MajorPaine", 550, 220 );
		g.drawString( "     to shoot that direction", 550, 240 );

		/**
		 * clip and draw the story image
		 */
		g.clip( storyTrimShape );
		g.drawImage( story, null, xPos + 27, yPos + 250 );
	}

	@Override
	public void update()
	{
		/**
		 * Press X to skip to the end of the story
		 */
		if( keyboard.isStateChanged() )
		{
			KeyEvent ke = keyboard.getLastKeyboardEvent();
			if( ke.getID() == KeyEvent.KEY_RELEASED )
			{
				switch( ke.getKeyCode() )
				{
					case KeyEvent.VK_X:
						yPos = -story.getHeight() + 340;
						break;
				}
			}
		}

		/**
		 * Scroll the story nice and slow for reading pleasure
		 */
		if( yPos + story.getHeight() >= 340 )
		{
			if( nextTime <= System.currentTimeMillis() )
			{
				yPos -= 2;
				nextTime = System.currentTimeMillis() + 100;
			}
		}
		else
		{
			yPos = -story.getHeight() + 340;
		}
	}

	@Override
	public Rectangle getBounds()
	{
		return startShape.getBounds();
	}

	@Override
	public Ellipse2D getEllipseBounds()
	{
		return null;
	}

	public void stopMusic()
	{
		bgMusic.close();
	}
}
