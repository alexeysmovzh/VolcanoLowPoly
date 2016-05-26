package com.fasthamster.volcanolw;

import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

/**
 * Created by alex on 19.05.16.
 */
public class Clouds {

    private ModelBatch batch;
    private Environment environment;
    private Array<ModelInstance> particles;

    private Array<ModelInstance> clouds = new Array<ModelInstance>();

    private boolean running;
    private boolean stopping = false;

    private float particlesPerSec;
    private float frustrum;
    private Vector3 step;

    private static final float MAX_VELOCITY = 0.02f;
    private static final float MIN_VELOCITY = 0.005f;
    private static final float MAX_PARTICLES = 0.1f;
    private static final float MIN_PARTICLES = 0.05f;


    // Constructor
    public Clouds(ModelBatch batch, Environment environment, Array<ModelInstance> particles) {

        this.batch = batch;
        this.environment = environment;
        this.particles = particles;

    }

    private void emit() {

        int i = Constants.rand.nextInt(particles.size);                   // Get random model
        ModelInstance particle = particles.get(i).copy();
        float z = MathUtils.random(Constants.FAR, Constants.NEAR);        // depth
        float y = MathUtils.random(Constants.BOTTOM, Constants.TOP);
        particle.transform.setToTranslation(new Vector3(frustrum, y, z));

        clouds.add(particle);

    }

    // Controls
    public void start(boolean windDirection) {

//        this.particlesPerSec = MathUtils.random(MIN_PARTICLES, MAX_PARTICLES);
//        float velocity = MathUtils.random(MIN_VELOCITY, MAX_VELOCITY);
        this.particlesPerSec = MAX_PARTICLES;
        float velocity = MAX_VELOCITY;

        if(windDirection == Constants.WIND_RIGHT_TO_LEFT) {
            this.frustrum = Constants.FRUSTUM;
            this.step = new Vector3(-velocity, 0f, 0f);
        } else {
            this.frustrum = -Constants.FRUSTUM;
            this.step = new Vector3(velocity, 0f, 0f);
        }

        running = true;

    }

    public void stop() {

        stopping = true;

        if(clouds.size == 0) {                   // but wait until remaining leave the frustum
            running = false;
            stopping = false;
            clouds.clear();
        }
    }

    int delta = 0;
    public void update() {

        if(running == true) {
            if (stopping == false && delta >= Constants.FPS / particlesPerSec) {     // Emit clouds
                emit();
                delta = 0;
            } else {
                delta++;
            }

            for (int i = 0; i < clouds.size; i++) {                                  // Move and delete clouds cycle

                ModelInstance c = clouds.get(i);
                Vector3 trans = new Vector3();
                c.transform.getTranslation(trans);

                if(Math.abs(trans.x) > Constants.FRUSTUM) {
                    clouds.removeIndex(i);
                } else {
                    c.transform.translate(step);
                }
            }
        }
    }

    public void render() {

        if(running == true)
            batch.render(clouds, environment);

    }

    public void dispose() {

        if(clouds.size != 0) clouds.clear();
        if(particles.size != 0) particles.clear();

    }
}
