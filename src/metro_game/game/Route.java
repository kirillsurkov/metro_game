package metro_game.game;

import java.util.ArrayList;
import java.util.List;

import org.joml.Vector2f;

public class Route {
	public class Waypoint {
		private Vector2f m_position;
		
		public Waypoint(float x, float y) {
			m_position = new Vector2f(x, y);
		}
		
		public void setPosition(float x, float y) {
			m_position.set(x, y);
		}
		
		public float getPositionX() {
			return m_position.x;
		}
		
		public float getPositionY() {
			return m_position.y;
		}
	}

	private int m_currentWaypoint;
	private boolean m_looped;
	private List<Waypoint> m_waypoints;
	
	public Route(float x, float y, boolean looped) {
		m_currentWaypoint = 0;
		m_looped = looped;
		m_waypoints = new ArrayList<Waypoint>();
		addWaypoint(x, y);
	}
	
	private Waypoint getTarget() {
		return m_waypoints.get(m_currentWaypoint);
	}
	
	public void addWaypoint(float x, float y) {
		m_waypoints.add(new Waypoint(x, y));
	}
	
	public void update(float srcX, float srcY) {
		if (getDirection(srcX, srcY).length() == 0) {
			if (m_looped) {
				m_currentWaypoint = (m_currentWaypoint + 1) % m_waypoints.size();
			} else {
				m_currentWaypoint = Math.min(m_currentWaypoint + 1, m_waypoints.size() - 1);
			}
		}
	}
	
	public Vector2f getDirection(float srcX, float srcY) {
		Vector2f res = new Vector2f(0.0f, 0.0f);
		Vector2f dir = new Vector2f(getTarget().getPositionX() - srcX, getTarget().getPositionY() - srcY);
		float dist = dir.length();
		if (dist > 0.01f) {
			res.x = dir.x / dist;
			res.y = dir.y / dist;
		}
		return res;
	}
}
