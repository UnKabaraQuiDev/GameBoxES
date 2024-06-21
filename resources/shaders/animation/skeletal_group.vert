#version 300 es
precision mediump float;

const int MAX_JOINTS = 50;//max joints allowed in a skeleton
const int MAX_WEIGHTS = 3;//max number of joints that can affect a vertex

layout (location = 0) in vec3 i_pos;
layout (location = 1) in vec3 i_norm;
layout (location = 2) in vec2 i_uv;
layout (location = 3) in ivec3 i_jointIndices;
layout (location = 4) in vec3 i_jointWeights;

layout(std140) uniform SceneData {
	mat4 projectionMatrix;
	mat4 viewMatrix;
	mat4 transformationMatrix;
	vec3 viewPos;
} scene;

out struct per_Vertex {
	vec3 worldpos;
	vec2 fragpos;
	vec2 uv;
	vec3 normal;
	ivec3 joints;
	vec3 weights;
} vertex;

void main(void){

	vec4 totalLocalPos = vec4(0.0);
	vec4 totalNormal = vec4(0.0);

	for(int i=0;i<MAX_WEIGHTS;i++){
		mat4 jointTransform = jointTransforms[i_jointIndices[i]];
		vec4 posePosition = jointTransform * vec4(i_pos, 1.0);
		totalLocalPos += posePosition * i_jointWeights[i];

		vec4 worldNormal = jointTransform * vec4(i_norm, 0.0);
		totalNormal += worldNormal * i_jointWeights[i];
	}

	gl_Position = scene.projectionMatrix * scene.viewMatrix * scene.transformationMatrix * totalLocalPos;

	vertex.worldpos = (scene.transformationMatrix * scene.viewMatrix * totalLocalPos).xyz;
	vertex.fragpos = gl_Position.xy;
	vertex.uv = i_uv;
	vertex.normal = totalNormal.xyz;
	vertex.joints = i_jointIndices;
	vertex.weights = i_jointWeights;

}
