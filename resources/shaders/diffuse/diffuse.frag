#version 400 core

in vec3 normal;
in vec2 texCoord;
in vec3 fragPos;

out vec4 fragColor;

uniform vec3 viewPos;

uniform float shininess;
uniform vec3 diffuseColor;
uniform vec3 specularColor;

// lights
struct PointLight {
	vec3 position;

	float constant;
	float linear;
	float quadratic;

	vec3 ambientColor;
	vec3 diffuseColor;
	vec3 specularColor;
};

#define MAX_LIGHTS 10
uniform int lightCount;
uniform PointLight lights[MAX_LIGHTS];

uniform vec3 ambient;

void main()
{
	vec3 norm = normalize(normal);
	vec3 ambient = vec3(0, 0, 0);
	vec3 diffuse = vec3(0, 0, 0);
	vec3 specular = vec3(0, 0, 0);
	vec3 viewDir = normalize(viewPos - fragPos);

	// Phong lighting model
	for (int i = 0; i < min(lightCount, MAX_LIGHTS); i++) {
		/*vec3 lightPos = lights[i].position;
		vec3 lightDir = normalize(lightPos - fragPos);
		vec3 reflectDir = reflect(-lightDir, norm);

		float spec = pow(max(dot(viewDir, reflectDir), 0.0), shininess);
		specular += specularStrength * spec * (lights[i].color + 0.5);

		float diff = max(dot(norm, lightDir), 0.0);
		diffuse += lights[i].color * diff;*/

		PointLight light = lights[i];

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
		ambient += (light.ambientColor + diffuseColor)*attenuation;
		diffuse += (light.diffuseColor * diff + diffuseColor)*attenuation;
		specular += (light.specularColor * spec + specularColor)*attenuation;

		//fragColor = vec4((ambient + diffuse + specular)*attenuation, 1);
	}

	vec3 result = (ambient + diffuse + specular);
	fragColor = vec4(result, 1.0);

}
