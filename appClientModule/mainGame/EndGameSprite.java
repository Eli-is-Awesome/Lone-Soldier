package mainGame;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Ellipse2D;
import java.io.IOException;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import libs.AudioSample;
import libs.GameDisplay;
import libs.GameEngine;
import libs.Sprite;
import libs.TileSheet;

/**
 * This sprite shows when the player dies.  It 
 * displays that you are dead and gives instructions for restarting or quitting
 * 
 * @author Eli Powell
 * 
 */
public class EndGameSprite implements Sprite
{

	private int xPos;
	private int yPos;
	private long nextTime;
	private AudioSample laugh = null;
	private int bgColumnImage;
	private int bgRowImage;
	private Rectangle tileShape;
	private static TileSheet youDied = null;

	/**
	 * Constructor
	 * 
	 */
	public EndGameSprite()
	{
		xPos = 0;
		yPos = 0;

		if( youDied == null )
		{
			/**
			 * I created this from random old artwork I had
			 */
			try
			{
				youDied = new TileSheet( this, "images/youDiedTileSheet.png",
						875, 640 );
			}
			catch( IOException e )
			{
				e.printStackTrace();
			}
		}

		/**
		 * Create the laugh clip
		 */
		if( laugh == null )
		{
			try
			{
				laugh = new AudioSample( this, "audio/laugh.wav" );
				laugh.setVolume( 4 );
				laugh.stop();
			}
			catch( IOException e )
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			catch( UnsupportedAudioFileException e )
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			catch( LineUnavailableException e )
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		/**
		 * Setup some variables for the animate
		 */
		nextTime = System.currentTimeMillis();
		bgColumnImage = 0;
		bgRowImage = 0;

		tileShape = new Rectangle( xPos, yPos, youDied.getTileWidth(),
				youDied.getTileHeight() );
	}

	@Override
	public void checkCollision( Sprite obj )
	{

	}

	@Override
	public void draw( Graphics2D g )
	{
		g.drawImage( youDied.getTile( bgRowImage, bgColumnImage ), 0, 0, null );

		g.setColor( Color.WHITE );
		g.setFont( new Font( Font.DIALOG, Font.BOLD, 24 ) );
		g.drawString( "Press R to restart", xPos + 80, ( int ) GameDisplay
				.getBounds().getHeight() - 70 );
		g.drawString( "Press ESC to quit", xPos + 605, ( int ) GameDisplay
				.getBounds().getHeight() - 70 );
	}

	@Override
	public void update()
	{
		/**
		 * Move through the background tilesheet
		 */
		if( nextTime <= System.currentTimeMillis() )
		{
			nextTime = System.currentTimeMillis() + 500;
			bgColumnImage++;
			bgColumnImage %= youDied.getNumberColumns();
		}

		laugh.start();
		if( !GameEngine.isRunning() )
		{
			laugh.close();
		}
	}

	@Override
	public Rectangle getBounds()
	{
		return tileShape.getBounds();
	}

	@Override
	public Ellipse2D getEllipseBounds()
	{
		return null;
	}
}
