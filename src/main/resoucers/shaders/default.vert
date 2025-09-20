#version 460

layout(location = 0) in vec3 position;
layout(location = 1) in vec3 colour;

out vec3 entityColour;

uniform mat4 model;

uniform mat4 projection;

void main() {
//    vec3[] vertices = vec3[](
//        vec3(-0.5, -0.5, 0.0),
//        vec3( 0.5, -0.5, 0.0),
//        vec3( 0.0,  0.5, 0.0)
//    );
    entityColour = colour;
    gl_Position = projection * model * vec4(position, 1);
//    gl_Position = vec4(vertices[int(gl_VertexID)], 1);
}