#version 130

precision highp float;

uniform vec4 u_color;

in float v_x;

out vec4 outColor;
out vec4 outGlow;

void main() {
	outColor = vec4(0);//v_x * u_color;
	outGlow = v_x * u_color;
}
