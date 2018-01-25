#version 330 core

in vec4 vertex_Color;
out vec4 fragColor;

uniform vec4 startColor;
uniform vec4 endColor;
uniform vec2 center;
uniform float radius;

void main(){
	
	float t = sqrt(pow(gl_FragCoord.x - center.x, 2) + pow(gl_FragCoord.y - center.y, 2))/radius;
	
	fragColor.x = ((1 - t) * startColor.x) + (t * endColor.x);
	fragColor.y = ((1 - t) * startColor.y) + (t * endColor.y);
	fragColor.z = ((1 - t) * startColor.z) + (t * endColor.z);
    fragColor.w = vertex_Color.w;
}