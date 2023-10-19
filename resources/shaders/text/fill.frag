#version 400 core

flat in int FragIndex;
in vec2 texCoord;

out vec4 fragColor;

#define MAX_BUFFER 128
uniform int buffer[MAX_BUFFER];

#define W 30
#define H 30
uniform sampler2D raster;
uniform sampler2D rasterIndex;

uniform vec4 fillColor;

void main() {
	vec2 rasterCoord = texture2D(rasterIndex, vec2(buffer[FragIndex], 0)).rg;
	vec4 color = texture2D(raster, rasterCoord*vec2(W, H));
	fragColor = fillColor;//vec4(rasterCoord, 0, 1);//mix(vec4(0), fillColor, color);
}