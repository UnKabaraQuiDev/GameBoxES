#version 300 es
precision mediump float;

in vec4 color;
in vec3 fragPos;
in vec3 camPos;

out vec4 fragColor;

void main()
{

	fragColor = mix(color, vec4(0, 0, 0, 0), float(length(fragPos - camPos) > 25));
	/*fragColor = vec4(fragPos, 1);
	fragColor = vec4(camPos, 1);
	fragColor = vec4(mix(fragPos, camPos, float(length(fragPos - camPos) < 5)), 1);*/
}
