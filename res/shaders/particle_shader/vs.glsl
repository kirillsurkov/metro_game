#version 130

uniform mat4 u_mvp;

in vec2 a_position;
in vec2 a_particle_pos;

void main() {
	gl_Position = u_mvp * vec4(a_position * 0.1 + a_particle_pos, 0.0, 1.0);
}
