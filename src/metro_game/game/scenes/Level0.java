package metro_game.game.scenes;

import metro_game.Context;
import metro_game.game.entities.EnemyEntity;

public class Level0 extends LevelBase {
	public Level0(Context context) {
		super(context, 8.0f);
	}
	
	@Override
	protected void createCrowd() {
		for (int y = -5; y <= 5; y++) {
			for (int x = -1; x <= 1; x++) {
				if (x == 0) {
					if (Math.random() <= 0.875f) {
						continue;
					}
				} else {
					if (Math.random() <= 0.75f) {
						continue;
					}
				}
				addGameEntity(new EnemyEntity(m_context, x * 1.6f, y * 2.4f));
			}
		}
	}
}
