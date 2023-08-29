#version 330

//#pragma glslify: voronoi3d = require('glsl-voronoi-noise/3d')

in  vec2 texCoord;

out vec4 fragColor;

uniform sampler2D txtSampler;
uniform float t;

void main()
{
	fragColor = mix(texture(txtSampler, texCoord), vec4(1.0, 1.0, 1.0, 1.0), t);
}
