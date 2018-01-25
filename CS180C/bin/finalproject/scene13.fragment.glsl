#version 330 core

in vec4 vertex_Color;
out vec4 fragColor;

uniform float alpha;

void main(){
	fragColor = vertex_Color;
	fragColor.w = alpha;
}