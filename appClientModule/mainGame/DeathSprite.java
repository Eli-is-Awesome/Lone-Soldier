package mainGame;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.io.IOException;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import libs.GameEvent;
import libs.GameEvent.GameState;
import libs.AudioSample;
import libs.GameEventDispatcher;
import libs.PlayingField;
import libs.Sprite;
import libs.TileSheet;
import libs.GameEvent.GameEventType;

/**
 * this is the death sprite.  this fires off and animates when the player gets killed
 * 
 * @author Eli Powell
 * 
 */

public class DeathSprite implements Sprite
{
	private static TileSheet player = null;
	private static Rectangle playerShape;
	private int rowImage;
	private int columnImage;
	private static boolean flipLeft;
	private static double xPos;
	private static double yPos;
	private long nextTime;
	private AudioSample deathSound;

	public DeathSprite( double x, double y )
	{
		xPos = x;
		yPos = y;

		if( player == null )
		{
			/**
			 * This original image was created by Lano Lauretta specifically for
			 * Lone "Soldier"
			 */
			try
			{
				player = new TileSheet( this, "images/DeathTilesheet.png", 64,
						64 );
			}
			catch( IOException e )
			{
				e.printStackTrace();
			}
		}

		/**
		 * Create the death sound effect
		 */
		if( deathSound == null )
		{
			try
			{
				deathSound = new AudioSample( this, "audio/LSDie.wav" );
				deathSound.setVolume( 4 );
				deathSound.play();
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

		playerShape = new Rectangle( ( int ) xPos, ( int ) yPos,
				player.getTileWidth(), player.getTileHeight() );

		nextTime = System.currentTimeMillis() + 500;
		columnImage = 0;
		rowImage = 0;
		flipLeft = false;
	}

	@Override
	public void draw( Graphics2D g )
	{
		AffineTransform transform = new AffineTransform();
		transform.concatenate( PlayingField.getCurrentPlayingField().translate(
				xPos, yPos ) );
		playerShape.x = ( int ) transform.getTranslateX();
		playerShape.y = ( int ) transform.getTranslateY();
		if( MajorPaine.isFlipLeft() )
		{
			transform.translate( player.getTileWidth(), 0 );
			transform
					.concatenate( AffineTransform.getScaleInstance( -1.0, 1.0 ) );
		}
		g.drawImage( player.getTile( rowImage, columnImage ), transform, null );
	}

	@Override
	public void update()
	{
		if( columnImage < player.getNumberColumns() - 1 )
		{
			if( nextTime <= System.currentTimeMillis() )
			{
				nextTime = System.currentTimeMillis() + 100;
				columnImage++;
			}
		}
		else
		{
			if( LoneSoldier.getGameState() != GameState.Stopped )
			{
				GameEventDispatcher.dispatchEvent( new GameEvent( this,
						GameEventType.End, this ) );
				deathSound.close();
			}
			columnImage = 5;
		}
	}

	static boolean isFlipLeft()
	{
		return flipLeft;
	}

	@Override
	public void checkCollision( Sprite obj )
	{

	}

	@Override
	public Rectangle getBounds()
	{
		return null;
	}

	@Override
	public Ellipse2D getEllipseBounds()
	{
		return null;
	}

	static double getxPos()
	{
		return xPos;
	}

	static void setxPos( double xPos )
	{
		DeathSprite.xPos = xPos;
	}

	static double getyPos()
	{
		return yPos;
	}

	static void setyPos( double yPos )
	{
		DeathSprite.yPos = yPos;
	}

}
