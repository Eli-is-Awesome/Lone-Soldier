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
 * Sarah sprite. This sprite is Major Paine's daughter
 * she acts as a trigger to finish the game
 * 
 * @author Eli Powell
 * 
 */
public class Sarah implements Sprite
{
	private double xPos;
	private double yPos;
	private BufferedImage sarah;
	private static Rectangle sarahShape;

	/**
	 * Constructor
	 * 
	 */
	public Sarah()
	{
		xPos = 11256;
		yPos = 387;

		if( sarah == null )
		{
			/**
			 * Load sarah image
			 */
			try
			{
				sarah = ImageUtil.loadBufferedImage( this,
						"images/jenny_happy.png" );
			}
			catch( IOException e )
			{
				e.printStackTrace();
			}
		}

		sarahShape = new Rectangle( ( int ) xPos, ( int ) yPos,
				sarah.getWidth(), sarah.getHeight() );
	}

	@Override
	public void checkCollision( Sprite obj )
	{
		if( obj instanceof MajorPaine )
		{
			if( sarahShape.intersects( obj.getBounds() ) )
			{
				GameEventDispatcher.dispatchEvent( new GameEvent( this,
						GameEventType.Remove, this ) );
				GameEventDispatcher.dispatchEvent( new GameEvent( this,
						GameEventType.Win, obj ) );
			}
		}
	}

	@Override
	public void draw( Graphics2D g )
	{
		AffineTransform transform = new AffineTransform();
		transform.concatenate( PlayingField.getCurrentPlayingField().translate(
				xPos, yPos ) );
		sarahShape.x = ( int ) transform.getTranslateX();
		sarahShape.y = ( int ) transform.getTranslateY();
		g.drawImage( sarah, transform, null );
	}

	@Override
	public void update()
	{

	}

	@Override
	public Rectangle getBounds()
	{
		return sarahShape.getBounds();
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
