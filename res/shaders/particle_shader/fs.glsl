#version 130

precision highp float;

in vec2 v_pos;
in float v_lifetime;
in vec3 v_particle_color;
in vec2 v_uv;

out vec4 outColor;

void main() {
	float a = smoothstep(0.5 - 0.001, 0.5 + 0.001, 1 - distance(vec2(0), v_pos));
	if (a <= 0) {
		discard;
	}
	outColor = vec4(v_particle_color, 1.0) * a;
}
