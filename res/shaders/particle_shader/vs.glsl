#version 130

uniform mat4 u_mvp;

in vec2 a_position;
in float a_particle_lifetime;
in vec2 a_particle_pos;

out float v_lifetime;

void main() {
	v_lifetime = a_particle_lifetime / 0.5f;
	gl_Position = u_mvp * vec4(0.05 * a_position + a_particle_pos, 0.0, 1.0);
}
