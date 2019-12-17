package metro_game;

import metro_game.Strings.Language;

public class Main {
	public static void main(String[] args) {
		int width = 800;
		int height = 600;
		Strings strings = new Strings(Language.EN);
		
		Context context = new Context(width, height, strings);
		Game game = new Game(context);
		Window window = new Window(context);
		new Renderer(context, window, game).run();
		window.destroy();
	}
}
