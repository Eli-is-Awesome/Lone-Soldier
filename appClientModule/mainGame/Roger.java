package mainGame;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import libs.GameEvent;
import libs.GameEvent.GameEventType;
import libs.GameEventDispatcher;
import libs.ImageUtil;
import libs.PlayingField;
import libs.Sprite;

/**
 * Roger sprite. This sprite is Major Paine's son
 * he acts as a trigger to begin the next level sequence
 * 
 * @author Eli Powell
 * 
 */
public class Roger implements Sprite
{
	private double xPos;
	private double yPos;
	private static BufferedImage roger = null;
	private static Rectangle rogerShape;

	/**
	 * Constructor
	 * 
	 */
	public Roger()
	{
		xPos = 11256;
		yPos = 400;

		if( roger == null )
		{
			/**
			 * Load roger image
			 */
			try
			{
				roger = ImageUtil.loadBufferedImage( this, "images/roger.png" );
			}
			catch( IOException e )
			{
				e.printStackTrace();
			}
		}

		rogerShape = new Rectangle( ( int ) xPos, ( int ) yPos, 32, 48 );
	}

	@Override
	public void checkCollision( Sprite obj )
	{
		if( obj instanceof MajorPaine )
		{
			if( rogerShape.intersects( obj.getBounds() ) )
			{
				GameEventDispatcher.dispatchEvent( new GameEvent( this,
						GameEventType.Remove, this ) );
				GameEventDispatcher.dispatchEvent( new GameEvent( this,
						GameEventType.Remove, obj ) );
				GameEventDispatcher.dispatchEvent( new GameEvent( null,
						GameEventType.NextLevel, null ) );
			}
		}
	}

	@Override
	public void draw( Graphics2D g )
	{
		AffineTransform transform = new AffineTransform();
		transform.concatenate( PlayingField.getCurrentPlayingField().translate(
				xPos, yPos ) );
		rogerShape.x = ( int ) transform.getTranslateX();
		rogerShape.y = ( int ) transform.getTranslateY();
		g.drawImage( roger, transform, null );
	}

	@Override
	public void update()
	{

	}

	@Override
	public Rectangle getBounds()
	{
		return rogerShape.getBounds();
	}

	double getxPos()
	{
		return xPos;
	}

	void setxPos( double xPos )
	{
		this.xPos = xPos;
	}

	double getyPos()
	{
		return yPos;
	}

	void setyPos( double yPos )
	{
		this.yPos = yPos;
	}

	@Override
	public Ellipse2D getEllipseBounds()
	{
		return null;
	}
}
