#version 330 core

in vec4 vertex_Color;
out vec4 fragColor;

//linear
uniform vec2 a;
uniform vec2 b;
uniform vec4 startColorL;
uniform vec4 endColorL;


void main(){
	float t;
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
    
}