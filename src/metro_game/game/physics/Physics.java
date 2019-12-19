package metro_game.game.physics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import metro_game.Context;
import metro_game.game.entities.GameEntity;
import metro_game.game.physics.bodies.Body;
import metro_game.scenes.Scene;

public class Physics {
	private Context m_context;
	private Map<Scene, Map<GameEntity, List<Body>>> m_cache;
	private Scene m_currentScene;
	
	public Physics(Context context) {
		m_context = context;
		m_cache = new HashMap<Scene, Map<GameEntity, List<Body>>>();
	}
	
	public void switchScene(Scene scene) {
		m_currentScene = scene;
		if (!m_cache.containsKey(m_currentScene)) {
			m_cache.put(m_currentScene, new HashMap<GameEntity, List<Body>>());
		}
	}
	
	public void addBody(GameEntity owner, Body body) {
		Map<GameEntity, List<Body>> sceneCache = getBodies();
		if (!sceneCache.containsKey(owner)) {
			sceneCache.put(owner, new ArrayList<Body>());
		}
		sceneCache.get(owner).add(body);
	}
	
	public Map<GameEntity, List<Body>> getBodies() {
		if (m_currentScene == null) {
			throw new IllegalStateException("m_currentScene is null");
		}
		return m_cache.get(m_currentScene);
	}
}
