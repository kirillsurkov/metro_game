package metro_game;

import java.util.ArrayList;
import java.util.List;

public class EventsQueue<T> {
	private List<T> m_events;
	private List<T> m_newEvents;
	
	public EventsQueue() {
		m_events = new ArrayList<T>();
		m_newEvents = new ArrayList<T>();
	}
	
	public void flush() {
		m_events = m_newEvents;
		m_newEvents = new ArrayList<T>();
	}
	
	public void pushEvent(T event) {
		m_newEvents.add(event);
	}
	
	public List<T> getEvents() {
		return m_events;
	}
}
