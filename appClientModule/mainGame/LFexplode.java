package mainGame;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.io.IOException;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import libs.AudioSample;
import libs.GameEvent;
import libs.GameEventDispatcher;
import libs.PlayingField;
import libs.Sprite;
import libs.TileSheet;
import libs.GameEvent.GameEventType;

/**
 * Explosion for the Lil Flyer Sprite.  this fires off when the lil flyer dies
 * 
 * @author Eli Powell
 * 
 */
public class LFexplode implements Sprite
{
	private double xPos;
	private double yPos;
	private static TileSheet flyer = null;
	private Rectangle flyerShape;
	private int rowImage;
	private int columnImage;
	private long nextTime;
	private AudioSample boom;

	/**
	 * Constructor
	 * 
	 */
	public LFexplode()
	{
		rowImage = 2;
		nextTime = System.currentTimeMillis();

		if( flyer == null )
		{
			/**
			 * Lil Flying Alien that DIES! lol
			 */
			try
			{
				flyer = new TileSheet( this, "images/lilFlyer.png", 64, 64 );
			}
			catch( IOException e )
			{
				e.printStackTrace();
			}
		}

		/**
		 * Create the alien dyeing sound
		 */
		if( boom == null )
		{
			try
			{
				boom = new AudioSample( this, "audio/AlienDie.wav" );
				boom.setVolume( 7 );
				boom.stop();
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

		columnImage = 0;

		flyerShape = new Rectangle( ( int ) xPos, ( int ) yPos,
				flyer.getTileWidth(), flyer.getTileHeight() );

	}

	@Override
	public void checkCollision( Sprite obj )
	{

	}

	@Override
	public void draw( Graphics2D g )
	{
		AffineTransform transform = new AffineTransform();
		transform.concatenate( PlayingField.getCurrentPlayingField().translate(
				xPos, yPos ) );
		flyerShape.x = ( int ) transform.getTranslateX();
		flyerShape.y = ( int ) transform.getTranslateY();
		g.drawImage( flyer.getTile( rowImage, columnImage ), transform, null );
	}

	@Override
	public void update()
	{
		/**
		 * Animate the explosion
		 */
		if( nextTime <= System.currentTimeMillis() )
		{
			nextTime = System.currentTimeMillis() + 180;
			columnImage++;

			/**
			 * If the animation is done, remove yourself
			 */
			if( columnImage >= flyer.getNumberColumns() )
			{
				boom.stop();
				GameEventDispatcher.dispatchEvent( new GameEvent( this,
						GameEventType.Remove, this ) );
			}
		}

	}

	@Override
	public Rectangle getBounds()
	{
		return flyerShape.getBounds();
	}

	public double getxPos()
	{
		return xPos;
	}

	public void setxPos( double xPos1 )
	{
		xPos = xPos1;
	}

	public double getyPos()
	{
		return yPos;
	}

	public void setyPos( double yPos1 )
	{
		yPos = yPos1;
	}

	@Override
	public Ellipse2D getEllipseBounds()
	{
		return null;
	}

	public void play()
	{
		boom.play();
	}
}
