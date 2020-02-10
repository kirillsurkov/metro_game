#version 130

precision highp float;

uniform vec4 u_color;

in float v_x;

out vec4 outColor;

void main() {
	outColor = v_x * u_color;
}
