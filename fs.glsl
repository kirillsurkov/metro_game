#version 130

uniform vec4 u_color;
uniform bool u_textured;
uniform sampler2D u_texture;

in highp vec2 v_texCoords;

out highp vec4 outColor;

void main()
{
	vec4 res = u_color;
	if (u_textured) {
		res *= texture2D(u_texture, v_texCoords).r;
	}
    outColor = res;
}