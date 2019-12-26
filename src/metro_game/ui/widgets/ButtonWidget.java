package metro_game.ui.widgets;

import metro_game.Context;
import metro_game.render.primitives.ColorPrimitive;
import metro_game.render.primitives.RectPrimitive;
import metro_game.render.primitives.ShaderPrimitive;
import metro_game.render.primitives.TextPrimitive;
import metro_game.render.primitives.ShaderPrimitive.ShaderType;
import metro_game.render.primitives.TextPrimitive.AlignmentX;
import metro_game.render.primitives.TextPrimitive.AlignmentY;

public class ButtonWidget extends Widget {
	private ColorPrimitive m_color;
	
	public ButtonWidget(Context context, String text, float x, float y, float width, float height) {
		super(context, x, y, width, height);
		m_color = new ColorPrimitive(1.0f, 0.0f, 0.0f, 1.0f);
		addPrimitive(new ShaderPrimitive(ShaderType.DEFAULT_GAME));
		addPrimitive(m_color);
		addPrimitive(new RectPrimitive(0, 0, width, height, 0.0f));
		addPrimitive(new ShaderPrimitive(ShaderType.FONT));
		addPrimitive(new ColorPrimitive(1.0f, 1.0f, 1.0f, 1.0f));
		addPrimitive(new TextPrimitive(text, true, false, 42, width / 2.0f, height / 2.0f, 0.0f, AlignmentX.CENTER, AlignmentY.CENTER));
	}
	
	@Override
	public void onHover(boolean onWidget) {
		if (onWidget) {
			if (isMouseDown()) {
				m_color.set(0.5f, 0.0f, 0.0f, 1.0f);
			} else {
				m_color.set(1.0f, 0.25f, 0.25f, 1.0f);
			}
		} else {
			m_color.set(1.0f, 0.0f, 0.0f, 1.0f);
		}
	}
}
