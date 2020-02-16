#version 130

precision highp float;

uniform vec4 u_color;

in float v_x;
in float v_uv_x;

out vec4 outColor;
out vec4 outGlow;

const float smoothing = 1.0 / 16.0;
const float colorWidth = 0.3;

void main() {
	float color = smoothstep(colorWidth + smoothing, colorWidth - smoothing, abs(v_uv_x));
	outColor = color * v_x * u_color;
	outGlow = v_x * u_color;
}
