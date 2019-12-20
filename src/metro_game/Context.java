package metro_game;

import metro_game.Strings.Language;
import metro_game.game.events.GameEvent;
import metro_game.ui.events.UIEvent;

public class Context {
	private int m_width;
	private int m_height;
	private Strings m_strings;
	private int m_mouseX;
	private int m_mouseY;
	private EventsQueue<UIEvent> m_uiEvents;
	private EventsQueue<GameEvent> m_gameEvents;
	
	public Context(int width, int height, Strings strings) {
		m_width = width;
		m_height = height;
		m_strings = strings;
		m_uiEvents = new EventsQueue<UIEvent>();
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
	
	public void setMousePos(int x, int y) {
		m_mouseX = x;
		m_mouseY = y;
	}
	
	public float getMouseX() {
		return 1.0f * m_mouseX / getWidth();
	}
	
	public float getMouseY() {
		return 1.0f * m_mouseY / getHeight();
	}
	
	public EventsQueue<UIEvent> getUIEvents() {
		return m_uiEvents;
	}
	
	public EventsQueue<GameEvent> getGameEvents() {
		return m_gameEvents;
	}
}