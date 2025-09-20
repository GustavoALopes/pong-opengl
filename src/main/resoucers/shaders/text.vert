#version 460

layout(location = 0) in vec2 position;
layout(location = 1) in vec3 color;
layout(location = 2) in vec2 textureCoordinates;

out vec2 textureCoordinatesFrag;
out vec3 colorFrag;

uniform mat4 projection;

void main() {
    textureCoordinatesFrag = textureCoordinates;
    colorFrag = color;
    gl_Position = projection * vec4(position, -5, 1);
}