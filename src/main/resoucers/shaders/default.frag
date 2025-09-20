#version 460

in vec3 entityColour;

out vec4 FragColor;

void main() {
    FragColor = vec4(entityColour, 1.0);
}