package metro_game.game.scenes;

import metro_game.Context;
import metro_game.game.entities.ChairEntity;
import metro_game.game.entities.PlayerEntityGolf;
import metro_game.game.entities.SensorEntity;
import metro_game.game.entities.WallEntity;

public class Level0 extends LevelBase {
	private enum Side {
		LEFT,
		RIGHT
	}
	
	public Level0(Context context) {
		super(context);
	}
	
	private void addChairs(Side side, float y) {
		float x = 4.1f * (side == Side.LEFT ? -1 : 1);
		addGameEntity(new ChairEntity(m_context, x, y + 1.6f));
		addGameEntity(new ChairEntity(m_context, x, y));
		addGameEntity(new ChairEntity(m_context, x, y - 1.6f));
	}
	
	private void createCarriage() {
		addGameEntity(new WallEntity(m_context, 5.0f, 0.0f, 27.8f, 90.0f));
		addGameEntity(new WallEntity(m_context, -5.0f, 0.0f, 27.8f, 90.0f));
		addGameEntity(new WallEntity(m_context, 0.0f, 13.9f, 10.0f, 0.0f));
		addGameEntity(new WallEntity(m_context, 0.0f, -13.9f, 10.0f, 0.0f));
		
		addChairs(Side.LEFT, 11.4f);
		addChairs(Side.LEFT, 2.4f);
		addChairs(Side.LEFT, -2.4f);
		addChairs(Side.LEFT, -11.4f);
		
		addChairs(Side.RIGHT, 11.4f);
		addChairs(Side.RIGHT, 2.4f);
		addChairs(Side.RIGHT, -2.4f);
		addChairs(Side.RIGHT, -11.4f);
		
		addGameEntity(new SensorEntity(m_context, 4.46f, 6.9f, 0.75f, 4.0f, 1.0f));
		addGameEntity(new SensorEntity(m_context, 4.46f, -6.9f, 0.75f, 4.0f, 2.0f));
		addGameEntity(new SensorEntity(m_context, -4.46f, 6.9f, 0.75f, 4.0f, 3.0f));
		addGameEntity(new SensorEntity(m_context, -4.46f, -6.9f, 0.75f, 4.0f, 4.0f));
	}
	
	@Override
	public void init() {
		createCarriage();
		addGameEntity(new PlayerEntityGolf(m_context, 0.0f, 0.0f));
	}
}
