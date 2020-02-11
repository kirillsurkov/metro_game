#version 130

precision highp float;

uniform vec4 u_color;
uniform sampler2D u_texture;

in vec2 v_uv;

out vec4 outColor;

void main() {
	outColor = texture2D(u_texture, v_uv);
}
