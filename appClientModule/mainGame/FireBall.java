package mainGame;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.io.IOException;
import java.util.Random;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import libs.AudioSample;
import libs.GameEvent;
import libs.GameEventDispatcher;
import libs.PlayingField;
import libs.Sprite;
import libs.TileSheet;
import libs.GameEvent.GameEventType;

/**
 * Energy boss fireball.  The energy boss shoots these at the player
 * 
 * @author Eli Powell
 * 
 */
public class FireBall implements Sprite
{
	private double xPos;
	private double yPos;
	private static TileSheet fireball = null;
	private static Ellipse2D fireballShape;
	private int rowImage;
	private int columnImage;
	private long nextTime;
	private int yVel;
	private Random random;
	private AudioSample fire;

	/**
	 * Constructor
	 * 
	 */
	public FireBall( double x, double y )
	{
		xPos = x;
		yPos = y;
		yVel = 12;
		rowImage = 0;
		columnImage = 0;
		nextTime = System.currentTimeMillis();
		random = new Random();

		if( fireball == null )
		{
			/**
			 * Fireball
			 */
			try
			{
				fireball = new TileSheet( this, "images/fireBallTileSheet.png",
						64, 64 );
			}
			catch( IOException e )
			{
				e.printStackTrace();
			}
		}
		
		/**
		 * Create the fireball sound
		 */
		if( fire == null )
		{
			try
			{
				fire = new AudioSample( this, "audio/Fireball.wav" );
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

		fireballShape = new Ellipse2D.Double( xPos + 7, yPos + 7,
				fireball.getTileWidth() - 14, fireball.getTileHeight() - 14 );

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
		fireballShape.setFrame( new Rectangle( ( int ) transform
				.getTranslateX() + 7, ( int ) transform.getTranslateY() + 7,
				fireball.getTileWidth() - 14, fireball.getTileHeight() - 14 ) );
		g.drawImage( fireball.getTile( rowImage, columnImage ), transform, null );
	}

	@Override
	public void update()
	{
		PlayingField playingField = PlayingField.getCurrentPlayingField();

		yPos += yVel;
		if( playingField.isCollision( xPos + ( fireball.getTileHeight() / 2 ),
				yPos + fireball.getTileHeight() ) )
		{
			GameEventDispatcher.dispatchEvent( new GameEvent( this, GameEventType.Remove, this ) );
			fire.close();
		}

		if( nextTime <= System.currentTimeMillis() )
		{
			nextTime = System.currentTimeMillis()
					+ ( random.nextInt( 500 ) + 1 );
			columnImage++;
			if( columnImage <= fireball.getNumberColumns() )
			{
				rowImage++;
				if( rowImage <= fireball.getNumberRows() )
				{
					rowImage %= fireball.getNumberRows();
				}
			}
			columnImage %= fireball.getNumberColumns();
		}
	}

	@Override
	public Rectangle getBounds()
	{
		return fireballShape.getBounds();
	}

	@Override
	public Ellipse2D getEllipseBounds()
	{
		// TODO Auto-generated method stub
		return null;
	}

}
