package metro_game.game;

import java.util.Stack;

import metro_game.Context;
import metro_game.game.entities.PhysicsEntity;
import metro_game.game.events.CameraEvent;
import metro_game.game.events.DestroyEntityEvent;
import metro_game.game.events.GameEvent;
import metro_game.game.events.NewBodyEvent;
import metro_game.game.events.SwitchSceneEvent;
import metro_game.game.physics.Physics;
import metro_game.game.scenes.MainMenu;
import metro_game.game.scenes.Scene;
import metro_game.ui.events.UIEvent;
import metro_game.ui.events.UIEvent.Type;

public class Game {
	private Context m_context;
	private Physics m_physics;
	private Camera m_camera;
	private Stack<Scene> m_scenes;
	private Scene m_newScene;
	
	public Game(Context context, Physics physics) {
		m_context = context;
		m_physics = physics;
		m_camera = new Camera(0.0f, 0.0f);
		m_scenes = new Stack<Scene>();
		m_newScene = new MainMenu(context);
	}
	
	private void pushScene(Scene scene) {
		if (m_scenes.size() > 0) {
			m_scenes.lastElement().setReady(false);
		}
		m_scenes.push(scene);
		m_physics.switchScene(scene);
		scene.setReady(true);
		scene.init();
	}
	
	private void popScene() {
		m_scenes.pop();
		if (m_scenes.size() > 0) {
			Scene scene = m_scenes.lastElement();
			m_physics.switchScene(scene);
			scene.setReady(true);
		}
	}
	
	public Camera getCamera() {
		return m_camera;
	}
	
	public Stack<Scene> getScenes() {
		return m_scenes;
	}
	
	public void update(double delta) {
		if (m_newScene != null) {
			pushScene(m_newScene);
			m_camera.setPosImmediately(true);
			m_newScene = null;
		}
		
		for (UIEvent event : m_context.getUIEvents().getEvents()) {
			if (event.getType() == Type.BACK) {
				if (!m_scenes.lastElement().onBack()) {
					popScene();
				}
			}
		}

		if (m_scenes.size() > 0) {
			Scene scene = m_scenes.lastElement();
			if (!scene.isPaused()) {
				m_physics.update(delta);
			}
			scene.update(delta);
			if (scene.isNeedClose()) {
				popScene();
			}
		}
		
		m_context.getGameEvents().flush();
		for (GameEvent gameEvent : m_context.getGameEvents().getEvents()) {
			switch (gameEvent.getType()) {
			case SWITCH_SCENE: {
				SwitchSceneEvent event = (SwitchSceneEvent) gameEvent;
				m_newScene = event.getScene();
				break;
			}
			case NEW_BODY: {
				NewBodyEvent event = (NewBodyEvent) gameEvent;
				if (event.getOwner() instanceof PhysicsEntity) {
					m_physics.addBody((PhysicsEntity) event.getOwner(), event.getBody().getPhysicsInterface());
				} else {
					System.out.println("Non-physics entity cannot own bodies");
				}
				break;
			}
			case CAMERA: {
				CameraEvent event = (CameraEvent) gameEvent;
				m_camera.move(event.getPositionX(), event.getPositionY());
				break;
			}
			case DESTROY_ENTITY: {
				DestroyEntityEvent event = (DestroyEntityEvent) gameEvent;
				if (event.getEntity() instanceof PhysicsEntity) {
					m_physics.destroyBodies((PhysicsEntity) event.getEntity());
				}
				break;
			}
			}
		}
		
		m_camera.update(delta);
	}
}