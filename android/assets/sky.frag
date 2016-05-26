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

#define DEG_TO_RAD 0.017453292

varying MED vec2 v_diffuseUV;
uniform sampler2D u_diffuseTexture;
uniform vec2 u_resolution;
uniform float u_alpha;
uniform float u_height;

const vec3 dColor0 = vec3(0.1, 0.2, 0.3);
const vec3 dColor1 = vec3(.5, .6, .7);
const vec3 nColor0 = vec3(.004, .016, .046f);
const vec3 nColor1 = vec3(.005, .102, .334);


void main() {

    vec2 position = gl_FragCoord.xy / u_resolution.xy;

    vec3 bottomColor = mix(nColor0, dColor0, u_height);
    vec3 topColor = mix(nColor1, dColor1, u_height);

    vec4 color = vec4(mix(topColor, bottomColor, position.y), 1.0);

    if(u_alpha > 0.0) {
        vec4 texColor = texture2D(u_diffuseTexture, v_diffuseUV);
        color = mix(color, texColor, texColor.a * u_alpha);
    }

    gl_FragColor = color;
}