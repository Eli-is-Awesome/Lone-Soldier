package mainGame;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.geom.Ellipse2D;
import java.io.IOException;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import libs.GameEvent;
import libs.GameEvent.GameEventType;
import libs.GameEvent.Level;
import libs.AudioSample;
import libs.GameEventDispatcher;
import libs.Keyboard;
import libs.Sprite;

/**
 * The winning sprite screen. For now, it is just a black background that says
 * you win and lets you know to expect more levels in the future.
 * You have 30 seconds to decided to play again before it switches to the credits.
 */
public class YouWinSprite implements Sprite
{
	private static Font f;
	private int time;
	private String timeString;
	private long nextTime;
	private Keyboard keyboard;
	private AudioSample bgMusic;

	/**
	 * Constructor
	 * 
	 */
	public YouWinSprite()
	{
		time = 30;
		timeString = Integer.toString( time );
		keyboard = new Keyboard();

		/**
		 * Create the background music
		 */
		if( bgMusic == null )
		{
			try
			{
				bgMusic = new AudioSample( this, "audio/End.wav" );
				bgMusic.setVolume( ( float ) 6.5 );
				bgMusic.loop( AudioSample.LOOP_CONTINUOUSLY );
				bgMusic.play();
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

		GraphicsEnvironment e = GraphicsEnvironment
				.getLocalGraphicsEnvironment();
		Font[] fonts = e.getAllFonts(); // Get the fonts
		for( Font font : fonts )
		{
			if( font.getFamily().equals( "Brush Script MT" )
					&& font.getName().equals( "BrushScriptMT" ) )
			{
				f = new Font( "BrushScriptMT", Font.PLAIN, 72 );
				break;
			}
			else if( font.getFontName().equals( "Rage Italic" ) )
			{
				f = new Font( "Rage Italic", Font.BOLD, 72 );
				break;
			}
			else
			{
				f = new Font( "Sans Serif", Font.BOLD, 64 );
			}
		}

	}

	@Override
	public void checkCollision( Sprite obj )
	{

	}

	@Override
	public void draw( Graphics2D g )
	{
		g.setColor( Color.BLACK );
		g.fillRect( 0, 0, 875, 640 );
		g.setFont( f );
		g.setColor( Color.WHITE );
		g.drawString( timeString, 420, 100 );
		g.drawString( "You Won!!", 289, 320 );
		g.setFont( new Font( "Sans Serif", Font.PLAIN, 14 ) );
		g.drawString( "Congratulations!  You survived the first 2 levels,", 240, 360 );
		g.drawString( "stay tuned for more updates in the future!", 260, 390 );
		g.setFont( new Font( f.getFamily(), Font.PLAIN, 32 ) );
		g.drawString( "Credits scheduled to begin in: ", 80, 70 );
		g.drawString( "Press S  before time is up to start  the game from the beginning", 100, 500 );
	}

	@Override
	public void update()
	{
		if( keyboard.isStateChanged() )
		{
			KeyEvent ke = keyboard.getLastKeyboardEvent();
			if( ke.getID() == KeyEvent.KEY_RELEASED )
			{
				switch( ke.getKeyCode() )
				{
					case KeyEvent.VK_S:
						bgMusic.close();
						LoneSoldier.setLevel( Level.One );
						GameEventDispatcher.dispatchEvent( new GameEvent( null,
								GameEventType.Restart, null ) );
						break;
				}
			}
		}

		if( nextTime <= System.currentTimeMillis() )
		{
			timeString = Integer.toString( time );
			if( time > 0 )
			{
				time--;
				nextTime = System.currentTimeMillis() + 1000;
				if( time <= 0 )
				{
					bgMusic.close();
					CreditsSprite credits = new CreditsSprite();
					GameEventDispatcher.dispatchEvent( new GameEvent( this,
							GameEventType.Remove, this ) );
					GameEventDispatcher.dispatchEvent( new GameEvent( this,
							GameEventType.AddFirst, credits ) );
					bgMusic.close();
				}
			}
		}
	}

	@Override
	public Rectangle getBounds()
	{
		return null;
	}

	@Override
	public Ellipse2D getEllipseBounds()
	{
		return null;
	}

}
