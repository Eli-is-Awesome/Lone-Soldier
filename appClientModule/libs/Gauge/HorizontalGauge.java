package libs.Gauge;

import java.awt.*;
import java.awt.geom.Ellipse2D;

import libs.Sprite;

public class HorizontalGauge extends Gauge implements Sprite
{
	public HorizontalGauge( String d, String ls, String us, int positionX,
			int positionY, int w, int h, float gp, float yp, float rp, int ip )
	{
		super( d, ls, us, positionX, positionY, w, h, gp, yp, rp, ip );
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

	/*
	 * public void draw( Graphics g ) { g.setColor( Color.GREEN ); g.fillRect(
	 * x, y, greenWidth(), height ); g.setColor( Color.YELLOW ); g.fillRect(
	 * x+greenWidth(), y, yellowWidth(), height ); g.setColor( Color.RED );
	 * g.fillRect( x+greenWidth()+yellowWidth(), y, redWidth(), height );
	 * g.setColor( Color.BLACK ); g.drawRect( x, y, width, height ); g.setFont(
	 * new Font("Times New Roman", Font.BOLD, 24) ); g.drawString( description,
	 * x+(width/2)-(g.getFontMetrics().stringWidth( description )/2),
	 * y+height+22 ); g.setFont( new Font("Arial", Font.ITALIC, 16) );
	 * g.drawString( lowerScale, x, y-2 ); g.drawString( upperScale,
	 * x+width-(g.getFontMetrics().stringWidth( upperScale )), y-2 );
	 * g.setColor( Color.WHITE ); g.fill3DRect( x+(int)indicatorPosition-3, y-2,
	 * 6, height+4, false ); g.setColor( Color.BLACK ); g.drawRect(
	 * x+(int)indicatorPosition-3, y-2, 6, height+4 ); }
	 */

	@Override
	public void checkCollision( Sprite obj )
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void draw( Graphics2D g )
	{
		g.setColor( Color.GREEN );
		g.fillRect( x, y, greenWidth(), height );
		g.setColor( Color.YELLOW );
		g.fillRect( x + greenWidth(), y, yellowWidth(), height );
		g.setColor( Color.RED );
		g.fillRect( x + greenWidth() + yellowWidth(), y, redWidth(), height );
		g.setColor( Color.BLACK );
		g.drawRect( x, y, width, height );
		g.setFont( new Font( "Times New Roman", Font.BOLD, 24 ) );
		g.drawString( description, x + ( width / 2 )
				- ( g.getFontMetrics().stringWidth( description ) / 2 ), y
				+ height + 22 );
		g.setFont( new Font( "Arial", Font.ITALIC, 16 ) );
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
	public Rectangle getBounds()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Ellipse2D getEllipseBounds()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void update()
	{
		// TODO Auto-generated method stub

	}
}
