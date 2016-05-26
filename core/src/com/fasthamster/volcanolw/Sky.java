package com.fasthamster.volcanolw;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;

/**
 * Created by alex on 22.05.16.
 */

// TODO: adjust sky sphere size and gl_face culling

public class Sky {

    private ModelBatch batch;
    private ModelInstance sky;
    private Texture texture;

    private SkyShader shader;

    private static final String STARS_TEXTURE = "stars.png";


    // Constructor
    public Sky(ModelBatch batch) {

        this.batch = batch;

        setupSky();
        setupShader();

    }

    private void setupSky() {

        texture = new Texture(Gdx.files.internal(STARS_TEXTURE));
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        Material material = new Material();
        material.set(TextureAttribute.createDiffuse(texture));
//        material.set(IntAttribute.createCullFace(GL20.GL_FALSE));

        ModelBuilder builder = new ModelBuilder();
        Model model = builder.createSphere(60f, 60f, 60f, 16, 16, material, VertexAttributes.Usage.Position | VertexAttributes.Usage.TextureCoordinates);

        sky = new ModelInstance(model);
        sky.transform.setToRotation(new Vector3(1f, 0f, 0f), -90f);

    }

    private void setupShader() {

        Renderable r = new Renderable();
        sky.getRenderable(r);
        shader = new SkyShader(r);
        shader.init();

    }

    public void update(float angle) {

        float height = MathUtils.sin(angle * 0.017453292f);
        shader.setHeight(height / 2f + 0.5f);           // convert from -1..1 range to 0..1

        if(height < 0f) {
            shader.setAlpha(Math.abs(height));
        } else {
            shader.setAlpha(0f);
        }
    }

    public void render() {

        batch.render(sky, shader);

    }

    public void dispose(){

        if(texture != null) texture.dispose();
        if(sky != null) sky.nodes.clear();
        if(shader != null) shader.dispose();

    }
}
