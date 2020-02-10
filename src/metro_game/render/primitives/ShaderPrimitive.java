package metro_game.render.primitives;

public class ShaderPrimitive extends Primitive {
	public enum ShaderType {
		FINAL,
		DEFAULT_GAME,
		FONT,
		TRAIL
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
