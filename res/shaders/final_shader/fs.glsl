#version 130

precision highp float;

uniform vec4 u_color;
uniform sampler2D u_texture;
uniform bool u_sharpen;

in vec2 v_uv;

out vec4 outColor;

void main() {
	vec4 color = texture2D(u_texture, v_uv);
	if (u_sharpen) {
		float a = color.a;
		if (a > 0) {
			color /= a;
		}
		color *= smoothstep(0.1, 0.6, a);
	}
	outColor = color;
}
