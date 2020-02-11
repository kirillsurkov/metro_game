#version 130

precision highp float;

uniform vec4 u_color;

out vec4 outColor;
out vec4 outGlow;

void main() {
	outColor = u_color;
	outGlow = vec4(0.0);
}
