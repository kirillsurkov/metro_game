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
		for (int x = 0; x < 10; x++) {
			for (int y = 0; y < 10; y++) {
				addGameEntity(new DummyBoxEntity(m_context, true, true, -10.0f + x * 2.0f, y * 2.0f, 1.0f, 1.0f, (float) Math.random() * 360.0f));
			}
		}
	}
}
