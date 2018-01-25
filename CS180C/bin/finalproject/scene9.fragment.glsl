#version 330 core

uniform bool radialGradientEnabled;

in vec4 vertex_Color;
out vec4 fragColor;

uniform vec2 center;
uniform float radius;
uniform vec4 startColor;
uniform vec4 endColor;

void main(){
	float t;
	
	if (radialGradientEnabled){
		t = sqrt(pow(gl_FragCoord.x - center.x, 2) + pow(gl_FragCoord.y - center.y, 2))/radius;
		if ( t < 0) { t = 0; }
		else if (t > 1) { t = 1;}
		
		fragColor.x = ((1 - t) * startColor.x) + (t * endColor.x);
		fragColor.y = ((1 - t) * startColor.y) + (t * endColor.y);
		fragColor.z = ((1 - t) * startColor.z) + (t * endColor.z);
    	fragColor.w = vertex_Color.w;
	} else {
		fragColor = vertex_Color;
	}
}