#version 330

in vec2 texCoord;
in vec3 normal;

out vec4 fragColor;

uniform sampler2D diffuse;
uniform float t;

struct PointLight {
	vec3 position;

	float constant;
	float linear;
	float quadratic;

	vec3 ambient;
	vec3 diffuse;
	vec3 specular;
};

#define MAX_LIGHTS 6
uniform int lightCount;
uniform PointLight lights[MAX_LIGHTS];

uniform float shininess;

vec3 CalcPointLight(PointLight light, vec3 normal, vec3 fragPos, vec3 viewDir)
{
	vec3 lightDir = normalize(light.position - fragPos);
	// diffuse shading
	float diff = max(dot(normal, lightDir), 0.0);
	// specular shading
	vec3 reflectDir = reflect(-lightDir, normal);
	float spec = pow(max(dot(viewDir, reflectDir), 0.0), shininess);
	// attenuation
	float distance = length(light.position - fragPos);
	float attenuation = 1.0 / (light.constant + light.linear * distance + light.quadratic * (distance * distance));
	// combine results
	vec3 ambient = light.ambient * texture(diffuse, texCoord).xyz;
	vec3 diffuse = light.diffuse * diff * texture(diffuse, texCoord).xyz;
	vec3 specular = light.specular * spec * texture(specular, texCoord).xyz;

	ambient *= attenuation;
	diffuse *= attenuation;
	specular *= attenuation;

	return (ambient + diffuse + specular);
}

void main()
{
	vec3 norm = normalize(normal);
	vec3 viewDir = normalize(/*viewPos*/ - gl_fragCoord);

	vec3 result = vec3(0, 0, 0);

	for (int i = 0; i < min(lightCount, MAX_LIGHTS); i++) {
		result += CalcPointLight(lights[i], normal, gl_fragCoord, viewDir);
	}

	fragColor = vec4(mix(result, vec3(1, 1, 1), t/2+0.5), 1.0);
}
