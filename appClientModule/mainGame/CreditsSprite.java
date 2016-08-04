package mainGame;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import libs.AudioSample;
import libs.GameEngine;
import libs.ImageUtil;
import libs.Sprite;

/**
 * Credits sprite.  This sprite shows the contributers to getting the game completed
 * and gives credit to them for their hard work
 * 
 * @author Eli Powell
 * 
 */
public class CreditsSprite implements Sprite
{
	private int xPos;
	private int yPos;
	private Rectangle startShape;
	private Rectangle storyTrimShape;
	private long nextTime;
	private AudioSample bgMusic;
	private static BufferedImage start = null;
	private static BufferedImage credits = null;

	/**
	 * Constructor
	 * 
	 */
	public CreditsSprite()
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
		if( credits == null )
		{
			try
			{
				credits = ImageUtil.loadBufferedImage( this,
						"images/credits.png" );
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
				bgMusic = new AudioSample( this, "audio/End.wav" );
				bgMusic.setVolume( 4 );
				bgMusic.loop( AudioSample.LOOP_CONTINUOUSLY );
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

		startShape = new Rectangle( 0, 0, 875, 640 );
		storyTrimShape = new Rectangle( 64, 354, 748, 244 );
	}

	@Override
	public void checkCollision( Sprite obj )
	{

	}

	@Override
	public void draw( Graphics2D g )
	{
		/**
		 * Draw the background image
		 */
		g.fill( startShape );
		g.drawImage( start, null, 0, 0 );

		/**
		 * clip and draw the credit image
		 */
		g.clip( storyTrimShape );
		g.drawImage( credits, null, xPos + 10, yPos + 250 );
	}

	@Override
	public void update()
	{
		/**
		 * Scroll the credits nice and slow for reading pleasure
		 */
		if( yPos + credits.getHeight() >= 180 )
		{
			if( nextTime <= System.currentTimeMillis() )
			{
				yPos -= 3;
				nextTime = System.currentTimeMillis() + 50;
			}
		}
		/**
		 * if the credits are finished, stop the game and close the music
		 */
		else
		{
			bgMusic.close();
			GameEngine.stop();
			System.exit( 0 );
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
}
