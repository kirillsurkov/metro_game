package metro_game.game.scenes;

import metro_game.Context;
import metro_game.game.entities.EnemyEntity;

public class Level0 extends LevelBase {
	public Level0(Context context) {
		super(context, 8.0f);
	}
	
	@Override
	protected void createCrowd() {
		EnemyEntity enemy = addGameEntity(new EnemyEntity(m_context, -1.6f, -3.0f));
		enemy.addWaypoint(1.6f, -3.0f);
	}
}
