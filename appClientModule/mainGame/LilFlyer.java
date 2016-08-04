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
 * Lil Flyer sprite. This sprite shoots lasers at the player
 * 
 * @author Eli Powell
 * 
 */
public class LilFlyer implements Sprite
{
	private double xPos;
	private double yPos;
	private static TileSheet flyer = null;
	private static Rectangle flyerShape;
	private int rowImage;
	private int columnImage;
	private long nextTime;
	private int xVel;
	private int hits;
	private long shootTime;
	private Laser laser;
	private LaserPool laserPool;

	/**
	 * Constructor
	 * 
	 */
	public LilFlyer( double x, double y )
	{
		xPos = x;
		yPos = y;
		xVel = 3;
		rowImage = 0;
		columnImage = 0;
		nextTime = System.currentTimeMillis();
		hits = 0;
		shootTime = System.currentTimeMillis();
		laserPool = new LaserPool();

		if( flyer == null )
		{
			/**
			 * Lil Flying Alien that shoots lazers
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

		flyerShape = new Rectangle( ( int ) xPos, ( int ) yPos + 2,
				flyer.getTileWidth(), flyer.getTileHeight() - 4 );
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
			if( flyerShape.intersects( obj.getBounds() ) )
			{
				GameEventDispatcher.dispatchEvent( new GameEvent( this,
						GameEventType.Remove, obj ) );
				GameEventDispatcher.dispatchEvent( new GameEvent( this,
						GameEventType.Score, new Integer( 50 ) ) );

				/**
				 * if the alien gets hit 3 times, he dies
				 */
				if( hits >= 2 )
				{
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
		flyerShape.x = ( int ) transform.getTranslateX();
		flyerShape.y = ( int ) transform.getTranslateY();

		g.drawImage( flyer.getTile( rowImage, columnImage ), transform, null );
	}

	@Override
	public void update()
	{
		PlayingField playingField = PlayingField.getCurrentPlayingField();

		if( yPos > MajorPaine.getyPos() )
		{
			yPos -= 8;
		}
		else if( !playingField.isCollision( xPos + ( flyerShape.width / 2 ),
				yPos + flyer.getTileHeight() )
				&& yPos <= MajorPaine.getyPos() + 1 )
		{
			yPos += 8;
		}

		if( !playingField.isCollision( xPos, yPos + ( flyerShape.height / 2 ) ) )
		{
			xPos -= xVel;
		}

		if( xPos + flyerShape.width <= GameDisplay.getBounds().getX() )
		{
			GameEventDispatcher.dispatchEvent( new GameEvent( this,
					GameEventType.Remove, this ) );
		}

		/**
		 * Fire some lasers
		 */
		if( xPos > MajorPaine.getxPos() + 64
				&& shootTime <= System.currentTimeMillis() )
		{
			laser = ( Laser ) laserPool.checkOut();
			if( nextTime <= System.currentTimeMillis() )
			{
				rowImage = 1;
				nextTime = System.currentTimeMillis() + 100;
				columnImage++;
				columnImage %= flyer.getNumberColumns() - 3;
			}
			if( laser != null )
			{
				laser.setxPos( xPos );
				laser.setyPos( yPos + flyerShape.height / 2 );

				GameEventDispatcher.dispatchEvent( new GameEvent( this,
						GameEventType.AddLast, laser ) );
			}

			shootTime = System.currentTimeMillis() + 1000;
		}
		else
		{
			rowImage = 0;
		}

		/**
		 * Animate the alien
		 */
		if( nextTime <= System.currentTimeMillis() )
		{
			nextTime = System.currentTimeMillis() + 250;
			columnImage++;
			columnImage %= flyer.getNumberColumns();
		}

	}

	@Override
	public Rectangle getBounds()
	{
		return flyerShape.getBounds();
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
