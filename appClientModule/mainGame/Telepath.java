package mainGame;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.io.IOException;

import libs.GameEvent;
import libs.GameEventDispatcher;
import libs.PlayingField;
import libs.Sprite;
import libs.TileSheet;
import libs.GameEvent.GameEventType;

/**
 * Telepath sprite. This sprite doesn't do much, 
 * but you have to kill him to progress
 * 
 * @author Eli Powell
 * 
 */
public class Telepath implements Sprite
{
	private double xPos;
	private double yPos;
	private static TileSheet drone = null;
	private static Rectangle droneShape;
	private static boolean dead;
	private int rowImage;
	private int columnImage;
	private long nextTime;
	private int hits;

	/**
	 * Constructor
	 * 
	 */
	public Telepath( double x, double y )
	{
		xPos = x;
		yPos = y;
		rowImage = 0;
		columnImage = 0;
		hits = 0;
		nextTime = System.currentTimeMillis();
		dead = false;

		if( drone == null )
		{
			try
			{
				drone = new TileSheet( this, "images/Telepath.png", 42, 64 );
			}
			catch( IOException e )
			{
				e.printStackTrace();
			}
		}

		droneShape = new Rectangle( ( int ) xPos, ( int ) yPos,
				drone.getTileWidth(), drone.getTileHeight() );
	}

	@Override
	public void checkCollision( Sprite obj )
	{
		/**
		 * Check to see how many times we have been hit and die if it reaches
		 * the limit
		 */
		if( obj instanceof PistolBullet )
		{
			if( droneShape.intersects( obj.getBounds() ) )
			{
				GameEventDispatcher.dispatchEvent( new GameEvent( this,
						GameEventType.Remove, obj ) );
				GameEventDispatcher.dispatchEvent( new GameEvent( this,
						GameEventType.Score, new Integer( 50 ) ) );

				/**
				 * if the alien gets hit 3 times, he dies
				 */
				if( hits >= 3 )
				{
					dead = true;
					GameEventDispatcher.dispatchEvent( new GameEvent( this,
							GameEventType.Remove, this ) );
					GameEventDispatcher.dispatchEvent( new GameEvent( this,
							GameEventType.Explode, this ) );
				}
				hits++;
			}
		}

	}

	@Override
	public void draw( Graphics2D g )
	{
		AffineTransform transform = new AffineTransform();
		transform.concatenate( PlayingField.getCurrentPlayingField().translate(
				xPos, yPos ) );
		droneShape.x = ( int ) transform.getTranslateX();
		droneShape.y = ( int ) transform.getTranslateY();
		g.drawImage( drone.getTile( rowImage, columnImage ), transform, null );
	}

	@Override
	public void update()
	{
		/**
		 * Animate the alien
		 */
		if( nextTime <= System.currentTimeMillis() )
		{
			nextTime = System.currentTimeMillis() + 175;
			columnImage++;
			columnImage %= drone.getNumberColumns();
		}

	}

	@Override
	public Rectangle getBounds()
	{
		return droneShape.getBounds();
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

	public static boolean isDead()
	{
		return dead;
	}
}
