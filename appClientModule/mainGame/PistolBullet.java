package mainGame;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.io.IOException;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import libs.AudioSample;
import libs.GameDisplay;
import libs.GameEvent;
import libs.GameEventDispatcher;
import libs.PlayingField;
import libs.Sprite;
import libs.GameEvent.GameEventType;
import libs.SpritePoolObject;

/**
 * Player bullet sprite. This sprite is created by the player sprite
 * 
 * @author Eli Powell
 * 
 */
public class PistolBullet extends SpritePoolObject implements Sprite
{

	private double xPos;
	private double yPos;
	private double xVel;
	private Ellipse2D bulletShape;
	private int yVel;
	private AudioSample fire;

	/**
	 * Constructor
	 * 
	 */
	public PistolBullet()
	{
		xVel = 10;
		yVel = 10;

		/**
		 * Create the firing sound
		 */
		if( fire == null )
		{
			try
			{
				fire = new AudioSample( this, "audio/pistol.wav" );
				fire.setVolume( 2 );
				fire.play();
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

		bulletShape = new Ellipse2D.Double( ( int ) xPos, ( int ) yPos, 5, 5 );
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
		g.setColor( Color.RED );
		g.setStroke( new BasicStroke( 2.5f ) );
		g.setColor( Color.BLACK );
		g.draw( bulletShape );
	}

	@Override
	public void update()
	{
		if( xPos + bulletShape.getWidth() >= MajorPaine.getxPos() + 600
				|| yPos >= GameDisplay.getBounds().getHeight() )
		{
			/**
			 * The bullet went off the screen so remove it
			 */
			GameEventDispatcher.dispatchEvent( new GameEvent( this,
					GameEventType.Remove, this ) );
			fire.close();
		}
		if( xPos <= MajorPaine.getxPos() - 546 || yPos <= 0 )
		{
			/**
			 * The bullet went off the screen so remove it
			 */
			GameEventDispatcher.dispatchEvent( new GameEvent( this,
					GameEventType.Remove, this ) );
			fire.close();
		}

		/**
		 * If it goes left or right, set the xVel & yVel and send the bullet the
		 * correct direction
		 */
		if( xPos >= MajorPaine.getxPos() + 32 && yPos > MajorPaine.getyPos() )
		{
			xVel = 10;
			yVel = 0;
		}
		if( xPos <= MajorPaine.getxPos() + 31 && yPos > MajorPaine.getyPos() )
		{
			xVel = -10;
			yVel = 0;
		}

		/**
		 * If it goes up, set the xVel & yVel and make sure it is coming out
		 * from the side the gun is on
		 */
		if( MajorPaine.isFlipLeft() == false && yPos < MajorPaine.getyPos()
				&& xPos < MajorPaine.getxPos() * 2 + 32
				&& xPos > MajorPaine.getxPos() - 32 )
		{
			xVel = 0;
			yVel = 10;
		}
		if( MajorPaine.isFlipLeft() == true && yPos < MajorPaine.getyPos()
				&& xPos < MajorPaine.getxPos() * 2 + 32
				&& xPos > MajorPaine.getxPos() - 32 )
		{
			xVel = 0;
			yVel = 10;
		}

		/**
		 * If it goes diagonal, set the xVel & yVel and make sure it is going
		 * the right direction
		 */
		if( MajorPaine.isFlipLeft() == false && yPos < MajorPaine.getyPos() + 3
				&& xPos > MajorPaine.getxPos() + 32 )
		{
			xVel = 10;
			yVel = 10;
		}
		if( MajorPaine.isFlipLeft() == true && yPos < MajorPaine.getyPos() + 3
				&& xPos < MajorPaine.getxPos() + 6 )
		{
			xVel = -10;
			yVel = 10;
		}

		/**
		 * execute the velocity changes
		 */
		xPos += xVel;
		yPos -= yVel;
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
