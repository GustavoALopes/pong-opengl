#version 460

in vec2 textureCoordinatesFrag;
in vec3 colorFrag;

out vec4 FragColor;

uniform sampler2D atlasTexture;

void main() {
    float c = texture(atlasTexture, textureCoordinatesFrag).r;
    FragColor = vec4(1, 1, 1, c) * vec4(colorFrag, 1);
//      FragColor = texture(atlasTexture, textureCoordinatesFrag);
//    FragColor = colorFrag * textureColor;

//    FragColor = vec4(colorFrag, 1);
}