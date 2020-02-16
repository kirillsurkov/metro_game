#version 130

uniform mat4 u_mvp;
uniform int u_count;

in vec2 a_position;
in float a_number;

out float v_x;
out float v_uv_x;

void main() {
	v_x = float(int(a_number) / 2) / float(u_count);
	v_uv_x = (int(a_number) % 2) * 2 - 1;
	gl_Position = u_mvp * vec4(a_position, 0.0, 1.0);
}
