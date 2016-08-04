package mainGame;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;

import libs.GameDisplay;
import libs.GameEvent.GameState;
import libs.ImageUtil;
import libs.MapFile;
import libs.PlayingField;
import libs.TileSheet;

public class TilePlayingField extends PlayingField
{
	/**
	 * our background image
	 */
	private Rectangle displayBounds;
	private short[][] fieldMap;
	private short[][] fieldMap2;
	private int gameDisplayXPos;
	private int gameDisplayYPos;
	private int tileHeight;
	private TileSheet mainTileSheet;

	private int tileWidth;
	private boolean disappear = false;
	private boolean flag = false;
	private static BufferedImage hiddenBackground;
	private static BufferedImage layer1;
	private static BufferedImage layer2;
	private static BufferedImage buildings;
	private static BufferedImage foreground;

	public TilePlayingField() throws IOException
	{
		displayBounds = GameDisplay.getBounds();

		layer1 = ImageUtil.loadBufferedImage( this, "images/bg.png" );
		layer2 = ImageUtil.loadBufferedImage( this, "images/background.png" );
		buildings = ImageUtil.loadBufferedImage( this, "images/buildings.png" );
		hiddenBackground = ImageUtil.loadBufferedImage( this,
				"images/hiddenPassageFiller.png" );
		foreground = ImageUtil
				.loadBufferedImage( this, "images/foreground.png" );

		/**
		 * the tiles are 64 X 64
		 */
		tileHeight = 64;
		tileWidth = 64;

		mainTileSheet = new TileSheet( this, "images/LS_LevelTilesheet.png",
				tileWidth, tileHeight );

		short[][] tempMap = MapFile.readMap( this, "images/level_1_main.txt" );
		short[][] tempMap2 = MapFile.readMap( this,
				"images/level_1_fakeTiles.txt" );
		if( tempMap != null )
		{
			fieldMap = tempMap;
		}
		else
		{
			throw new IOException( "Invalid map contents" );
		}
		if( tempMap2 != null )
		{
			fieldMap2 = tempMap2;
		}
		else
		{
			throw new IOException( "Invalid map contents" );
		}
	}

	@Override
	public void draw( Graphics2D g2d )
	{
		int tileIndex;
		int tileIndex2;

		/**
		 * Draw the background image
		 */
		g2d.drawImage( layer1.getSubimage( gameDisplayXPos / 12,
				gameDisplayYPos / 12, ( int ) displayBounds.getWidth(),
				( int ) displayBounds.getHeight() ), 0, 0, null );
		g2d.drawImage( layer2.getSubimage( gameDisplayXPos / 9,
				gameDisplayYPos / 9, ( int ) displayBounds.getWidth(),
				( int ) displayBounds.getHeight() ), 0, 0, null );
		g2d.drawImage( hiddenBackground.getSubimage( gameDisplayXPos,
				gameDisplayYPos, ( int ) displayBounds.getWidth(),
				( int ) displayBounds.getHeight() ), 0, 0, null );
		g2d.drawImage( buildings.getSubimage( gameDisplayXPos, gameDisplayYPos,
				( int ) displayBounds.getWidth(),
				( int ) displayBounds.getHeight() ), 0, 0, null );

		/**
		 * Figure out the tile x, y index of fieldMap
		 */
		int firstTileX = ( gameDisplayXPos / tileWidth );
		int lastTileX = firstTileX + ( displayBounds.width / tileWidth ) + 2;
		if( lastTileX >= fieldMap[0].length )
		{
			lastTileX = fieldMap[0].length - 1;
		}

		int firstTileY = ( gameDisplayYPos / tileHeight );
		int lastTileY = firstTileY + ( displayBounds.height / tileHeight ) + 2;
		if( lastTileY >= fieldMap.length )
		{
			lastTileY = fieldMap.length;
		}

		/**
		 * Draw the map tiles
		 */
		for( int y = firstTileY; y < lastTileY; y++ )
		{
			for( int x = firstTileX; x <= lastTileX; x++ )
			{
				tileIndex = fieldMap[y][x];
				if( tileIndex != 0 )
				{
					g2d.drawImage( mainTileSheet.getTile(
							( tileIndex / mainTileSheet.getNumberColumns() ),
							( tileIndex % mainTileSheet.getNumberColumns() ) ),
							( x * tileWidth ) - gameDisplayXPos,
							( y * tileHeight ) - gameDisplayYPos, tileWidth,
							tileHeight, null );
				}
			}
		}

		/**
		 * Figure out the tile x, y index of fieldMap2
		 */
		int firstTileX2 = ( gameDisplayXPos / tileWidth );
		int lastTileX2 = firstTileX2 + ( displayBounds.width / tileWidth ) + 2;
		if( lastTileX2 >= fieldMap2[0].length )
		{
			lastTileX2 = fieldMap2[0].length - 1;
		}

		int firstTileY2 = ( gameDisplayYPos / tileHeight );
		int lastTileY2 = firstTileY2 + ( displayBounds.height / tileHeight )
				+ 2;
		if( lastTileY2 >= fieldMap2.length )
		{
			lastTileY2 = fieldMap2.length;
		}

		if( disappear != true )
		{
			/**
			 * Draw the map2 tiles
			 */
			for( int y2 = firstTileY2; y2 < lastTileY2; y2++ )
			{
				for( int x2 = firstTileX2; x2 <= lastTileX2; x2++ )
				{
					tileIndex2 = fieldMap2[y2][x2];
					if( tileIndex2 != 0 )
					{
						g2d.drawImage(
								mainTileSheet.getTile(
										( tileIndex2 / mainTileSheet
												.getNumberColumns() ),
										( tileIndex2 % mainTileSheet
												.getNumberColumns() ) ),
								( x2 * tileWidth ) - gameDisplayXPos,
								( y2 * tileHeight ) - gameDisplayYPos,
								tileWidth, tileHeight, null );
					}
				}
			}
		}
	}

	public void drawFront( Graphics2D g )
	{
		/**
		 * Draw the foreground
		 */
		g.drawImage( foreground.getSubimage( gameDisplayXPos, gameDisplayYPos,
				( int ) displayBounds.getWidth(),
				( int ) displayBounds.getHeight() ), 0, 0, null );
	}

	@Override
	public Rectangle getBounds()
	{
		return new Rectangle( 0, 0, fieldMap[0].length * tileWidth,
				fieldMap.length * tileHeight );
	}

	@Override
	public boolean isCollision( double x, double y )
	{
		double x1 = x;
		double x2 = x;
		double y1 = y;
		double y2 = y;

		if( getGridY( y1 ) < 0 || getGridY( y1 ) >= fieldMap.length
				&& getGridY( y2 ) < 0 || getGridY( y2 ) >= fieldMap2.length )
		{
			return false;
		}
		if( getGridX( x1 ) < 0 || getGridX( x1 ) >= fieldMap[0].length
				&& getGridX( x2 ) < 0 || getGridX( x2 ) >= fieldMap2[0].length )
		{
			return false;
		}
		else
		{
			if( MajorPaine.getxPos() >= 2727 )
			{
				flag = true;
			}
			if( LoneSoldier.getGameState() == GameState.Finish )
			{
				flag = false;
			}

			if( flag == true )
			{
				disappear = true;
			}
			if( flag == false )
			{
				disappear = false;
			}
		}

		if( disappear == true )
		{
			return( fieldMap[getGridY( y1 )][getGridX( x1 )] != 0 );
		}
		else
		{
			return( fieldMap[getGridY( y1 )][getGridX( x1 )] != 0 || fieldMap2[getGridY( y2 )][getGridX( x2 )] != 0 );
		}

	}

	@Override
	public Point toGameDisplay( double x, double y )
	{
		return new Point( ( int ) x - gameDisplayXPos, ( int ) y
				- gameDisplayYPos );
	}

	@Override
	public void update( double x, double y )
	{
		gameDisplayXPos = getScreenX( x );
		gameDisplayYPos = getScreenY( y );
	}

	/**
	 * Return the x grid coordinate for the display x coordinate
	 * 
	 * @param x
	 * @return
	 */
	private int getGridX( double x )
	{
		return ( int ) x / tileWidth;
	}

	/**
	 * Return the y grid coordinate for the display y coordinate
	 * 
	 * @param y
	 * @return
	 */
	private int getGridY( double y )
	{
		return ( int ) y / tileHeight;
	}

	/**
	 * Get the game frame's x coordinate on the playing field relative to the x
	 * value
	 * 
	 * @param x
	 * @return int
	 */
	private int getScreenX( double x )
	{
		int offset = ( int ) ( Math.round( x ) - ( displayBounds.getWidth() / 2 ) );

		offset = Math.max( offset, 0 );
		offset = Math
				.min( offset, this.getBounds().width - displayBounds.width );
		return offset;
	}

	/**
	 * Get the game frame's y coordinate on the playing field relative to the y
	 * value
	 * 
	 * @param y
	 * @return int
	 */
	private int getScreenY( double y )
	{
		int offset = ( int ) ( Math.round( y ) - ( displayBounds.getHeight() / 2 ) );

		offset = Math.max( offset, 0 );
		offset = Math.min( offset, this.getBounds().height
				- displayBounds.height );

		return offset;
	}
}
