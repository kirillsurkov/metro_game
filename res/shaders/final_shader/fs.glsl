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
		color = vec4(smoothstep(0.75 - 1.0 / 2.0, 0.75 + 1.0 / 2.0, color.a));
	}
	outColor = color;
}
