#version 130
#extension GL_ARB_texture_multisample: enable

precision highp float;

uniform vec4 u_color;
uniform vec2 u_textureSize;
uniform sampler2DMS u_texture;
uniform int u_samples;
uniform bool u_horizontal;

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
	float[] kernel = float[5](0.2270270270, 0.1945945946, 0.1216216216, 0.0540540541, 0.0162162162);
	vec2 step = vec2(u_horizontal, !u_horizontal);
	vec4 color = getColor(u_texture, v_uv) * kernel[0];
	for (int i = 1; i < 5; i++) {
		float w = kernel[i];
		color += getColor(u_texture, v_uv + step * i) * w;
		color += getColor(u_texture, v_uv - step * i) * w;
	}
	outColor = vec4(color.rgb, 1.0);
}
