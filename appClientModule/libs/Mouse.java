package libs;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class Mouse implements MouseListener, MouseMotionListener
{
	private volatile MouseEvent lastMouseEvent;
	private volatile boolean stateChanged;

	/**
	 * Constructor
	 * 
	 */
	public Mouse()
	{
		/**
		 * Add a mouse listeners so we can get mouse motion.
		 */
		GameDisplay.addMouseListener( this );
		GameDisplay.addMouseMotionListener( this );

		/**
		 * Set the state changed
		 */
		stateChanged = false;
	}

	/**
	 * @return the lastMouseEvent
	 */
	public MouseEvent getLastMouseEvent()
	{
		stateChanged = false;
		return lastMouseEvent;
	}

	/**
	 * @return the stateChanged
	 */
	public boolean isStateChanged()
	{
		return stateChanged;
	}

	@Override
	public void mouseClicked( MouseEvent e )
	{
		this.processMouseEvent( e );
	}

	@Override
	public void mouseDragged( MouseEvent e )
	{
		this.processMouseEvent( e );
	}

	@Override
	public void mouseEntered( MouseEvent e )
	{
		this.processMouseEvent( e );
	}

	@Override
	public void mouseExited( MouseEvent e )
	{
		this.processMouseEvent( e );
	}

	@Override
	public void mouseMoved( MouseEvent e )
	{
		this.processMouseEvent( e );
	}

	@Override
	public void mousePressed( MouseEvent e )
	{
		this.processMouseEvent( e );
	}

	@Override
	public void mouseReleased( MouseEvent e )
	{
		this.processMouseEvent( e );
	}

	/**
	 * Process the mouse event
	 * 
	 * @param me
	 */
	private void processMouseEvent( MouseEvent me )
	{
		lastMouseEvent = me;
		stateChanged = true;
	}

}
