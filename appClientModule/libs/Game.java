package libs;

/**
 * Game interface for the Game Engine class
 * 
 * @author williamhooper
 */
public interface Game
{
	/**
	 * Check for collisions
	 */
	abstract public void collisions();

	/**
	 * Update the game states
	 */
	abstract public void update();

	/**
	 * Render graphics onto the offscreen buffer
	 */
	abstract public void render();

	/**
	 * Actively draw on the screen.
	 */
	abstract public void draw();

	/**
	 * Actively draw any foreground on the screen.
	 */
	abstract public void drawFront();

	/**
	 * Manage game event
	 */
	abstract public void manageGameEvent( GameEvent ge );
}
