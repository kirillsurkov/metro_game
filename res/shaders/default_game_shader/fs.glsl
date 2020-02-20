#version 130

precision highp float;

in vec4 v_color;

out vec4 outColor;
out vec4 outGlow;

void main() {
	outColor = v_color;
	outGlow = v_color;//vec4(0.0);
}
