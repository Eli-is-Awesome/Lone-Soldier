package mainGame;

import libs.SpritePool;
import libs.SpritePoolObject;

public class LaserPool extends SpritePool
{

	@Override
	protected SpritePoolObject create()
	{
		return new Laser();
	}

}
