package metro_game.render.shaders;

import java.io.IOException;

public class DefaultGameShader extends ShaderDraw {
	public DefaultGameShader() throws IOException {
		super(ShaderType.DEFAULT_GAME, "default_game_shader");
	}
}
