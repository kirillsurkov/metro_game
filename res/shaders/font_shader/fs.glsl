#version 130

precision highp float;

uniform vec4 u_color;
uniform sampler2D u_texture;

in vec2 v_texCoords;

out vec4 outColor;

void main() {
	float alpha = texture2D(u_texture, v_texCoords).r;
	outColor = vec4(u_color.rgb, u_color.a * alpha);
}
