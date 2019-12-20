package metro_game.game.events;

import metro_game.game.entities.GameEntity;

public class DestroyEntityEvent extends GameEvent {
	private GameEntity m_entity;
	
	public DestroyEntityEvent(GameEntity entity) {
		super(Type.DESTROY_ENTITY);
		m_entity = entity;
	}
	
	public GameEntity getEntity() {
		return m_entity;
	}
}
