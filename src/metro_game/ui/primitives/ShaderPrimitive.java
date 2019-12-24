package metro_game.ui.primitives;

public class ShaderPrimitive extends Primitive {
	public enum ShaderType {
		DEFAULT_GAME,
		FONT
	}
	
	private ShaderType m_shaderType;
	
	public ShaderPrimitive(ShaderType shaderType) {
		super(Type.SHADER);
		m_shaderType = shaderType;
	}
	
	public ShaderType getShaderType() {
		return m_shaderType;
	}
}
