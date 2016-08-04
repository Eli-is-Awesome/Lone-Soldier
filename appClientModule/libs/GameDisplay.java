package libs;

import java.awt.AWTException;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.DisplayMode;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferStrategy;

/**
 * Game display
 * 
 * @author williamhooper
 */
public class GameDisplay
{
	private static BufferStrategy bufferStrategy;
	private static Graphics currentGraphics;
	private static Frame frameInstance = null;
	private static boolean captureCursor;
	private static Robot robot;

	/**
	 * Add a keyboard listener
	 * 
	 * @param kel
	 */
	public static synchronized void addKeyboardListener( KeyListener kel )
	{
		if( frameInstance != null )
		{
			frameInstance.addKeyListener( kel );
		}
		else
		{
			throw new RuntimeException( "Game display not created" );
		}
	}

	/**
	 * Create a game display
	 * 
	 * @param width
	 * @param height
	 * @throws AWTException
	 */
	public static void create( int width, int height )
	{
		if( frameInstance == null )
		{
			GraphicsEnvironment env = GraphicsEnvironment
					.getLocalGraphicsEnvironment();
			GraphicsDevice screenDevice = env.getDefaultScreenDevice();
			GraphicsConfiguration gc = screenDevice.getDefaultConfiguration();
			frameInstance = new Frame( gc );

			/**
			 * Set the capture cursor state to false
			 */
			captureCursor = false;

			/**
			 * Turn off auto repaint and decorations from Frame Class
			 */
			frameInstance.setIgnoreRepaint( true );
			frameInstance.setUndecorated( true );

			/**
			 * Get focus so events come to here
			 */
			frameInstance.setFocusable( true );
			frameInstance.requestFocus(); // makes our program the priority

			/**
			 * Set the graphics frame width and height
			 */
			frameInstance.setSize( new Dimension( width, height ) );

			/**
			 * Center frame on screen
			 */
			frameInstance.setLocationRelativeTo( null );

			/**
			 * Add a mouse listener so we can get mouse exit events
			 */
			frameInstance.addMouseListener( new MouseAdapter()
			{
				@Override
				public void mouseExited( MouseEvent me )
				{
					int newX;
					int newY;

					if( captureCursor && !me.isShiftDown() )
					{
						/**
						 * If the mouse is moved outside the frame, move it back
						 */
						if( !frameInstance.getBounds().contains(
								me.getXOnScreen(), me.getYOnScreen() ) )
						{
							newX = me.getXOnScreen();
							if( me.getXOnScreen() < frameInstance.getX() )
							{
								newX = frameInstance.getX();
							}
							else if( me.getXOnScreen() > ( frameInstance.getX() + frameInstance
									.getWidth() ) )
							{
								newX = ( frameInstance.getX()
										+ frameInstance.getWidth() - 1 );
							}

							newY = me.getYOnScreen();
							if( me.getYOnScreen() < frameInstance.getY() )
							{
								newY = frameInstance.getY();
							}
							else if( me.getYOnScreen() > ( frameInstance.getY() + frameInstance
									.getHeight() ) )
							{
								newY = ( frameInstance.getY()
										+ frameInstance.getHeight() - 1 );
							}

							robot.mouseMove( newX, newY );
						}
					}
				}
			} );

			/**
			 * Create a robot to manage our mouse
			 */
			try
			{
				Rectangle bounds = GameDisplay.getBounds();
				robot = new Robot( screenDevice );
				robot.mouseMove( bounds.x + bounds.width / 2, bounds.y
						+ bounds.width / 2 );
			}
			catch( AWTException e )
			{
				throw new RuntimeException( e.getMessage() );
			}

			/**
			 * Make the frame visible. Wait until the frame is really visible to
			 * avoid an exception when creating the buffer strategy
			 */
			frameInstance.setVisible( true );
			do
			{
			} while( !frameInstance.isVisible() );

			/**
			 * Create a double buffer strategy
			 */
			frameInstance.createBufferStrategy( 2 ); // sets BufferStrategy to 2
														// buffers
			bufferStrategy = frameInstance.getBufferStrategy();
		}
	}

	/**
	 * Dispose of the game display
	 * 
	 */
	public static void dispose()
	{
		if( frameInstance != null )
		{
			/**
			 * Dispose of the frame instance and set to null
			 */
			frameInstance.dispose();
			frameInstance = null;
		}
		else
		{
			throw new RuntimeException( "Game display not created" );
		}
	}

	/**
	 * Return the bounds of the game display as a rectangle
	 * 
	 * @return Rectangle
	 */
	public static Rectangle getBounds()
	{
		if( frameInstance != null )
		{
			return new Rectangle( 0, 0, frameInstance.getWidth(),
					frameInstance.getHeight() );
		}
		else
		{
			throw new RuntimeException( "Game display not created" );
		}
	}

	/**
	 * Get the current graphics context
	 * 
	 * @return Graphics
	 */
	public static Graphics getContext()
	{
		if( frameInstance != null )
		{
			currentGraphics = bufferStrategy.getDrawGraphics();
			return currentGraphics;
		}
		else
		{
			throw new RuntimeException( "Game display not created" );
		}
	}

	/**
	 * Hide the cursor
	 * 
	 */
	public static void hideCursor()
	{
		if( frameInstance != null )
		{
			/**
			 * Hide the mouse cursor
			 */
			Image cursorImage = Toolkit.getDefaultToolkit().getImage(
					"xparent.gif" );
			Cursor blankCursor = Toolkit.getDefaultToolkit()
					.createCustomCursor( cursorImage, new Point( 0, 0 ), "" );
			frameInstance.setCursor( blankCursor );
		}
		else
		{
			throw new RuntimeException( "Game display not created" );
		}
	}

	/**
	 * @return the captureCursor
	 */
	public static boolean isCaptureCursor()
	{
		return captureCursor;
	}

	/**
	 * @param captureCursor
	 *            the captureCursor to set
	 */
	public static void setCaptureCursor( boolean captureCursor )
	{
		GameDisplay.captureCursor = captureCursor;
	}

	/**
	 * Set to full screen
	 * 
	 */
	public static void setFullScreen()
	{
		if( frameInstance != null )
		{
			int width = frameInstance.getWidth();
			int height = frameInstance.getHeight();

			GraphicsEnvironment env = GraphicsEnvironment
					.getLocalGraphicsEnvironment();
			GraphicsDevice screenDevice = env.getDefaultScreenDevice();

			/**
			 * If full screen is supported then switch to full screen mode and
			 * find the best display mode that matches the frame instance
			 */
			if( screenDevice.isFullScreenSupported() )
			{
				if( screenDevice.isDisplayChangeSupported() )
				{
					setDisplayMode( screenDevice, width, height );
					screenDevice.setFullScreenWindow( frameInstance );
				}
				else
				{
					throw new RuntimeException(
							"display unable to change to requested resolution" );
				}
			}
		}
		else
		{
			throw new RuntimeException( "Game display not created" );
		}
	}

	/**
	 * Update the current graphics screen
	 * 
	 */
	public static void update()
	{
		if( frameInstance != null )
		{
			/**
			 * If the buffer got lost (graphics memory reallocated before we
			 * were done with it) then don't show.
			 */
			if( !bufferStrategy.contentsLost() )
			{
				bufferStrategy.show();
			}

			/**
			 * Dispose of the graphics like we were asked to do
			 */
			if( currentGraphics != null )
			{
				currentGraphics.dispose();
			}

			/**
			 * Make sure the context buffer is flushed to the screen
			 */
			Toolkit.getDefaultToolkit().sync();
		}
		else
		{
			throw new RuntimeException( "Game display not created" );
		}
	}

	/**
	 * Set the best display mode for the given width and height
	 * 
	 * @param device
	 */
	private static void setDisplayMode( GraphicsDevice device, int width,
			int height )
	{
		int maxBitDepth = -1;
		DisplayMode bestMode;
		DisplayMode[] modes = device.getDisplayModes();

		bestMode = null;
		for( int i = 0; i < modes.length; i++ )
		{
			if( modes[i].getWidth() == width && modes[i].getHeight() == height
					&& modes[i].getBitDepth() > maxBitDepth )
			{
				bestMode = modes[i];
			}
		}
		if( bestMode != null )
		{
			device.setDisplayMode( bestMode );
		}
		else
		{
			throw new RuntimeException( "Unable to find matching display mode" );
		}
	}

	/**
	 * Add a mouse motion listener
	 * 
	 * @param mel
	 */
	protected static synchronized void addMouseListener( MouseListener mel )
	{
		if( frameInstance != null )
		{
			frameInstance.addMouseListener( mel );
		}
		else
		{
			throw new RuntimeException( "Game display not created" );
		}
	}

	/**
	 * Add a mouse listener
	 * 
	 * @param mel
	 */
	protected static synchronized void addMouseMotionListener(
			MouseMotionListener mel )
	{
		if( frameInstance != null )
		{
			frameInstance.addMouseMotionListener( mel );
		}
		else
		{
			throw new RuntimeException( "Game display not created" );
		}
	}

	/**
	 * Private constructor
	 * 
	 */
	private GameDisplay()
	{
		/**
		 * no code required
		 */
	}

	@Override
	public Object clone() throws CloneNotSupportedException
	{
		throw new CloneNotSupportedException();
	}
}
