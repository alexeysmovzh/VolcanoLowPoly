#ifdef GL_ES
    #define LOWP lowp
    #define MED mediump
    #define HIGH highp
    precision mediump float;
#else
    #define MED
    #define LOWP
    #define HIGH
#endif

uniform mat4 u_worldTrans;
uniform mat4 u_projViewTrans;
uniform float u_time;

attribute vec3 a_position;
attribute vec2 a_texCoord0;

varying vec2 v_diffuseUV;

const float rotSpeed = 0.02;

mat3 rotation(float a) {

    float s = sin(a);
    float c = cos(a);
    return mat3(vec3(c, 0.0, s),
                vec3(0.0, 1, 0.0),
                vec3(-s, 0.0, c));
}


void main() {

    v_diffuseUV = a_texCoord0;

    vec3 pos = a_position * rotation(rotSpeed * u_time);

	gl_Position = u_projViewTrans * u_worldTrans * vec4(pos, 1.0);

}