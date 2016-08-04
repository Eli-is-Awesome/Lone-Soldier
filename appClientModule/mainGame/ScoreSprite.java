package mainGame;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.geom.Ellipse2D;

import libs.Sprite;

/**
 * The score sprite. This sprite displays the score in the bottom left corner of
 * the display
 * 
 * @author williamhooper
 * 
 */
public class ScoreSprite implements Sprite
{
	private int score;
	private static Font f;

	/**
	 * Constructor
	 * 
	 */
	public ScoreSprite()
	{
		GraphicsEnvironment e = GraphicsEnvironment
				.getLocalGraphicsEnvironment();
		Font[] fonts = e.getAllFonts(); // Get the fonts
		for( Font font : fonts )
		{
			if( font.getFamily().equals( "Brush Script MT" )
					&& font.getName().equals( "BrushScriptMT" ) )
			{
				f = new Font( "BrushScriptMT", Font.PLAIN, 24 );
				break;
			}
			else if( font.getFontName().equals( "Rage Italic" ) )
			{
				f = new Font( "Rage Italic", Font.BOLD, 24 );
				break;
			}
			else
			{
				f = new Font( "Sans Serif", Font.BOLD, 24 );
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
		g.setFont( f );
		g.setColor( Color.WHITE );
		g.drawString( "Score " + score, 16, 612 );
	}

	@Override
	public void update()
	{

	}

	public void reset()
	{
		score = 0;
	}

	/**
	 * Add points to the score
	 * 
	 * @param value
	 */
	public void add( int value )
	{
		score += value;
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
