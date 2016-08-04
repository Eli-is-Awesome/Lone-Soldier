package mainGame;

import java.awt.Color;
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
import libs.GameEvent.GameEventType;
import libs.SpritePoolObject;

/**
 * Enemy laser sprite. This sprite is created by the lil flyer sprite
 * 
 * @author Eli Powell
 * 
 */
public class Laser extends SpritePoolObject implements Sprite
{

	private double xPos;
	private double yPos;
	private double xVel;
	private Ellipse2D bulletShape;
	private AudioSample laser;

	/**
	 * Constructor
	 * 
	 */
	public Laser()
	{
		xVel = 6;

		/**
		 * Create the laser sound
		 */
		if( laser == null )
		{
			try
			{
				laser = new AudioSample( this, "audio/Laser.wav" );
				laser.setVolume( 4 );
				laser.play();
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

		bulletShape = new Ellipse2D.Double( ( int ) xPos, ( int ) yPos, 20, 5 );
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
		bulletShape.setFrame( ( int ) transform.getTranslateX(),
				( int ) transform.getTranslateY(), bulletShape.getWidth(),
				bulletShape.getHeight() );
		g.setColor( Color.GREEN );
		g.fill( bulletShape );
	}

	@Override
	public void update()
	{
		/**
		 * If the laser goes off screen, remove it
		 */
		if( xPos <= 0 )
		{
			laser.close();
			GameEventDispatcher.dispatchEvent( new GameEvent( this,
					GameEventType.Remove, this ) );
		}
		xPos -= xVel;
	}

	@Override
	public Rectangle getBounds()
	{
		return bulletShape.getBounds();
	}

	@Override
	public Ellipse2D getEllipseBounds()
	{
		return bulletShape;
	}

	public void setxPos( double xPos1 )
	{
		xPos = xPos1;
	}

	public void setyPos( double yPos1 )
	{
		yPos = yPos1;
	}

}
