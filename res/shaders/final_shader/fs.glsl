#version 130
#extension GL_ARB_texture_multisample: enable

precision highp float;

uniform vec4 u_color;
uniform sampler2DMS u_colorTexture;
uniform sampler2DMS u_glowTexture;
uniform int u_samples;

in vec2 v_uv;

out vec4 outColor;

vec4 getColor(in sampler2DMS texture, in vec2 uv) {
	vec4 color = vec4(0);
	for (int i = 0; i < u_samples; i++) {
		color += texelFetch(texture, ivec2(uv), i);
	}
	return color / (1.0 * u_samples);
}

void main() {
	vec4 glow = getColor(u_glowTexture, v_uv);
	outColor = glow + getColor(u_colorTexture, v_uv);
}
