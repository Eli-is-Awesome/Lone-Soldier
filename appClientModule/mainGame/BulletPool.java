package mainGame;

import libs.SpritePool;
import libs.SpritePoolObject;

public class BulletPool extends SpritePool
{

	@Override
	protected SpritePoolObject create()
	{
		return new PistolBullet();
	}

}
