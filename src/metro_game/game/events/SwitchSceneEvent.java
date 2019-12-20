package metro_game.game.events;

import metro_game.game.scenes.Scene;

public class SwitchSceneEvent extends GameEvent {
	private Scene m_scene;
	
	public SwitchSceneEvent(Scene scene) {
		super(Type.SWITCH_SCENE);
		m_scene = scene;
	}
	
	public Scene getScene() {
		return m_scene;
	}
}
