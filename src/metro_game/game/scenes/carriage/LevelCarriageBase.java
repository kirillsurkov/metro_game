package metro_game.game.scenes.carriage;

import metro_game.Context;
import metro_game.game.entities.ChairEntity;
import metro_game.game.entities.DoorEntity;
import metro_game.game.entities.TimerSensorEntity;
import metro_game.game.entities.WallEntity;
import metro_game.game.scenes.LevelBase;

public class LevelCarriageBase extends LevelBase {
	private enum Side {
		LEFT,
		RIGHT
	}
	
	private float m_carriageWidth;
	
	public LevelCarriageBase(Context context, float carriageWidth) {
		super(context);
		m_carriageWidth = carriageWidth;
	}
	
	private void addChairs(Side side, float y) {
		float x = (m_carriageWidth * 0.5f - 0.9f) * (side == Side.LEFT ? -1 : 1);
		addGameEntity(new ChairEntity(m_context, x, y + 1.6f));
		addGameEntity(new ChairEntity(m_context, x, y));
		addGameEntity(new ChairEntity(m_context, x, y - 1.6f));
	}
	
	private void createDoorSensor(DoorEntity door, float sensorWidth, float doorWidth, float x, float y, Side side) {
		float sensorX = x + (sensorWidth * 0.5f + 0.15f) * (side == Side.LEFT ? -1 : 1);
		addGameEntity(new TimerSensorEntity(m_context, sensorX, y, sensorWidth, doorWidth, 1.0f) {
			@Override
			public void onActivated() {
				super.onActivated();
				door.open();
			}
			
			@Override
			public void onDeactivated() {
				super.onDeactivated();
				door.close();
			}
		});
	}
	
	protected void createCarriage() {
		float chairsWidth = 4.7f;
		
		float wallX = m_carriageWidth * 0.5f;
		float wallY = 13.9f;
		
		addGameEntity(new WallEntity(m_context, 0.0f, wallY, m_carriageWidth, 0.0f));
		addGameEntity(new WallEntity(m_context, 0.0f, -wallY, m_carriageWidth, 0.0f));

		addGameEntity(new WallEntity(m_context, wallX, wallY - (chairsWidth + 0.25f) * 0.5f, chairsWidth + 0.25f, 90.0f));
		addGameEntity(new WallEntity(m_context, wallX, 0.0f, chairsWidth * 2.0f + 0.3f, 90.0f));
		addGameEntity(new WallEntity(m_context, wallX, -wallY + (chairsWidth + 0.25f) * 0.5f, chairsWidth + 0.25f, 90.0f));
		
		addGameEntity(new WallEntity(m_context, -wallX, wallY - (chairsWidth + 0.25f) * 0.5f, chairsWidth + 0.25f, 90.0f));
		addGameEntity(new WallEntity(m_context, -wallX, 0.0f, chairsWidth * 2.0f + 0.3f, 90.0f));
		addGameEntity(new WallEntity(m_context, -wallX, -wallY + (chairsWidth + 0.25f) * 0.5f, chairsWidth + 0.25f, 90.0f));
		
		addChairs(Side.LEFT, wallY - 0.15f - chairsWidth * 0.5f);
		addChairs(Side.LEFT, 0.05f + chairsWidth * 0.5f);
		addChairs(Side.LEFT, -0.05f - chairsWidth * 0.5f);
		addChairs(Side.LEFT, -wallY + 0.15f + chairsWidth * 0.5f);
		
		addChairs(Side.RIGHT, wallY - 0.15f - chairsWidth * 0.5f);
		addChairs(Side.RIGHT, 0.05f + chairsWidth * 0.5f);
		addChairs(Side.RIGHT, -0.05f - chairsWidth * 0.5f);
		addChairs(Side.RIGHT, -wallY + 0.15f + chairsWidth * 0.5f);

		float sensorWidth = 0.75f;
		float doorWidth = wallY - 0.4f - chairsWidth * 2.0f;
		float doorY = wallY - 0.25f - chairsWidth - doorWidth * 0.5f;
		
		createDoorSensor(addGameEntity(new DoorEntity(m_context, doorWidth, wallX, doorY)), sensorWidth, doorWidth, wallX, doorY, Side.LEFT);
		createDoorSensor(addGameEntity(new DoorEntity(m_context, doorWidth, wallX, -doorY)), sensorWidth, doorWidth, wallX, -doorY, Side.LEFT);
		createDoorSensor(addGameEntity(new DoorEntity(m_context, doorWidth, -wallX, doorY)), sensorWidth, doorWidth, -wallX, doorY, Side.RIGHT);
		createDoorSensor(addGameEntity(new DoorEntity(m_context, doorWidth, -wallX, -doorY)), sensorWidth, doorWidth, -wallX, -doorY, Side.RIGHT);
	}
	
	protected void createCrowd() {
	}
	
	@Override
	public void init() {
		createCarriage();
		createCrowd();
	}
}
