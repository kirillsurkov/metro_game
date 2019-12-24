#version 130

uniform vec4 u_color;

in highp vec2 v_texCoords;

out highp vec4 outColor;

void main() {
	outColor = u_color;
}
