package mainGame;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.io.IOException;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import libs.AudioSample;
import libs.GameDisplay;
import libs.GameEvent;
import libs.GameEventDispatcher;
import libs.Keyboard;
import libs.Mouse;
import libs.PlayingField;
import libs.Sprite;
import libs.TileSheet;
import libs.GameEvent.GameEventType;

/**
 * Major Paine sprite.  this is our main player.
 * a soldier who shoots his pistol to kill aliens
 * and save his family one by one
 * 
 * @author Eli Powell
 *
 */
public class MajorPaine implements Sprite
{
	public enum Direction
	{
		LEFT, RIGHT, UP, DOWN, NONE
	}

	private static TileSheet player = null;
	private static Rectangle playerShape;
	private int rowImage;
	private int columnImage;
	private boolean jumpFlag;
	private static boolean flipLeft;
	private static double xPos;
	private static double yPos;
	private int height;
	private int width;;
	static Direction direction;
	private Keyboard keyboard;
	private Mouse mouse;
	private long nextTime;
	private long shootTime;
	private int shootPosX;
	private int shootPosY;
	private double howHigh;
	private BulletPool bulletPool;
	private PistolBullet bullet;
	private static int hits;
	private AudioSample hitSound;

	public MajorPaine( double x, double y )
	{
		xPos = x;
		yPos = y;
		hits = 0;

		if( player == null )
		{
			/**
			 * This original image was created by Lano Lauretta specifically for
			 * Lone "Soldier"
			 */
			try
			{
				player = new TileSheet( this, "images/MajorPaineTilesheet.png",
						64, 64 );
			}
			catch( IOException e )
			{
				e.printStackTrace();
			}
		}

		/**
		 * Create the hit sound effects
		 */
		if( hitSound == null )
		{
			try
			{
				hitSound = new AudioSample( this, "audio/LS_Hit.wav" );
				hitSound.setVolume( 4 );
				hitSound.stop();
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

		playerShape = new Rectangle( ( int ) xPos, ( int ) yPos,
				player.getTileWidth(), player.getTileHeight() );

		height = player.getTileHeight();
		width = player.getTileWidth();

		jumpFlag = false;
		nextTime = System.currentTimeMillis() + 500;
		columnImage = 0;
		rowImage = 0;
		flipLeft = false;
		direction = Direction.NONE;

		/**
		 * Create our keyboard and mouse. We will need it
		 */
		keyboard = new Keyboard();
		mouse = new Mouse();
		bulletPool = new BulletPool();
	}

	@Override
	public void draw( Graphics2D g )
	{
		AffineTransform transform = new AffineTransform();
		transform.concatenate( PlayingField.getCurrentPlayingField().translate(
				xPos, yPos ) );
		playerShape.x = ( int ) transform.getTranslateX();
		playerShape.y = ( int ) transform.getTranslateY();
		if( flipLeft )
		{
			transform.translate( player.getTileWidth(), 0 );
			transform
					.concatenate( AffineTransform.getScaleInstance( -1.0, 1.0 ) );
		}
		g.drawImage( player.getTile( rowImage, columnImage ), transform, null );
	}

	@Override
	public void update()
	{
		PlayingField playingField = PlayingField.getCurrentPlayingField();
		Rectangle playingFieldBounds = playingField.getBounds();

		/**
		 * Fire a bullet
		 */
		if( mouse.isStateChanged() )
		{
			MouseEvent me = mouse.getLastMouseEvent();

			if( me.getButton() == MouseEvent.BUTTON1
					&& me.getID() == MouseEvent.MOUSE_CLICKED
					&& shootTime <= System.currentTimeMillis() )
			{
				shootPosX = me.getX();
				shootPosY = me.getY();
				shootTime = System.currentTimeMillis() + 250;

				/**
				 * Shoot straight if the mouse position is in front or behind
				 * the player
				 */
				if( shootPosY >= playerShape.y )
				{
					bullet = ( PistolBullet ) bulletPool.checkOut();
					if( bullet != null )
					{
						if( flipLeft )
						{
							bullet.setxPos( xPos );
							bullet.setyPos( yPos + height / 3 );
						}
						else
						{
							bullet.setxPos( xPos + width );
							bullet.setyPos( yPos + height / 3 );
						}
						GameEventDispatcher.dispatchEvent( new GameEvent( this,
								GameEventType.AddFirst, bullet ) );
					}

					if( nextTime <= System.currentTimeMillis() )
					{
						rowImage = 3;
						nextTime = System.currentTimeMillis() + 125;
						columnImage = player.getNumberColumns() - 3;
					}
				}

				/**
				 * Shoot up if the mouse is above the player
				 */
				if( ( shootPosX <= playerShape.x + width * 1.5 )
						&& shootPosX >= ( playerShape.x - width / 2 )
						&& shootPosY <= playerShape.y - 5 )
				{
					bullet = ( PistolBullet ) bulletPool.checkOut();
					if( bullet != null )
					{
						if( flipLeft )
						{
							bullet.setxPos( xPos + width - 5 + 5 );
							bullet.setyPos( yPos - 1 );
						}
						else
						{
							bullet.setxPos( xPos + 3 );
							bullet.setyPos( yPos - 1 );
						}
						GameEventDispatcher.dispatchEvent( new GameEvent( this,
								GameEventType.AddFirst, bullet ) );
					}

					if( nextTime <= System.currentTimeMillis() )
					{
						rowImage = 4;
						nextTime = System.currentTimeMillis() + 100;
						columnImage = player.getNumberColumns() - 4;
					}
				}

				/**
				 * Shoot diagonal in the direction the mouse is if the mouse is
				 * positioned anywhere outside of the straight shot or vertical
				 * shot
				 */
				if( ( ( shootPosX >= playerShape.x + width * 2 ) || ( shootPosX <= playerShape.x
						- width ) )
						&& shootPosY <= playerShape.y )
				{
					bullet = ( PistolBullet ) bulletPool.checkOut();
					if( bullet != null )
					{
						if( flipLeft )
						{
							bullet.setxPos( xPos + 5 );
							bullet.setyPos( yPos + 2 );
						}
						else
						{
							bullet.setxPos( xPos + width - 3 );
							bullet.setyPos( yPos + 2 );
						}
						GameEventDispatcher.dispatchEvent( new GameEvent( this,
								GameEventType.AddFirst, bullet ) );

						if( nextTime <= System.currentTimeMillis() )
						{
							rowImage = 5;
							nextTime = System.currentTimeMillis() + 100;
							columnImage = player.getNumberColumns() - 3;
						}
					}
				}

			}
		}

		/**
		 * Keyboard controls
		 */
		if( keyboard.isStateChanged() )
		{
			KeyEvent ke = keyboard.getLastKeyboardEvent();

			if( ke.getID() == KeyEvent.KEY_RELEASED )
			{
				direction = Direction.NONE;
			}
			else
			{
				switch( ke.getKeyCode() )
				{
					case KeyEvent.VK_UP:
						if( jumpFlag != true && playingField.isCollision( xPos + ( width / 2 ), yPos + height - 1 ) )
						{
							jumpFlag = true;
							howHigh = yPos - ( height * 2 );
						}
						break;

					case KeyEvent.VK_LEFT:
						direction = Direction.LEFT;
						break;

					case KeyEvent.VK_RIGHT:
						direction = Direction.RIGHT;
						break;

					case KeyEvent.VK_W:
						if( jumpFlag != true
								&& playingField.isCollision( xPos
										+ ( width / 2 ), yPos + height - 1 ) )
						{
							jumpFlag = true;
							howHigh = yPos - ( height * 2 );
						}
						break;

					case KeyEvent.VK_A:
						direction = Direction.LEFT;
						break;

					case KeyEvent.VK_D:
						direction = Direction.RIGHT;
						break;
				}
			}
		}

		/**
		 * If we're jumping then move us up, else we're going down
		 */
		if( jumpFlag )
		{
			if( !playingField.isCollision( xPos + ( width / 2 ), yPos ) )
			{
				yPos -= 5;

				if( direction == Direction.RIGHT )
				{
					xPos = xPos + 0.4;
				}
				if( direction == Direction.LEFT )
				{
					xPos = xPos - 0.4;
				}

				if( yPos <= howHigh )
				{
					jumpFlag = false;
				}
				rowImage = 2;
				columnImage = 1;
			}
			if( yPos <= 0 )
			{
				yPos = 0;
				jumpFlag = false;
			}
		}
		else
		{
			if( !playingField.isCollision( xPos + ( width / 2 ), yPos + height
					- 3 ) )
			{
				yPos += 5;

				if( direction == Direction.RIGHT )
				{
					xPos = xPos + 0.4;
				}
				if( direction == Direction.LEFT )
				{
					xPos = xPos - 0.4;
				}

				rowImage = 2;
				columnImage = player.getNumberColumns() - 4;
			}
			if( yPos >= ( playingFieldBounds.height - height ) )
			{
				yPos = playingFieldBounds.height - height;
			}

		}

		/**
		 * If we're moving in a direction then update
		 */
		switch( direction )
		{
			case LEFT:
				if( !playingField.isCollision( xPos, yPos + ( height / 2 ) ) )
				{
					xPos -= 3;
					if( nextTime <= System.currentTimeMillis() )
					{
						rowImage = 1;
						nextTime = System.currentTimeMillis() + 100;
						columnImage++;
						columnImage %= player.getNumberColumns();
					}
				}
				if( xPos <= 0 )
				{
					xPos = 0;
				}
				flipLeft = true;
				break;

			case RIGHT:
				if( !playingField.isCollision( xPos + width, yPos
						+ ( height / 2 ) ) )
				{
					xPos += 3;
					if( nextTime <= System.currentTimeMillis() )
					{
						rowImage = 1;
						nextTime = System.currentTimeMillis() + 100;
						columnImage++;
						columnImage %= player.getNumberColumns();
					}
				}
				if( xPos >= ( playingFieldBounds.width - width ) )
				{
					xPos = playingFieldBounds.width - width;
				}
				flipLeft = false;
				break;

			case NONE:
				if( nextTime <= System.currentTimeMillis() )
				{
					rowImage = 0;
					nextTime = System.currentTimeMillis() + 125;
					columnImage++;
					columnImage %= player.getNumberColumns() - 3;
				}
				break;

			default:
				break;
		}

		/**
		 * If we hit the bottom of the screen, die
		 */
		if( yPos + height >= GameDisplay.getBounds().getHeight() )
		{
			GameEventDispatcher.dispatchEvent( new GameEvent( this,
					GameEventType.Death, this ) );
		}

	}

	static boolean isFlipLeft()
	{
		return flipLeft;
	}

	@Override
	public void checkCollision( Sprite obj )
	{
		/**
		 * If the laser hits us, remove the laser, take some life from the meter,
		 * play the hit sound and count the hits
		 */
		if( obj instanceof Laser )
		{
			if( playerShape.intersects( obj.getBounds() ) )
			{
				GameEventDispatcher.dispatchEvent( new GameEvent( this,
						GameEventType.Remove, obj ) );
				GameEventDispatcher.dispatchEvent( new GameEvent( this,
						GameEventType.Hit, new Integer( 16 ) ) );
				hitSound.play();
				hits++;
			}
		}
		/**
		 * If the Big Flyer runs into us, take some life from the meter,
		 * play the hit sound and count the hits
		 */
		if( obj instanceof BigFlyer )
		{
			Rectangle newShape = new Rectangle( ( int ) playerShape.getX(),
					( int ) playerShape.getY(), 3, playerShape.height );
			if( newShape.intersects( obj.getBounds().getX(), obj.getBounds()
					.getY(), 3, 64 ) )
			{
				GameEventDispatcher.dispatchEvent( new GameEvent( this,
						GameEventType.Hit, new Integer( 16 ) ) );
				hitSound.play();
				hits++;
			}
		}
		/**
		 * If the fireball hits us, remove the fireball, take some life from the meter,
		 * play the hit sound and count the hits
		 */
		if( obj instanceof FireBall )
		{
			if( playerShape.intersects( obj.getBounds() ) )
			{
				GameEventDispatcher.dispatchEvent( new GameEvent( this,
						GameEventType.Remove, obj ) );
				GameEventDispatcher.dispatchEvent( new GameEvent( this,
						GameEventType.Hit, new Integer( 16 ) ) );
				hitSound.play();
				hits++;
			}
		}
		/**
		 * If the Big Drone hits us, take some life from the meter,
		 * play the hit sound and count the hits
		 */
		if( obj instanceof BigDrone )
		{
			Rectangle newShape = new Rectangle( ( int ) playerShape.getX(),
					( int ) playerShape.getY(), 3, playerShape.height );
			if( newShape.intersects( obj.getBounds().getX(), obj.getBounds()
					.getY(), 3, 64 ) )
			{
				GameEventDispatcher.dispatchEvent( new GameEvent( this,
						GameEventType.Hit, new Integer( 16 ) ) );
				hitSound.play();
				hits++;
			}
		}

		/**
		 * if the player gets hit 16 times, he dies
		 */
		if( hits >= 16 )
		{
			GameEventDispatcher.dispatchEvent( new GameEvent( this,
					GameEventType.Remove, this ) );
			GameEventDispatcher.dispatchEvent( new GameEvent( this,
					GameEventType.Death, this ) );
			hitSound.close();
		}
	}

	@Override
	public Rectangle getBounds()
	{
		return new Rectangle( playerShape.x, playerShape.y, width, height );
	}

	@Override
	public Ellipse2D getEllipseBounds()
	{
		return null;
	}

	static double getxPos()
	{
		return xPos;
	}

	static void setxPos( double xPos )
	{
		MajorPaine.xPos = xPos;
	}

	static double getyPos()
	{
		return yPos;
	}

	static void setyPos( double yPos )
	{
		MajorPaine.yPos = yPos;
	}

	public static void resetHits()
	{
		hits = 0;
	}

}
