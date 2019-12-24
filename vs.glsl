#version 130

uniform mat4 u_mvp;

in vec2 a_position;
out vec2 v_texCoords;

void main()
{
	v_texCoords = a_position;
	gl_Position = u_mvp * vec4(a_position, 0.0, 1.0);
}