package metro_game.game.physics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import metro_game.Context;
import metro_game.game.entities.PhysicsEntity;
import metro_game.game.physics.bodies.Body.BodyPhysicsInterface;
import metro_game.game.scenes.Scene;

public class Physics {
	public class OwnerBodyPair {
		private PhysicsEntity m_owner;
		private BodyPhysicsInterface m_body;
		
		public OwnerBodyPair(PhysicsEntity owner, BodyPhysicsInterface body) {
			m_owner = owner;
			m_body = body;
		}
		
		public PhysicsEntity getOwner() {
			return m_owner;
		}
		
		public BodyPhysicsInterface getBody() {
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
	
	public void addBody(PhysicsEntity owner, BodyPhysicsInterface body) {
		OwnerBodyPair pair = new OwnerBodyPair(owner, body);
		getBodies().add(pair);
		m_engine.addBody(pair);
	}
	
	public void destroyBodies(PhysicsEntity owner) {
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
