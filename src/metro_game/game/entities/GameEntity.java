package metro_game.game.entities;

import java.util.ArrayList;
import java.util.List;

import metro_game.Context;
import metro_game.game.events.NewBodyEvent;
import metro_game.game.physics.bodies.Body;
import metro_game.game.shapes.Shape;

public class GameEntity {
	protected Context m_context;
	private boolean m_needRemove;
	private List<Shape> m_shapes;
	
	public GameEntity(Context context) {
		m_context = context;
		m_needRemove = false;
		m_shapes = new ArrayList<Shape>();
	}
	
	public void onCollide(GameEntity gameEntity) {
	}
	
	public void setNeedRemove(boolean needRemove) {
		m_needRemove = needRemove;
	}
	
	public boolean isNeedRemove() {
		return m_needRemove;
	}
	
	public <T extends Shape> T addShape(T shape) {
		m_shapes.add(shape);
		return shape;
	}
	
	public List<Shape> getShapes() {
		return m_shapes;
	}
	
	public <T extends Body> T addBody(T body) {
		m_context.getGameEvents().pushEvent(new NewBodyEvent(this, body));
		return body;
	}
	
	public void update(double delta) {
	}
}
