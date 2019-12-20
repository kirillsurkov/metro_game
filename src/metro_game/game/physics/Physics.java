package metro_game.game.physics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import metro_game.Context;
import metro_game.game.entities.GameEntity;
import metro_game.game.physics.bodies.Body;
import metro_game.scenes.Scene;

public class Physics {
	public class OwnerBodyPair {
		private GameEntity m_owner;
		private Body m_body;
		
		public OwnerBodyPair(GameEntity owner, Body body) {
			m_owner = owner;
			m_body = body;
		}
		
		public GameEntity getOwner() {
			return m_owner;
		}
		
		public Body getBody() {
			return m_body;
		}
	}
	
	private Engine m_engine;
	private Map<Scene, List<OwnerBodyPair>> m_cache;
	private Scene m_currentScene;
	
	public Physics(Context context, Engine engine) {
		m_engine = engine;
		m_cache = new HashMap<Scene, List<OwnerBodyPair>>();
		m_currentScene = null;
	}
	
	public void switchScene(Scene scene) {
		m_engine.clearWorld();
		m_currentScene = scene;
		if (!m_cache.containsKey(m_currentScene)) {
			m_cache.put(m_currentScene, new ArrayList<OwnerBodyPair>());
		}
		for (OwnerBodyPair pair : getBodies()) {
			m_engine.addBody(pair);
		}
	}
	
	public void addBody(GameEntity owner, Body body) {
		OwnerBodyPair pair = new OwnerBodyPair(owner, body);
		getBodies().add(pair);
		m_engine.addBody(pair);
	}
	
	public void destroyBodies(GameEntity owner) {
		Stack<Integer> toRemove = new Stack<Integer>();
		List<OwnerBodyPair> bodies = getBodies();
		for (int i = 0; i < bodies.size(); i++) {
			OwnerBodyPair pair = bodies.get(i);
			if (pair.getOwner() == owner) {
				m_engine.removeBody(pair);
				toRemove.push(i);
			}
		}
		while (toRemove.size() > 0) {
			bodies.remove((int) toRemove.pop());
		}
	}
	
	public List<OwnerBodyPair> getBodies() {
		if (m_currentScene == null) {
			throw new IllegalStateException("m_currentScene is null");
		}
		return m_cache.get(m_currentScene);
	}
	
	public void update(double delta) {
		m_engine.update(delta);
	}
}
