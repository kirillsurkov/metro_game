#version 130

out vec2 v_uv;

void main() {
	vec2 pos = vec2(0.0); 
	switch (gl_VertexID) {
		case 0: pos = vec2( 1.0, -1.0); break;
		case 1: pos = vec2( 1.0,  1.0); break;
		case 2: pos = vec2(-1.0, -1.0); break;
		case 3: pos = vec2(-1.0,  1.0); break;
	}
	v_uv = (pos + 1) / 2;
	gl_Position = vec4(pos, 0.0, 1.0);
}
