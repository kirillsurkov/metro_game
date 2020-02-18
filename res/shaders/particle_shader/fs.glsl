#version 130

precision highp float;

in float v_lifetime;

out vec4 outColor;

void main() {
	outColor = v_lifetime * vec4(0.0, 1.0, 1.0, 1.0);
}
