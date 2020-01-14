package metro_game.game.entities;

import java.util.ArrayList;
import java.util.List;

import metro_game.Context;
import metro_game.game.events.NewBodyEvent;
import metro_game.game.physics.bodies.Body;
import metro_game.render.primitives.Primitive;

public class GameEntity {
	protected Context m_context;
	private boolean m_needRemove;
	private List<Primitive> m_primitives;
	
	public GameEntity(Context context) {
		m_context = context;
		m_needRemove = false;
		m_primitives = new ArrayList<Primitive>();
	}
	
	public void onCollideStart(GameEntity gameEntity) {
	}
	
	public void onCollideEnd(GameEntity gameEntity) {
	}
	
	public void setNeedRemove(boolean needRemove) {
		m_needRemove = needRemove;
	}
	
	public boolean isNeedRemove() {
		return m_needRemove;
	}
	
	public <T extends Primitive> T addPrimitive(T primitive) {
		m_primitives.add(primitive);
		return primitive;
	}
	
	public List<Primitive> getPrimitives() {
		return m_primitives;
	}
	
	public <T extends Body> T addBody(T body) {
		m_context.getGameEvents().pushEvent(new NewBodyEvent(this, body));
		return body;
	}
	
	public void update(double delta) {
	}
}
