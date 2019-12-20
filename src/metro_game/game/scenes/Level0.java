package metro_game.game.scenes;

import metro_game.Context;
import metro_game.game.entities.DummyBoxEntity;
import metro_game.game.entities.PlayerEntity;

public class Level0 extends LevelBase {
	public Level0(Context context) {
		super(context);
	}
	
	@Override
	public void init() {
		addGameEntity(new PlayerEntity(m_context, 0.0f, -5.0f));
		addGameEntity(new DummyBoxEntity(m_context, true, false, 2.0f, 0.0f, 5.0f, 1.0f, -5.0f));
		addGameEntity(new DummyBoxEntity(m_context, true, false, -2.0f, 3.5f, 5.0f, 1.0f, 5.0f));
		addGameEntity(new DummyBoxEntity(m_context, true, false, 2.0f, 7.0f, 5.0f, 1.0f, -5.0f));
		addGameEntity(new DummyBoxEntity(m_context, false, false, 0.0f, 10.5f, 5.0f, 1.0f, 0.0f));
	}
}
