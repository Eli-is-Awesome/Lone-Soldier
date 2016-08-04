package mainGame;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.io.IOException;
import java.util.Random;

import libs.GameEvent;
import libs.GameEvent.GameState;
import libs.GameEventDispatcher;
import libs.PlayingField;
import libs.Sprite;
import libs.TileSheet;
import libs.GameEvent.GameEventType;

/**
 * Energy Boss Sprite.  This is a fireball that is supposed 
 * to represent an energy ball.  It fires little fireballs
 * at the player
 * 
 * @author Eli Powell
 * 
 */
public class EnergyBoss implements Sprite
{
	private double xPos;
	private double yPos;
	private static TileSheet boss1 = null;
	private static Ellipse2D bossShape;
	private int rowImage;
	private int columnImage;
	private long nextTime;
	private long shootTime;
	private int xVel;
	private boolean flipit;
	private int bossHits;
	private Random random;

	/**
	 * Constructor
	 * 
	 */
	public EnergyBoss( double x, double y )
	{
		xPos = x;
		yPos = y;
		xVel = 5;
		rowImage = 0;
		columnImage = 0;
		bossHits = 0;
		nextTime = System.currentTimeMillis();
		random = new Random();

		if( boss1 == null )
		{
			/**
			 * Fireball that looks like energy ball
			 */
			try
			{
				boss1 = new TileSheet( this, "images/energyBallTileSheet.png",
						128, 128 );
			}
			catch( IOException e )
			{
				e.printStackTrace();
			}
		}

		bossShape = new Ellipse2D.Double( xPos + 14, yPos + 14,
				boss1.getTileWidth() - 28, boss1.getTileHeight() - 28 );
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
			if( bossShape.intersects( obj.getBounds() ) )
			{
				GameEventDispatcher.dispatchEvent( new GameEvent( this,
						GameEventType.Remove, obj ) );
				GameEventDispatcher.dispatchEvent( new GameEvent( this,
						GameEventType.Score, new Integer( 100 ) ) );

				/**
				 * if the boss gets hit Ten Times, you win!
				 */
				if( bossHits >= 10 )
				{
					GameEventDispatcher.dispatchEvent( new GameEvent( this,
							GameEventType.Score, new Integer( 10000 ) ) );
					GameEventDispatcher.dispatchEvent( new GameEvent( this,
							GameEventType.Remove, this ) );
					LoneSoldier.setGameState( GameState.Finish );
				}
				bossHits++;
			}
		}
	}

	@Override
	public void draw( Graphics2D g )
	{
		AffineTransform transform = new AffineTransform();
		transform.concatenate( PlayingField.getCurrentPlayingField().translate(
				xPos, yPos ) );
		bossShape.setFrame( new Rectangle(
				( int ) transform.getTranslateX() + 14, ( int ) transform
						.getTranslateY() + 14, boss1.getTileWidth() - 28, boss1
						.getTileHeight() - 28 ) );
		g.drawImage( boss1.getTile( rowImage, columnImage ), transform, null );
	}

	@Override
	public void update()
	{
		PlayingField playingField = PlayingField.getCurrentPlayingField();

		/**
		 * Let's keep the energy ball moving back and forth above the player's
		 * head
		 */
		if( xPos >= MajorPaine.getxPos() - 256
				&& xPos <= playingField.getBounds().getWidth()
				&& flipit != true )
		{
			flipit = false;
		}
		else
		{
			flipit = true;
			if( xPos + this.getEllipseBounds().getFrame().getWidth() >= MajorPaine
					.getxPos() + 384 )
			{
				flipit = false;
			}
		}

		if( flipit )
		{
			xPos += xVel;
		}
		else
		{
			xPos -= xVel;
		}

		/**
		 * Fire some little fireballs at the player
		 */
		int time;
		time = random.nextInt( 2000 ) + 1001;
		if( shootTime <= System.currentTimeMillis()
				&& xPos < MajorPaine.getxPos() + 300 )
		{
			shootTime = System.currentTimeMillis() + time;
			FireBall fireball = new FireBall( xPos, yPos + 120 );
			GameEventDispatcher.dispatchEvent( new GameEvent( this,
					GameEventType.AddFirst, fireball ) );
		}

		/**
		 * Animate the energy ball
		 */
		if( nextTime <= System.currentTimeMillis() )
		{
			nextTime = System.currentTimeMillis() + 128;
			columnImage++;
			if( columnImage <= boss1.getNumberColumns() )
			{
				rowImage++;
				if( rowImage <= boss1.getNumberRows() )
				{
					rowImage %= boss1.getNumberRows();
				}
			}
			columnImage %= boss1.getNumberColumns();
		}

	}

	@Override
	public Rectangle getBounds()
	{
		return bossShape.getBounds();
	}

	public double getxPos()
	{
		return bossShape.getX();
	}

	public void setxPos( double xPos )
	{
		this.xPos = xPos;
	}

	public double getyPos()
	{
		return bossShape.getY();
	}

	public void setyPos( double yPos )
	{
		this.yPos = yPos;
	}

	@Override
	public Ellipse2D getEllipseBounds()
	{
		return bossShape;
	}
}
