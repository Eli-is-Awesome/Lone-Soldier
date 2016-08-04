package libs;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Ellipse2D;

/**
 * Sprite class
 * 
 * @author williamhooper
 */

public interface Sprite
{
	/**
	 * Determine if the passed Sprite object collided with this object.
	 * 
	 * @param obj
	 */
	public abstract void checkCollision( Sprite obj );

	/**
	 * Draw method
	 * 
	 * @param g
	 */
	public abstract void draw( Graphics2D g );

	/**
	 * Return the bounding box for this sprite
	 * 
	 */
	public abstract Rectangle getBounds();

	/**
	 * Return the circular bounding box for any that are needed
	 * 
	 * @return
	 */
	public abstract Ellipse2D getEllipseBounds();

	/**
	 * Update the sprite's state.
	 * 
	 */
	public abstract void update();

}
