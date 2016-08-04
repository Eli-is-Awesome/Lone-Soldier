package mainGame;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.io.IOException;

import libs.GameDisplay;
import libs.GameEvent;
import libs.GameEventDispatcher;
import libs.PlayingField;
import libs.Sprite;
import libs.TileSheet;
import libs.GameEvent.GameEventType;

/**
 * Big Drone Sprite.  This sprite hammers it's arms when it gets close to the player
 * No shooting attack
 * 
 * @author Eli Powell
 * 
 */
public class BigDrone implements Sprite
{
	private double xPos;
	private double yPos;
	private static TileSheet drone = null;
	private static Rectangle droneShape;
	private int rowImage;
	private int columnImage;
	private long nextTime;
	private int xVel;
	private int hits;
	private boolean flipRight;
	private Direction direction;

	public enum Direction
	{
		LEFT, RIGHT
	}

	/**
	 * Constructor
	 * 
	 */
	public BigDrone( double x, double y )
	{
		xPos = x;
		yPos = y;
		xVel = 3;
		rowImage = 0;
		columnImage = 0;
		hits = 0;
		nextTime = System.currentTimeMillis();
		direction = Direction.LEFT;

		if( drone == null )
		{
			/**
			 * Big Flying Alien that reaches out to attack
			 */
			try
			{
				drone = new TileSheet( this, "images/bigDrone.png", 60, 120 );
			}
			catch( IOException e )
			{
				e.printStackTrace();
			}
		}

		droneShape = new Rectangle( ( int ) xPos, ( int ) yPos + 2,
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
					GameEventDispatcher.dispatchEvent( new GameEvent( this,
							GameEventType.Remove, this ) );
					GameEventDispatcher.dispatchEvent( new GameEvent( this,
							GameEventType.Explode, this ) );
				}
				hits++;
			}
		}

		if( obj instanceof MajorPaine )
		{
			if( droneShape.intersects( obj.getBounds() ) )
			{
				rowImage = 2;
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

		if( flipRight )
		{
			transform.translate( drone.getTileWidth(), 0 );
			transform
					.concatenate( AffineTransform.getScaleInstance( -1.0, 1.0 ) );
		}
		g.drawImage( drone.getTile( rowImage, columnImage ), transform, null );
	}

	@Override
	public void update()
	{
		PlayingField playingField = PlayingField.getCurrentPlayingField();

		if( xPos <= 0 || yPos >= GameDisplay.getBounds().getHeight() )
		{
			GameEventDispatcher.dispatchEvent( new GameEvent( this,
					GameEventType.Remove, this ) );
		}

		if( playingField.isCollision( xPos + droneShape.width, yPos
				+ ( droneShape.height / 2 ) )
				&& direction == Direction.RIGHT )
		{
			direction = Direction.LEFT;
		}
		if( playingField.isCollision( xPos, yPos + ( droneShape.height / 2 ) )
				&& direction == Direction.LEFT )
		{
			direction = Direction.RIGHT;
		}
		if( !playingField.isCollision( xPos + droneShape.width, yPos
				+ droneShape.height ) )
		{
			yPos += 6;
		}

		/**
		 * Animate the alien
		 */
		if( nextTime <= System.currentTimeMillis() )
		{
			if( MajorPaine.getxPos() >= xPos - 256 )
			{
				rowImage = 1;
			}
			nextTime = System.currentTimeMillis() + 175;
			columnImage++;
			columnImage %= drone.getNumberColumns();
		}

		switch( direction )
		{
			case LEFT:
				xPos -= xVel;
				flipRight = false;
				break;

			case RIGHT:
				xPos += xVel;
				flipRight = true;
				break;
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
}
