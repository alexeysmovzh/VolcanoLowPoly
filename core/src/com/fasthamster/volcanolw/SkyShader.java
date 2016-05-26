package com.fasthamster.volcanolw;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g3d.Attributes;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.Shader;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.shaders.BaseShader;
import com.badlogic.gdx.graphics.g3d.shaders.DefaultShader;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

/**
 * Created by alex on 22.05.16.
 */
public class SkyShader extends BaseShader {

    private final int u_projViewTrans = register(new Uniform("u_projViewTrans"), DefaultShader.Setters.projViewTrans);
    private final int u_worldTrans = register(new Uniform("u_worldTrans"), DefaultShader.Setters.worldTrans);
    private final int u_resolution = register(new Uniform("u_resolution"));
    private final int u_diffuseTexture = register(new Uniform("u_diffuseTexture", TextureAttribute.Diffuse), DefaultShader.Setters.diffuseTexture);
    private final int u_time = register(new Uniform("u_time"));
    private final int u_height = register(new Uniform("u_height"));
    private final int u_alpha = register(new Uniform("u_alpha"));

    private float alpha;
    private float height;
    private long globalStartTime;

    private Renderable renderable;


    // Constructor
    public SkyShader(final Renderable renderable) {

        Gdx.app.log(Constants.TAG, "SkyShader();");

        String vert = Gdx.files.internal("sky.vert").readString();
        String frag = Gdx.files.internal("sky.frag").readString();

        this.program = new ShaderProgram(vert, frag);
        this.renderable = renderable;

        globalStartTime = System.nanoTime();

    }

    public void setHeight(float height) {

        this.height = height;

    }

    public void setAlpha(float alpha) {

        this.alpha = alpha;

    }

    @Override
    public void init() {

        final ShaderProgram program = this.program;
        this.program = null;
        init(program, renderable);
        renderable = null;

    }

    @Override
    public void begin(final Camera camera, final RenderContext context) {

        super.begin(camera, context);

        set(u_resolution, camera.viewportHeight, camera.viewportWidth);
        set(u_time, (System.nanoTime() - globalStartTime) / 1000000000f);
        set(u_alpha, alpha);
        set(u_height, height);

    }

    @Override
    public void render(Renderable renderable, Attributes combinedAttributes) {

        super.render(renderable, combinedAttributes);

    }

    @Override
    public void end() {

        super.end();

    }

    @Override
    public int compareTo(Shader other) {
        return 0;
    }

    @Override
    public boolean canRender(Renderable renderable) {

//        return renderable.material.has(VolcanoBooleanAttribute.Sky);
        return true;

    }
}

