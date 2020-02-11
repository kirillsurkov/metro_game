#version 130

precision highp float;

uniform vec4 u_color;
uniform bool u_glow;

in float v_x;

out vec4 outColor;
out vec4 outGlow;

void main() {
	outColor = (u_glow ? 0 : 1) * v_x * u_color;
	outGlow = (u_glow ? 1 : 0) * v_x * u_color;
}