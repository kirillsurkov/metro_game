#version 130

precision highp float;

uniform float u_delta;

in float a_lifetime;
out float o_lifetime;

void main() {
	o_lifetime = a_lifetime - u_delta;
	gl_Position = vec4(0);
}
