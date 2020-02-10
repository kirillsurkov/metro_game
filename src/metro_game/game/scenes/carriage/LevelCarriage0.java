package metro_game.game.scenes.carriage;

import metro_game.Context;
import metro_game.game.Route;
import metro_game.game.entities.EnemyEntity;
import metro_game.game.entities.PlayerEntityGolf;

public class LevelCarriage0 extends LevelCarriageBase {
	public LevelCarriage0(Context context) {
		super(context, 8.0f);
	}
	
	@Override
	protected void createCrowd() {
		float sp1 = 25.0f;
		//float sp2 = 2.5f;
		
		EnemyEntity enemy = addGameEntity(new EnemyEntity(m_context, Route.Mode.LOOP_FORWARD_BACKWARD, -1.6f, -4.0f, sp1));
		enemy.addWaypoint(0.0f, -4.0f, sp1);
		enemy.addWaypoint(1.6f, -2.4f, sp1);
		enemy.addWaypoint(0.0f, -2.4f, sp1);
		enemy.addWaypoint(-1.6f, -0.8f, sp1);
		enemy.addWaypoint(0.0f, -0.8f, sp1);
		enemy.addWaypoint(1.6f, 0.8f, sp1);
		enemy.addWaypoint(0.0f, 0.8f, sp1);
		enemy.addWaypoint(-1.6f, 2.4f, sp1);
		enemy.addWaypoint(0.0f, 2.4f, sp1);
		enemy.addWaypoint(1.6f, 4.0f, sp1);
		enemy.addWaypoint(-1.6f, 4.0f, sp1);
		
		addGameEntity(new EnemyEntity(m_context, Route.Mode.NOLOOP, 3.2f, -4.0f, 0.0f));
		addGameEntity(new EnemyEntity(m_context, Route.Mode.NOLOOP, 3.2f, -0.8f, 0.0f));
		addGameEntity(new EnemyEntity(m_context, Route.Mode.NOLOOP, 3.2f, 2.4f, 0.0f));
		
		addGameEntity(new EnemyEntity(m_context, Route.Mode.NOLOOP, -3.2f, -2.4f, 0.0f));
		addGameEntity(new EnemyEntity(m_context, Route.Mode.NOLOOP, -3.2f, 0.8f, 0.0f));
		addGameEntity(new EnemyEntity(m_context, Route.Mode.NOLOOP, -3.2f, 4.0f, 0.0f));
		
		addGameEntity(new PlayerEntityGolf(m_context, 0.0f, 6.0f));
	}
}
