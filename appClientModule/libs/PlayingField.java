package libs;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;

/**
 * Playing field class to manage a virtual graphics display that is larger than
 * the physical display. This class would typically manage a scrolling
 * background.
 * 
 * @author williamhooper
 */
public abstract class PlayingField
{
	private static PlayingField currentPlayingField;

	/**
	 * Draw the playing field onto the graphics context
	 * 
	 * @param g2d
	 */
	public abstract void draw( Graphics2D g2d );

	/**
	 * Return the bounds of the playing field as a rectangle
	 * 
	 * @return Rectangle
	 */
	public abstract Rectangle getBounds();

	/**
	 * Check to see it there is a collision with anything on the playing field
	 * at the x,y coordinates
	 * 
	 * @param x
	 * @param y
	 * @return boolean
	 */
	public abstract boolean isCollision( double x, double y );

	/**
	 * Return an affine transformation for the provided x,y playing field
	 * coordinates converted to game display coordinates
	 * 
	 * @param x
	 * @param y
	 * @return AffineTransform
	 */
	public AffineTransform translate( double x, double y )
	{
		Point point = toGameDisplay( x, y );
		return AffineTransform.getTranslateInstance( point.x, point.y );
	}

	/**
	 * Update the position of the game display on the playing field
	 * 
	 * @param x
	 * @param y
	 */
	public abstract void update( double x, double y );

	/**
	 * Convert x,y playing field coordinates to a point on the game display
	 * 
	 * @param x
	 * @param y
	 * @return Point
	 */
	protected abstract Point toGameDisplay( double x, double y );

	/**
	 * Get the current playing field
	 * 
	 * @return the currentPlayingField
	 */
	public synchronized static PlayingField getCurrentPlayingField()
	{
		return currentPlayingField;
	}

	/**
	 * Set the current playing field
	 * 
	 * @param currentPlayingField
	 *            the currentPlayingField to set
	 */
	public synchronized static void setCurrentPlayingField(
			PlayingField currentPlayingField )
	{
		PlayingField.currentPlayingField = currentPlayingField;
	}
}
