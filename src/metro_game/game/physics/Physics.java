package metro_game.game.physics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import metro_game.Context;
import metro_game.game.physics.bodies.Body;
import metro_game.scenes.Scene;

public class Physics {
	private Engine m_engine;
	private Map<Scene, List<Body>> m_cache;
	private Scene m_currentScene;
	
	public Physics(Context context, Engine engine) {
		m_engine = engine;
		m_cache = new HashMap<Scene, List<Body>>();
		m_currentScene = null;
	}
	
	public void switchScene(Scene scene) {
		m_engine.clearWorld();
		m_currentScene = scene;
		if (!m_cache.containsKey(m_currentScene)) {
			m_cache.put(m_currentScene, new ArrayList<Body>());
		}
		for (Body body : getBodies()) {
			m_engine.addBody(body);
		}
	}
	
	public void addBody(Body body) {
		getBodies().add(body);
		m_engine.addBody(body);
	}
	
	public List<Body> getBodies() {
		if (m_currentScene == null) {
			throw new IllegalStateException("m_currentScene is null");
		}
		return m_cache.get(m_currentScene);
	}
	
	public void update(double delta) {
		m_engine.update(delta);
	}
}
