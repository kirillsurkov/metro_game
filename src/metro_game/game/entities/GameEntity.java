package metro_game.game.entities;

import java.util.ArrayList;
import java.util.List;

import metro_game.Context;
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
	
	public void update(double delta) {
	}
}
