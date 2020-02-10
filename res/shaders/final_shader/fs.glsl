#version 130
#extension GL_ARB_texture_multisample: enable

precision highp float;

uniform vec4 u_color;
uniform sampler2D u_colorTexture;

in vec2 v_uv;

out vec4 outColor;

void main() {
	//outColor = texelFetch(u_colorTexture, ivec2(v_uv), 0);
	outColor = texture2D(u_colorTexture, v_uv);
}
