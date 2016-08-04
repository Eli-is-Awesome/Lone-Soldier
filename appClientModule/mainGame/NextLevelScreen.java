package mainGame;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.geom.Ellipse2D;

import libs.Sprite;

/**
 * The next level screen.  It displays between levels and 
 * counts down a timer until you can start
 * 
 * @author Eli Powell
 * 
 */
public class NextLevelScreen implements Sprite
{
	private static Font f;
	private int time;
	private String timeString;
	private long nextTime;

	/**
	 * Constructor
	 * 
	 */
	public NextLevelScreen()
	{
		time = 10;
		timeString = Integer.toString( time );

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
		g.drawString( "Level 2", 335, 320 );
		g.setFont( new Font( "Sans Serif", Font.PLAIN, 18 ) );
		g.drawString( "You will be able to restart Level 2 if you die,", 250,
				350 );
		g.drawString( "but your score will reset! Be Careful!!", 270, 380 );
		g.setFont( new Font( f.getName(), Font.PLAIN, 32 ) );
		if( time <= 0 )
		{
			g.drawString( "Press S  to start  the next level", 250, 145 );
		}
		g.drawString( "Next Level ready in: ", 170, 70 );
	}

	@Override
	public void update()
	{
		if( nextTime <= System.currentTimeMillis() )
		{
			timeString = Integer.toString( time );
			if( time > 0 )
			{
				time--;
				nextTime = System.currentTimeMillis() + 1000;
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
