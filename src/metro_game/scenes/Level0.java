package metro_game.scenes;

import metro_game.Context;
import metro_game.game.entities.DummyBoxEntity;

public class Level0 extends LevelBase {
	public Level0(Context context) {
		super(context);
	}
	
	@Override
	public void init() {
		addGameEntity(new DummyBoxEntity(0, 0));
	}
}
