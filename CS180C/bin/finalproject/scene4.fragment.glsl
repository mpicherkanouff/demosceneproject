#version 330 core

in vec4 vertex_Color;
out vec4 fragColor;

uniform bool linearGradientEnabled;

//linear
uniform vec2 a;
uniform vec2 b;
uniform vec4 startColorL;
uniform vec4 endColorL;
uniform float alpha;

//radial
uniform vec2 center;
uniform float radius;
uniform vec4 startColorR;
uniform vec4 endColorR;

void main(){
	float t;
	if (linearGradientEnabled) {
		vec2 c = a - b;
		vec2 u = a - gl_FragCoord.xy;
		float proj = dot(u,c)/dot(c, c);
		
		t = proj;
		if ( t < 0) { t = 0; }
		else if (t > 1) { t = 1;}

		fragColor.x = ((1 - t) * startColorL.x) + (t * endColorL.x);
		fragColor.y = ((1 - t) * startColorL.y) + (t * endColorL.y);
		fragColor.z = ((1 - t) * startColorL.z) + (t * endColorL.z);
    	fragColor.w = vertex_Color.w;
	} else {
		t = sqrt(pow(gl_FragCoord.x - center.x, 2) + pow(gl_FragCoord.y - center.y, 2))/radius;
		if ( t < 0) { t = 0; }
		else if (t > 1) { t = 1;}
		
		fragColor.x = ((1 - t) * startColorR.x) + (t * endColorR.x);
		fragColor.y = ((1 - t) * startColorR.y) + (t * endColorR.y);
		fragColor.z = ((1 - t) * startColorR.z) + (t * endColorR.z);
    	fragColor.w = alpha;
	}
    
}