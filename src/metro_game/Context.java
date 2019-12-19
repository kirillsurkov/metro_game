package metro_game;

import metro_game.Strings.Language;
import metro_game.game.events.GameEvent;
import metro_game.ui.events.InputEvent;

public class Context {
	private int m_width;
	private int m_height;
	private Strings m_strings;
	private double m_mouseX;
	private double m_mouseY;
	private EventsQueue<InputEvent> m_inputEvents;
	private EventsQueue<GameEvent> m_gameEvents;
	
	public Context(int width, int height, Strings strings) {
		m_width = width;
		m_height = height;
		m_strings = strings;
		m_inputEvents = new EventsQueue<InputEvent>();
		m_gameEvents = new EventsQueue<GameEvent>();
	}
	
	public int getWidth() {
		return m_width;
	}
	
	public int getHeight() {
		return m_height;
	}
	
	public float getAspect() {
		return 1.0f * m_width / m_height;
	}
	
	public void setLanguage(Language language) {
		m_strings.setLanguage(language);
	}
	
	public Language getLanguage() {
		return m_strings.getLanguage();
	}
	
	public String getString(String key) {
		return m_strings.getString(key);
	}
	
	public void setMousePos(double x, double y) {
		m_mouseX = x;
		m_mouseY = y;
	}
	
	public double getMouseX() {
		return m_mouseX;
	}
	
	public double getMouseY() {
		return m_mouseY;
	}
	
	public EventsQueue<InputEvent> getInputEvents() {
		return m_inputEvents;
	}
	
	public EventsQueue<GameEvent> getGameEvents() {
		return m_gameEvents;
	}
}