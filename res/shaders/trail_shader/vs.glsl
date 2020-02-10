#version 130

uniform mat4 u_mvp;
uniform float u_count;

in vec2 a_position;
in float a_number;

out float v_x;

void main() {
	v_x = a_number / u_count;
	gl_Position = u_mvp * vec4(a_position, 0.0, 1.0);
}
