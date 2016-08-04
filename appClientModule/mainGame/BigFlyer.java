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
 * Big Flyer Sprite.  This sprite will extends it's arms to attack
 * No Shooting
 * 
 * @author Eli Powell
 * 
 */
public class BigFlyer implements Sprite
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
	private Rectangle flyerArms;
	private Rectangle flyerBody;

	/**
	 * Constructor
	 * 
	 */
	public BigFlyer( double x, double y )
	{
		xPos = x;
		yPos = y;
		xVel = 2;
		rowImage = 0;
		columnImage = 0;
		hits = 0;
		nextTime = System.currentTimeMillis();

		if( flyer == null )
		{
			/**
			 * Big Flying Alien that reaches out to attack
			 */
			try
			{
				flyer = new TileSheet( this, "images/bigFlyer.png", 128, 64 );
			}
			catch( IOException e )
			{
				e.printStackTrace();
			}
		}

		flyerArms = new Rectangle( ( int ) xPos, ( int ) yPos + 2,
				flyer.getTileWidth() / 2, flyer.getTileHeight() - 4 );
		flyerBody = new Rectangle( ( int ) xPos + 64, ( int ) yPos + 2,
				flyer.getTileWidth() / 2, flyer.getTileHeight() - 4 );
		flyerShape = flyerArms.union( flyerBody );
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
			if( flyerBody.intersects( obj.getBounds() ) )
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

	}

	@Override
	public void draw( Graphics2D g )
	{
		AffineTransform transform = new AffineTransform();
		transform.concatenate( PlayingField.getCurrentPlayingField().translate(
				xPos, yPos ) );
		flyerShape.x = ( int ) transform.getTranslateX();
		flyerShape.y = ( int ) transform.getTranslateY();
		flyerBody.x = ( int ) transform.getTranslateX();
		flyerBody.y = ( int ) transform.getTranslateY();
		flyerArms.x = ( int ) transform.getTranslateX();
		flyerArms.y = ( int ) transform.getTranslateY();

		g.drawImage( flyer.getTile( rowImage, columnImage ), transform, null );
	}

	@Override
	public void update()
	{
		PlayingField playingField = PlayingField.getCurrentPlayingField();

		if( yPos > MajorPaine.getyPos() )
		{
			yPos -= 6;
		}
		else if( !playingField.isCollision( xPos + ( flyerShape.width / 2 ),
				yPos + flyer.getTileHeight() )
				&& yPos <= MajorPaine.getyPos() + 1 )
		{
			yPos += 6;
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
		 * Animate the alien
		 */
		if( nextTime <= System.currentTimeMillis() )
		{
			nextTime = System.currentTimeMillis() + 250;
			if( xPos <= MajorPaine.getxPos() + 192
					&& xPos >= MajorPaine.getxPos() - 1 )
			{
				rowImage = 1;
			}
			else
			{
				rowImage = 0;
			}
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
