#version 330 core

in vec4 position;
in vec4 color;

uniform mat4 modelviewMatrix;
uniform mat4 projectionMatrix;
uniform float t;

out vec4 vertex_Color;

void main(){	
    vec4 eyespacePosition =  modelviewMatrix * position;
    gl_Position = projectionMatrix * eyespacePosition;
    gl_Position.x = gl_Position.x + cos((5*t) + (5*gl_Position.y));
    vertex_Color = color;
}
