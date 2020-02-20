#version 130

uniform mat4 u_mvp;
uniform bool u_uniforms;
uniform vec4 u_color;

in vec2 a_position;
in float a_scale;
in vec2 a_offset;
in vec4 a_color;

out vec4 v_color;

void main() {
	float scale = u_uniforms ? 1 : a_scale;
	vec2 offset = u_uniforms ? vec2(0) : a_offset;
	v_color = u_uniforms ? u_color : a_color;
	gl_Position = u_mvp * vec4(scale * a_position + offset, 0.0, 1.0);
}
