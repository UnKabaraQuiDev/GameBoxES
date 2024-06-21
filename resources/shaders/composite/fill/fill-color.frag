#version 300 es
precision mediump float;

in vec2 fragCoord;

out vec4 fragColor;

uniform vec4 color;

void main() {
	fragColor = color;
}
