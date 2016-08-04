package mainGame;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import libs.GameDisplay;
import libs.GameEvent;
import libs.GameEvent.Level;
import libs.AudioSample;
import libs.GameEventDispatcher;
import libs.Keyboard;
import libs.PlayingField;
import libs.Sprite;
import libs.Game;
import libs.GameEngine;
import libs.SpritePool;
import libs.SpritePoolObject;
import libs.GameEvent.GameEventType;
import libs.GameEvent.GameState;

/**
 * Tile based side scroller
 * 
 * @author williamhooper $Id: TileScroll.java,v 1.2 2011/06/01 04:21:21
 *         williamhooper Exp $
 *         
 * @author Eli Powell
 */
public class LoneSoldier implements Game
{
	protected static TilePlayingField playingField;
	protected static TilePlayingField2 playingField2;
	private ArrayList<Sprite> spriteList;
	protected static BufferedImage backgroundImage;

	/**
	 * Sprite variables
	 */
	private static MajorPaine player;
	private static EnergyBoss energyBoss;
	private static GameState gameState;
	private static Level level;
	private static ScoreSprite scoreSprite;
	private static LifeSprite lifeSprite;
	private static StartSprite startSprite;
	private static NextLevelScreen nextLevel;
	private static Roger roger;

	private Keyboard keyboard;

	/**
	 * Enemy and Boss variables
	 */
	private double bossLaunch;
	private int numberOfEnemys;
	private int enemyCount;
	private boolean bfFlag;
	private boolean bf1;
	private boolean bf2;
	private boolean bf3;
	private boolean bf4;
	private boolean lfFlag;
	private boolean lf1;
	private boolean lf2;
	private boolean lf3;
	private long lfNextEnemyTime;
	private long bgNextEnemytime;
	private long nextDroneTime;
	private int droneCount;
	private int MAX_DRONES = 5;

	private static AudioSample bgMusic;
	private static AudioSample bossMusic;

	/**
	 * Main
	 * 
	 * @param args
	 */
	public static void main( String[] args )
	{
		LoneSoldier game = new LoneSoldier();
		GameEngine.start( game );

		/**
		 * If we are here we have stopped the game engine
		 */
		GameDisplay.dispose();
	}

	/**
	 * Constructor
	 * 
	 * @throws LineUnavailableException
	 * @throws UnsupportedAudioFileException
	 * @throws IOException
	 */
	public LoneSoldier()
	{
		/**
		 * Create our game display
		 */
		GameDisplay.create( 875, 640 );

		/**
		 * Create our keyboard. We will need it to check for the ESC key
		 */
		keyboard = new Keyboard();

		/**
		 * Set our game state and level
		 */
		setGameState( GameState.Starting );
		setLevel( Level.One );

		/**
		 * Create the background music
		 */
		if( bgMusic == null )
		{
			try
			{
				bgMusic = new AudioSample( this, "audio/MainBGMusic.wav" );
				bgMusic.setVolume( ( float ) 6.5 );
				bgMusic.stop();
			}
			catch( IOException e )
			{
				e.printStackTrace();
			}
			catch( UnsupportedAudioFileException e )
			{
				e.printStackTrace();
			}
			catch( LineUnavailableException e )
			{
				e.printStackTrace();
			}
		}

		/**
		 * Create the boss music
		 */
		if( bossMusic == null )
		{
			try
			{
				bossMusic = new AudioSample( this, "audio/BossMusic.wav" );
				bossMusic.setVolume( ( float ) 6.5 );
				bossMusic.stop();
			}
			catch( IOException e )
			{
				e.printStackTrace();
			}
			catch( UnsupportedAudioFileException e )
			{
				e.printStackTrace();
			}
			catch( LineUnavailableException e )
			{
				e.printStackTrace();
			}
		}

		/**
		 * Create the playing field
		 */
		try
		{
			playingField = new TilePlayingField();
			PlayingField.setCurrentPlayingField( playingField );
		}
		catch( IOException e2 )
		{
			e2.printStackTrace();
		}

		/**
		 * Create Second Level Playing Field
		 */
		try
		{
			playingField2 = new TilePlayingField2();
		}
		catch( IOException e )
		{
			e.printStackTrace();
		}

		/**
		 * Create our list of sprites
		 */
		spriteList = new ArrayList<Sprite>();
		startSprite = new StartSprite();
		spriteList.add( startSprite );
		
		player = new MajorPaine( 10, 390 );
		scoreSprite = new ScoreSprite();
		lifeSprite = new LifeSprite( "Life", "Dead", "Alive", 16, 32, 256, 16,
				.25f, .375f, .375f, 256 );
		nextLevel = new NextLevelScreen();
		roger = new Roger();

		/**
		 * Create some variables for animations and enemy triggers
		 */
		enemyCount = 0;
		numberOfEnemys = 1;
		droneCount = 0;
		nextDroneTime = System.currentTimeMillis();
		lfNextEnemyTime = System.currentTimeMillis();
		bgNextEnemytime = System.currentTimeMillis();

		bfFlag = false;
		bf1 = false;
		bf2 = false;
		bf3 = false;
		bf4 = false;
		lfFlag = false;
		lf1 = false;
		lf2 = false;
		lf3 = false;

		/**
		 * create the bosses and give them triggers
		 */
		bossLaunch = playingField.getBounds().getMaxX() - 1024;
	}

	@Override
	public void draw()
	{
		/**
		 * Swap in the graphics we drew on
		 */
		GameDisplay.update();
	}

	@Override
	public void drawFront()
	{
		/**
		 * Swap in the graphics we drew on
		 */
		GameDisplay.update();
	}

	@Override
	public void render()
	{
		/**
		 * If it's level one, get level one playing field
		 */
		if( getLevel() == Level.One )
		{
			/**
			 * Get the current graphics
			 */
			Graphics2D offscreenGraphics = ( Graphics2D ) GameDisplay
					.getContext();
			Graphics2D offscreenForeground = ( Graphics2D ) GameDisplay
					.getContext();

			/**
			 * Draw the background
			 */
			playingField.draw( offscreenGraphics );

			/**
			 * Draw the Sprite objects
			 */
			for( Sprite spriteObj : spriteList )
			{
				spriteObj.draw( offscreenGraphics );
			}

			playingField.drawFront( offscreenForeground );
		}

		/**
		 * If it's level two, get level two playing field
		 */
		if( getLevel() == Level.Two )
		{
			/**
			 * Get the current graphics
			 */
			Graphics2D offscreenGraphics = ( Graphics2D ) GameDisplay
					.getContext();
			Graphics2D offscreenForeground = ( Graphics2D ) GameDisplay
					.getContext();

			/**
			 * Draw the background
			 */
			playingField2.draw( offscreenGraphics );

			/**
			 * Draw the Sprite objects
			 */
			for( Sprite spriteObj : spriteList )
			{
				spriteObj.draw( offscreenGraphics );
			}

			if( getGameState() == GameState.Running )
			{
				playingField2.drawFront( offscreenForeground );
			}
		}

	}

	@Override
	public void update()
	{
		/**
		 * Set the keyboard controls for starting, pausing, restarting and quitting the game
		 */
		if( keyboard.isStateChanged() )
		{
			KeyEvent ke = keyboard.getLastKeyboardEvent();
			if( ke.getID() == KeyEvent.KEY_RELEASED )
			{
				switch( ke.getKeyCode() )
				{
					case KeyEvent.VK_ESCAPE:
						/**
						 * Exit the application
						 */
						bgMusic.close();
						startSprite.stopMusic();
						GameEngine.stop();
						System.exit( 0 );
						break;

					case KeyEvent.VK_S:
						if( getGameState() == GameState.Starting )
						{
							GameEventDispatcher.dispatchEvent( new GameEvent(
									this, GameEventType.Start, null ) );
						}
						if( getGameState() == GameState.Level2 )
						{
							new GameEvent( this, GameEventType.Start, null );
						}
						break;

					case KeyEvent.VK_R:
						GameEventDispatcher.dispatchEvent( new GameEvent( this,
								GameEventType.Restart, null ) );
						break;

					case KeyEvent.VK_P:
						if( getGameState() == GameState.Running )
						{
							GameEventDispatcher.dispatchEvent( new GameEvent(
									this, GameEventType.Pause, null ) );
						}
						if( getGameState() == GameState.Paused )
						{
							GameEventDispatcher.dispatchEvent( new GameEvent(
									this, GameEventType.UnPause, null ) );
						}
						break;
				}
			}
		}

		/**
		 * If the game is done starting and not paused, run it
		 */
		if( getGameState() != GameState.Paused
				|| getGameState() != GameState.Starting )
		{

			double playerPos = MajorPaine.getxPos();
			Rectangle bounds = GameDisplay.getBounds();

			if( getGameState() != GameState.Paused )
			{
				/**
				 * Update the Sprite objects
				 */
				synchronized( spriteList )
				{
					for( Sprite spriteObj : spriteList )
					{
						spriteObj.update();
					}
				}

				/**
				 * Level 1
				 */
				if( getLevel() == Level.One )
				{
					/**
					 * update the playing field
					 */
					playingField.update( MajorPaine.getxPos(),
							MajorPaine.getyPos() );

					/**
					 * create the bosses and give them triggers
					 */
					bossLaunch = playingField.getBounds().getMaxX() - 1024;

					/**
					 * Let's launch some big Flyer enemies
					 */
					if( playerPos >= 1098 && playerPos <= 1101 )
					{
						bfFlag = true;
						bf1 = true;
					}
					else if( playerPos >= 2756 && playerPos <= 2759 )
					{
						bfFlag = true;
						bf2 = true;
					}
					else if( playerPos >= 4032 && playerPos <= 4035 )
					{
						bfFlag = true;
						bf3 = true;
					}
					else if( playerPos >= 5120 && playerPos <= 5123 )
					{
						bfFlag = true;
						bf4 = true;
					}

					if( bgNextEnemytime <= System.currentTimeMillis()
							&& bfFlag == true )
					{
						if( bf1 )
						{
							BigFlyer bf1 = new BigFlyer( 1722,
									bounds.getHeight() + 70 );
							synchronized( spriteList )
							{
								spriteList.add( bf1 );
							}
						}
						else if( bf2 )
						{
							BigFlyer bf2 = new BigFlyer( 3500,
									bounds.getHeight() + 70 );
							synchronized( spriteList )
							{
								spriteList.add( bf2 );
							}
						}
						else if( bf3 )
						{
							BigFlyer bf3 = new BigFlyer( 4488,
									bounds.getHeight() + 70 );
							synchronized( spriteList )
							{
								spriteList.add( bf3 );
							}
						}
						else if( bf4 )
						{
							BigFlyer bf4 = new BigFlyer( 5880,
									bounds.getHeight() + 70 );
							synchronized( spriteList )
							{
								spriteList.add( bf4 );
							}
						}

						bgNextEnemytime = System.currentTimeMillis() + 2000;
						enemyCount++;
						if( enemyCount >= numberOfEnemys )
						{
							enemyCount = 0;
							bfFlag = false;
							bf1 = false;
							bf2 = false;
							bf3 = false;
							bf4 = false;
						}
					}

					/**
					 * Let's launch some lil Flyer enemies
					 */
					if( playerPos >= 1834 && playerPos <= 1837 )
					{
						lfFlag = true;
						lf1 = true;
					}
					else if( playerPos >= 4598 && playerPos <= 4601 )
					{
						lfFlag = true;
						lf2 = true;
					}
					else if( playerPos >= 6076 && playerPos <= 6079 )
					{
						lfFlag = true;
						lf3 = true;
					}

					if( lfNextEnemyTime <= System.currentTimeMillis()
							&& lfFlag == true )
					{
						if( lf1 )
						{
							LilFlyer lf1 = new LilFlyer( 1834 + 415,
									bounds.getHeight() + 70 );
							synchronized( spriteList )
							{
								spriteList.add( lf1 );
							}
						}
						else if( lf2 )
						{
							LilFlyer lf2 = new LilFlyer( 4598 + 836, -20 );
							synchronized( spriteList )
							{
								spriteList.add( lf2 );
							}
						}
						else if( lf3 )
						{
							LilFlyer lf3 = new LilFlyer( 6076 + 936, 300 );
							synchronized( spriteList )
							{
								spriteList.add( lf3 );
							}
						}

						lfFlag = false;
						lf1 = false;
						lf2 = false;
						lf3 = false;
						lfNextEnemyTime = System.currentTimeMillis() + 2000;
					}

					/**
					 * Launch a little preview to the next level enemies... hehe
					 */
					if( playerPos >= 6580 && playerPos <= 6583 )
					{
						synchronized( spriteList )
						{
							spriteList.add( new BigDrone( 7600, 268 ) );
						}
					}

					/**
					 * If we reach the end, launch the Energy Boss Sprite
					 */
					if( playerPos >= bossLaunch && gameState != GameState.End
							&& gameState != GameState.Level2
							&& gameState != GameState.Finish
							&& gameState != GameState.Stopped )
					{
						bgMusic.stop();
						bossMusic.loop( AudioSample.LOOP_CONTINUOUSLY );
						bossMusic.play();
						synchronized( spriteList )
						{
							energyBoss = new EnergyBoss( playingField
									.getBounds().getWidth() - 138, 0 );
							spriteList.add( energyBoss );
							setGameState( GameState.End );
						}
					}

					if( getGameState() == GameState.Finish
							&& !spriteList.contains( roger ) )
					{
						synchronized( spriteList )
						{
							spriteList.add( roger );
							bossMusic.close();
						}
					}
				}
			}

			/**
			 * Level 2
			 */
			if( getGameState() == GameState.Running && getLevel() == Level.Two )
			{
				/**
				 * update the playing field
				 */
				playingField2.update( MajorPaine.getxPos(),
						MajorPaine.getyPos() );

				/**
				 * Start launching Big Drones and Telepaths
				 */
				if( playerPos >= 1000 && playerPos <= 1500 )
				{
					synchronized( spriteList )
					{
						if( nextDroneTime <= System.currentTimeMillis()
								&& droneCount <= MAX_DRONES )
						{
							nextDroneTime = System.currentTimeMillis() + 2500;
							spriteList.add( new BigDrone( 2028, 320 ) );
							droneCount++;
							droneCount %= MAX_DRONES;
						}
					}
				}
				if( playerPos >= 2400 && playerPos <= 2850 )
				{
					synchronized( spriteList )
					{
						if( nextDroneTime <= System.currentTimeMillis()
								&& droneCount <= MAX_DRONES )
						{
							nextDroneTime = System.currentTimeMillis() + 3000;
							spriteList.add( new BigDrone( 3028, 272 ) );
							if( !spriteList.contains( new Telepath( 3390, 64 ) ) )
							{
								spriteList.add( new Telepath( 3390, 64 ) );
							}
							droneCount++;
							droneCount %= MAX_DRONES;
						}
					}
				}
				if( playerPos >= 2255 && playerPos <= 2900 )
				{
					synchronized( spriteList )
					{
						if( nextDroneTime <= System.currentTimeMillis()
								&& droneCount <= MAX_DRONES )
						{
							nextDroneTime = System.currentTimeMillis() + 3000;
							spriteList.add( new BigDrone( 3037, 200 ) );
							droneCount++;
							droneCount %= MAX_DRONES;
						}
					}
				}
				if( playerPos >= 3700 && playerPos <= 4550 )
				{
					synchronized( spriteList )
					{
						if( nextDroneTime <= System.currentTimeMillis()
								&& droneCount <= MAX_DRONES )
						{
							nextDroneTime = System.currentTimeMillis() + 3000;
							spriteList.add( new BigDrone( 5053, 64 ) );
							droneCount++;
							if( !spriteList
									.contains( new Telepath( 5245, 128 ) ) )
							{
								spriteList.add( new Telepath( 5245, 128 ) );
							}
						}
						if( droneCount >= MAX_DRONES )
						{
							droneCount = 0;
						}
					}
				}
				if( playerPos >= 6112 && playerPos <= 6495 )
				{
					synchronized( spriteList )
					{
						if( nextDroneTime <= System.currentTimeMillis()
								&& droneCount <= MAX_DRONES )
						{
							nextDroneTime = System.currentTimeMillis() + 3000;
							spriteList.add( new BigDrone( 7090, 200 ) );
							droneCount++;
							droneCount %= MAX_DRONES;
						}
					}
				}
				if( playerPos >= 7166 && playerPos <= 7775 )
				{
					synchronized( spriteList )
					{
						if( nextDroneTime <= System.currentTimeMillis()
								&& droneCount <= MAX_DRONES )
						{
							nextDroneTime = System.currentTimeMillis() + 5000;
							spriteList.add( new BigDrone( 8575, 200 ) );
							droneCount++;
							droneCount %= MAX_DRONES;
							if( !spriteList.contains( new Telepath( 9025, 64 ) ) )
							{
								spriteList.add( new Telepath( 9025, 64 ) );
							}
						}
					}
				}
				if( playerPos >= 10000 && playerPos <= 10275 )
				{
					synchronized( spriteList )
					{
						if( nextDroneTime <= System.currentTimeMillis()
								&& droneCount <= MAX_DRONES )
						{
							nextDroneTime = System.currentTimeMillis() + 4000;
							spriteList.add( new BigDrone( 10525, 200 ) );
							droneCount++;
							droneCount %= MAX_DRONES;
							if( !spriteList
									.contains( new Telepath( 10752, 64 ) ) )
							{
								spriteList.add( new Telepath( 10752, 64 ) );
							}
							bossMusic.play();
						}
					}
				}

				/**
				 * If the player reaches the end, launch Sarah
				 */
				if( playerPos >= 11000 && playerPos <= 11003 )
				{
					synchronized( spriteList )
					{
						if( !spriteList.contains( new Telepath( 10752, 64 ) ) )
						{
							spriteList.add( new Sarah() );
							bossMusic.close();
						}
					}
				}
			}
		}
	}

	@Override
	public void collisions()
	{
		if( getGameState() != GameState.Stopped
				|| getGameState() != GameState.Starting )
		{
			/**
			 * check for collisions
			 */
			synchronized( spriteList )
			{
				for( Sprite spriteObj : spriteList )
				{
					for( Sprite otherObj : spriteList )
					{
						if( !spriteObj.equals( otherObj ) )
							spriteObj.checkCollision( otherObj );
					}
				}
			}
		}
	}

	@Override
	public void manageGameEvent( GameEvent ge )
	{
		switch( ge.getType() )
		{
			case NextLevel:
				synchronized( spriteList )
				{
					spriteList.clear();
					spriteList.add( nextLevel );

					MajorPaine.resetHits();
					SpritePool.reset();
					SpritePool.reset();
					setLevel( Level.Two );
					setGameState( GameState.Starting );
					PlayingField.setCurrentPlayingField( playingField2 );
				}
				break;

			case AddFirst:
				synchronized( spriteList )
				{
					spriteList.add( ( Sprite ) ge.getAttachment() );
				}
				break;

			case AddLast:
				synchronized( spriteList )
				{
					spriteList.add( spriteList.size() - 1,
							( Sprite ) ge.getAttachment() );
				}
				break;

			case Remove:
				synchronized( spriteList )
				{
					Sprite sprite = ( Sprite ) ge.getAttachment();
					spriteList.remove( sprite );
					if( sprite instanceof SpritePoolObject )
					{
						( ( SpritePoolObject ) sprite ).checkIn();
					}
				}
				break;

			case Start:
				if( getGameState() == GameState.Starting
						&& getLevel() == Level.Two )
				{
					bgMusic.loop( AudioSample.LOOP_CONTINUOUSLY );
					bgMusic.play();
					synchronized( spriteList )
					{
						startSprite.stopMusic();
						enemyCount = 0;
						spriteList.clear();
						MajorPaine.setxPos( 10 );
						MajorPaine.setyPos( 390 );
						spriteList.add( player );
						lifeSprite.reset();
						spriteList.add( lifeSprite );
						spriteList.add( scoreSprite );
						setGameState( GameState.Running );
					}
				}
				else if( getGameState() == GameState.Starting )
				{
					bgMusic.loop( AudioSample.LOOP_CONTINUOUSLY );
					bgMusic.play();
					synchronized( spriteList )
					{
						startSprite.stopMusic();
						spriteList.clear();
						spriteList.add( lifeSprite );
						spriteList.add( player );
						spriteList.add( scoreSprite );
						setGameState( GameState.Running );
					}
				}
				break;

			case Restart:
				synchronized( spriteList )
				{
					bgMusic.loop( AudioSample.LOOP_CONTINUOUSLY );
					bgMusic.play();

					if( getLevel() == Level.One )
					{
						PlayingField.setCurrentPlayingField( playingField );
					}
					else if( getLevel() == Level.Two )
					{
						PlayingField.setCurrentPlayingField( playingField2 );
					}
					enemyCount = 0;
					scoreSprite.reset();
					spriteList.clear();
					player = new MajorPaine( 10, 390 );
					spriteList.add( player );
					lifeSprite.reset();
					spriteList.add( lifeSprite );
					spriteList.add( scoreSprite );
					setGameState( GameState.Running );
				}
				break;

			case Pause:
				synchronized( spriteList )
				{
					setGameState( GameState.Paused );
					bgMusic.stop();
					// spriteList.add( pauseScreen );
				}
				break;

			case UnPause:
				synchronized( spriteList )
				{
					setGameState( GameState.Running );
					bgMusic.start();
				}
				break;

			case Death:
				synchronized( spriteList )
				{
					bgMusic.stop();
					bossMusic.stop();
					spriteList.clear();
					spriteList.add( lifeSprite );
					DeathSprite deathSprite = new DeathSprite(
							MajorPaine.getxPos(), MajorPaine.getyPos() );
					spriteList.add( deathSprite );
					spriteList.add( scoreSprite );
					setGameState( GameState.End );
				}
				break;

			case Explode:
				synchronized( spriteList )
				{
					Sprite sprite = ( Sprite ) ge.getAttachment();
					if( sprite instanceof BigFlyer )
					{
						BFexplode bfExplode = new BFexplode();
						bfExplode.setxPos( ( ( BigFlyer ) sprite ).getxPos() );
						bfExplode.setyPos( ( ( BigFlyer ) sprite ).getyPos() );
						spriteList.add( bfExplode );
						bfExplode.play();
					}
					if( sprite instanceof LilFlyer )
					{
						LFexplode lfExplode = new LFexplode();
						lfExplode.setxPos( ( ( LilFlyer ) sprite ).getxPos() );
						lfExplode.setyPos( ( ( LilFlyer ) sprite ).getyPos() );
						spriteList.add( lfExplode );
						lfExplode.play();
					}
					if( sprite instanceof BigDrone )
					{
						LFexplode lfExplode = new LFexplode();
						lfExplode.setxPos( ( ( BigDrone ) sprite ).getxPos() );
						lfExplode.setyPos( ( ( BigDrone ) sprite ).getyPos() );
						spriteList.add( lfExplode );
						lfExplode.play();
					}
					if( sprite instanceof Telepath )
					{
						LFexplode lfExplode = new LFexplode();
						lfExplode.setxPos( ( ( Telepath ) sprite ).getxPos() );
						lfExplode.setyPos( ( ( Telepath ) sprite ).getyPos() );
						spriteList.add( lfExplode );
						lfExplode.play();
					}
					spriteList.remove( sprite );
				}
				break;

			case End:
				synchronized( spriteList )
				{
					bgMusic.stop();
					spriteList.add( new EndGameSprite() );
					setGameState( GameState.Stopped );
				}
				break;

			case Win:
				synchronized( spriteList )
				{
					bgMusic.close();
					bossMusic.close();
					Sprite sprite = ( Sprite ) ge.getAttachment();
					spriteList.remove( sprite );
					spriteList.clear();
					YouWinSprite youWin = new YouWinSprite();
					spriteList.add( youWin );
					spriteList.add( scoreSprite );
					setGameState( GameState.Stopped );
				}
				break;

			case Score:
				int score = ( ( Integer ) ge.getAttachment() ).intValue();
				scoreSprite.add( score );
				break;

			case Hit:
				int life = ( ( Integer ) ge.getAttachment() ).intValue();
				lifeSprite.hit( life );
				break;

			default:
				break;
		}
	}

	public static GameState getGameState()
	{
		return gameState;
	}

	public static void setGameState( GameState gameState )
	{
		LoneSoldier.gameState = gameState;
	}

	public static Level getLevel()
	{
		return level;
	}

	public static void setLevel( Level level )
	{
		LoneSoldier.level = level;
	}
}
