package metro_game.scenes;

import metro_game.Context;
import metro_game.game.entities.DummyBoxEntity;

public class Level0 extends LevelBase {
	public Level0(Context context) {
		super(context);
	}
	
	@Override
	public void init() {
		addGameEntity(new DummyBoxEntity(m_context, false, true, 0.0f, -5.0f, 1.0f, 1.0f, 0.0f));
		addGameEntity(new DummyBoxEntity(m_context, false, false, -1.5f, 0.0f, 5.0f, 1.0f, 45.0f));
		addGameEntity(new DummyBoxEntity(m_context, false, false, 2.5f, 3.5f, 5.0f, 1.0f, -45.0f));
		addGameEntity(new DummyBoxEntity(m_context, true, false, -4.25f, 8.5f, 5.0f, 1.0f, 0.0f));
	}
}
