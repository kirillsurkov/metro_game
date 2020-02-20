#version 130

uniform mat4 u_mvp;

in float a_particle_lifetime_orig;
in float a_particle_lifetime;
in vec2 a_particle_pos;
in vec3 a_particle_color;

out vec2 v_pos;
out float v_lifetime;
out vec3 v_particle_color;
out vec2 v_uv;

void main() {
	vec2 pos;
	switch (gl_VertexID) {
		case 0: pos = vec2( 1, -1); break;
		case 1: pos = vec2( 1,  1); break;
		case 2: pos = vec2(-1, -1); break;
		case 3: pos = vec2(-1,  1); break;
	}
	v_pos = pos;
	v_lifetime = clamp(a_particle_lifetime / a_particle_lifetime_orig, 0, 1);
	v_particle_color = a_particle_color;
	v_uv = (pos + 1.0) / 2.0;
	gl_Position = u_mvp * vec4(0.2 * pos * v_lifetime + a_particle_pos, 0.0, 1.0);
}
