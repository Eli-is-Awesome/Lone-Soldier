package mainGame;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.geom.Ellipse2D;

import libs.Sprite;
import libs.Gauge.Gauge;

/**
 * The life sprite. This sprite displays the life of the player in the upper left corner of
 * the display
 * 
 * @author Eli Powell
 * 
 */
public class LifeSprite extends Gauge implements Sprite
{
	private static Font f;

	/**
	 * Constructor
	 * 
	 */
	public LifeSprite( String d, String ls, String us, int positionX,
			int positionY, int w, int h, float gp, float yp, float rp, int ip )
	{
		super( d, ls, us, positionX, positionY, w, h, gp, yp, rp, ip );

		GraphicsEnvironment e = GraphicsEnvironment
				.getLocalGraphicsEnvironment();
		Font[] fonts = e.getAllFonts(); // Get the fonts
		for( Font font : fonts )
		{
			if( font.getFamily().equals( "Brush Script MT" )
					&& font.getName().equals( "BrushScriptMT" ) )
			{
				f = new Font( "BrushScriptMT", Font.PLAIN, 20 );
				break;
			}
			else if( font.getFontName().equals( "Rage Italic" ) )
			{
				f = new Font( "Rage Italic", Font.BOLD, 20 );
				break;
			}
			else
			{
				f = new Font( "Sans Serif", Font.BOLD, 18 );
			}
		}
	}

	@Override
	protected int greenWidth()
	{
		return Math.round( greenPercent * width );
	}

	@Override
	protected int yellowWidth()
	{
		return Math.round( yellowPercent * width );
	}

	@Override
	protected int redWidth()
	{
		return Math.round( redPercent * width );
	}

	@Override
	public void checkCollision( Sprite obj )
	{

	}

	@Override
	public void draw( Graphics2D g )
	{
		g.setColor( Color.RED );
		g.fillRect( x, y, greenWidth(), height );
		g.setColor( Color.YELLOW );
		g.fillRect( x + greenWidth(), y, yellowWidth(), height );
		g.setColor( Color.GREEN );
		g.fillRect( x + greenWidth() + yellowWidth(), y, redWidth(), height );
		g.setColor( Color.WHITE );
		g.drawRect( x, y, width, height );
		g.setFont( f );
		g.drawString( description, x + ( width / 2 )
				- ( g.getFontMetrics().stringWidth( description ) / 2 ), y
				+ height + 22 );
		g.setFont( f );
		g.drawString( lowerScale, x, y - 2 );
		g.drawString( upperScale,
				x + width - ( g.getFontMetrics().stringWidth( upperScale ) ),
				y - 2 );
		g.setColor( Color.WHITE );
		g.fill3DRect( x + ( int ) indicatorPosition - 3, y - 2, 6, height + 4,
				false );
		g.setColor( Color.BLACK );
		g.drawRect( x + ( int ) indicatorPosition - 3, y - 2, 6, height + 4 );
	}

	@Override
	public void update()
	{

	}

	public void reset()
	{
		indicatorPosition = width;
	}

	/**
	 * Add points to the score
	 * 
	 * @param value
	 */
	public void hit( int value )
	{
		indicatorPosition -= value;
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
