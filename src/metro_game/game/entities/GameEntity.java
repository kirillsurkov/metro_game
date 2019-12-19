package metro_game.game.entities;

import java.util.ArrayList;
import java.util.List;

import metro_game.game.physics.bodies.Body;
import metro_game.game.shapes.Shape;

public class GameEntity {
	private List<Shape> m_shapes;
	private List<Body> m_bodies;
	
	public GameEntity() {
		m_shapes = new ArrayList<Shape>();
		m_bodies = new ArrayList<Body>();
	}
	
	public <T extends Shape> T addShape(T shape) {
		m_shapes.add(shape);
		return shape;
	}
	
	public List<Shape> getShapes() {
		return m_shapes;
	}
	
	public <T extends Body> T addBody(T body) {
		m_bodies.add(body);
		return body;
	}
	
	public List<Body> getBodies() {
		return m_bodies;
	}
	
	public void update(double delta) {
	}
}
