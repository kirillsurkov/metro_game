#version 130

uniform vec4 u_color;
uniform sampler2D u_texture;

in highp vec2 v_texCoords;

out highp vec4 outColor;

void main() {
	outColor = u_color * texture2D(u_texture, v_texCoords).r;
}
