package metro_game;

import metro_game.Strings.Language;
import metro_game.game.Game;
import metro_game.game.physics.Engine;
import metro_game.game.physics.JBox2dEngine;
import metro_game.game.physics.Physics;
import metro_game.render.Renderer;

public class Main {
	public static void main(String[] args) {
		int width = 800;
		int height = 600;
		Strings strings = new Strings(Language.EN);
		
		Context context = new Context(width, height, strings);
		Engine engine = new JBox2dEngine();
		Physics physics = new Physics(context, engine);
		Game game = new Game(context, physics);
		Window window = new Window(context);
		Renderer renderer = new Renderer(context, game);
		
		long lastFrame = System.nanoTime();
		while (window.isAlive()) {
			long now = System.nanoTime();
			double delta = (now - lastFrame) / 1e9;
			lastFrame = now;
			
			context.getInputEvents().flush();
			context.getGameEvents().flush();
			game.update(delta);
			
			if (game.getScenes().size() == 0) {
				window.close();
				break;
			}
			
			renderer.draw();
			
			window.swapBuffers();
		}
		
		window.destroy();
	}
}
