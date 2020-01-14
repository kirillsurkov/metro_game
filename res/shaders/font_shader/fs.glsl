#version 130

precision highp float;

uniform sampler2D u_texture;
uniform vec4 u_color;
uniform vec4 u_border_color;
uniform float u_border;
uniform float u_sdf_pixel;
uniform float u_sdf_onedge;

in vec2 v_texCoords;

const float smoothing = 1.0 / 16.0;

out vec4 outColor;

void main() {
	float distance = texture2D(u_texture, v_texCoords).r;
	float alpha = smoothstep(u_sdf_onedge - smoothing, u_sdf_onedge + smoothing, distance);
	vec4 color = u_color;
	if (u_border > 0) {
		float onborder = u_sdf_onedge - u_border * u_sdf_pixel;
		color = mix(u_border_color, u_color, alpha);
		alpha = smoothstep(onborder - smoothing, onborder + smoothing, distance);
	}
	outColor = color * alpha;
}
