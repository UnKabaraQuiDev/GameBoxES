#version 330

in  vec2 texCoord;

out vec4 fragColor;

uniform sampler2D txtSampler;
uniform float t;

void main()
{
	fragColor = mix(texture(txtSampler, texCoord), 1-texture(txtSampler, texCoord), t);
}
