package metro_game.game;

import java.util.ArrayList;
import java.util.List;

import org.joml.Vector2f;

public class Route {
	public class Waypoint {
		private Vector2f m_position;
		private float m_speed;
		
		public Waypoint(float x, float y, float speed) {
			m_position = new Vector2f(x, y);
			m_speed = speed;
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
		
		public void setSpeed(float speed) {
			m_speed = speed;
		}
		
		public float getSpeed() {
			return m_speed;
		}
	}
	
	public enum Mode {
		NOLOOP,
		LOOP_START_END,
		LOOP_FORWARD_BACKWARD
	}

	private int m_currentWaypoint;
	private Mode m_mode;
	private boolean m_forward;
	private List<Waypoint> m_waypoints;
	
	public Route(float x, float y, float speed, Mode mode) {
		m_currentWaypoint = 0;
		m_mode = mode;
		m_forward = true;
		m_waypoints = new ArrayList<Waypoint>();
		addWaypoint(x, y, speed);
	}
	
	private Waypoint getTarget() {
		return m_waypoints.get(m_currentWaypoint);
	}
	
	public void addWaypoint(float x, float y, float speed) {
		m_waypoints.add(new Waypoint(x, y, speed));
	}
	
	public void update(float srcX, float srcY) {
		if (getDirection(srcX, srcY).length() == 0) {
			switch (m_mode) {
			case NOLOOP: {
				m_currentWaypoint = Math.min(m_currentWaypoint + 1, m_waypoints.size() - 1);
				break;
			}
			case LOOP_START_END: {
				m_currentWaypoint = (m_currentWaypoint + 1) % m_waypoints.size();
				break;
			}
			case LOOP_FORWARD_BACKWARD: {
				if (m_currentWaypoint <= 0) {
					m_forward = true;
				}
				if (m_currentWaypoint >= m_waypoints.size() - 1) {
					m_forward = false;
				}
				m_currentWaypoint = Math.max(Math.min(m_currentWaypoint + (m_forward ? 1 : -1), m_waypoints.size() - 1), 0);
				break;
			}
			}
		}
	}
	
	public Vector2f getDirection(float srcX, float srcY) {
		Vector2f res = new Vector2f(0.0f, 0.0f);
		Vector2f dir = new Vector2f(getTarget().getPositionX() - srcX, getTarget().getPositionY() - srcY);
		float dist = dir.length();
		if (dist > 0.1f) {
			res.x = dir.x / dist;
			res.y = dir.y / dist;
		}
		return res;
	}
	
	public float getSpeed() {
		return getTarget().getSpeed();
	}
}
