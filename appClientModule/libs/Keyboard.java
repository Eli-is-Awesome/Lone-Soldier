package libs;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Keyboard implements KeyListener
{
	private volatile KeyEvent lastKeyboardEvent;
	private boolean stateChanged;

	/**
	 * Cconstructor
	 * 
	 */
	public Keyboard()
	{
		Robot robot;

		/**
		 * Add a keyboard listeners so we can get keyboard events.
		 */
		GameDisplay.addKeyboardListener( this );

		stateChanged = false;

		/**
		 * Fire a key event so we have a last key event
		 */
		try
		{
			robot = new Robot();
			robot.waitForIdle();
			robot.keyRelease( KeyEvent.VK_ALT );
		}
		catch( AWTException e )
		{
			throw new RuntimeException( e.getMessage() );
		}

	}

	/**
	 * @return the lastKeyboardEvent
	 */
	public KeyEvent getLastKeyboardEvent()
	{
		stateChanged = false;
		return lastKeyboardEvent;
	}

	/**
	 * @return the stateChanged
	 */
	public boolean isStateChanged()
	{
		return stateChanged;
	}

	@Override
	public void keyPressed( KeyEvent e )
	{
		this.processKeyboardEvent( e );
	}

	@Override
	public void keyReleased( KeyEvent e )
	{
		this.processKeyboardEvent( e );
	}

	@Override
	public void keyTyped( KeyEvent e )
	{
		// this.processKeyboardEvent( e );
	}

	/**
	 * Process the keyboard event
	 * 
	 * @param me
	 */
	private void processKeyboardEvent( KeyEvent ke )
	{
		lastKeyboardEvent = ke;
		stateChanged = true;
	}

}
