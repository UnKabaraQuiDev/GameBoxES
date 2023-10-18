#version 400 core

in vec2 fragCoord;

uniform float hue;

//layout (location=0) out vec4 color;
out vec4 fragColor;

vec3 hsv(float hue) {
	hue = mod(hue, 1);
	float r = abs(hue * 6 - 3) - 1;
	float g = 2 - abs(hue * 6 - 2);
	float b = 2 - abs(hue * 6 - 4);
	vec3 rgb = vec3(r, g, b);
	rgb = clamp(rgb, 0, 1);
	return rgb;
}

void main() {
	/*float x = fragCoord.x;
    float y = fragCoord.y;
    float scale = 20;
    float tScale = 2;
    float yScale = 25;
    float nx = floor(x*scale)*tScale;
    float ny = floor(y*scale)*tScale;
    float width = sin(nx)*0.5+0.5;
    float height = sin(ny*yScale)*0.5+0.5;
    fragColor = vec4(hsv(cos(width)*0.5+0.5+0.45), 1.0) * (width * height);*/
    fragColor = vec4(0.2, 0.3, 0.4, 1.0);
}