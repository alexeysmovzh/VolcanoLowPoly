package com.fasthamster.volcanolw;

import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

/**
 * Created by alex on 09.05.16.
 */


public class Smoke {

    private ModelBatch batch;
    private Environment environment;

    private Array<ModelInstance> particles;
    private Array<Particle> smoke = new Array<Particle>();

    private boolean running;
    private boolean stopping = false;

    private Vector3 step;

    private static final float SPEED = 0.002f;
    private static final float WIND_FACTOR = 0.0025f;
    private static final Vector3 EMITTER = new Vector3(0f, 8f, 0f);
    private static final float PARTICLES_PER_SEC = 0.15f;
    private static final float DISPERSION = 0.0015f;
    private static final float SCALE = 1.001f;                          // scale particle to this amount each update step


    // Particle information holder
    public static class Particle {

        public final ModelInstance particle;
        public final Vector3 step;

        public Particle(ModelInstance particle, Vector3 step) {

            this.particle = particle;
            this.step = step;

        }
    }

    // Constructor
    public Smoke(ModelBatch batch, Environment environment, Array<ModelInstance> particles) {

        this.batch = batch;
        this.environment = environment;
        this.particles = particles;

    }

    // TODO: random angle start rotation
    private void emit() {

        // Get random model
        int i = Constants.rand.nextInt(particles.size);
        ModelInstance particle = particles.get(i).copy();

        // Dispersion
        float factor = MathUtils.random(-DISPERSION, DISPERSION);

        particle.transform.setToTranslation(EMITTER);

        smoke.add(new Particle(particle, step.cpy().add(new Vector3(factor, 0f, factor))));

    }

    // Controls
    public void start(boolean windDirection) {

        if(windDirection == Constants.WIND_RIGHT_TO_LEFT) {
            this.step = new Vector3(WIND_FACTOR, SPEED, 0f);
        } else {
            this.step = new Vector3(-WIND_FACTOR, SPEED, 0f);
        }

        running = true;
    }

    public void stop() {

        stopping = true;                            // if call stopping smoke
                                                    // cancel emitting new particles
        if(smoke.size == 0) {                       // but wait until remaining leave the frustrum
            running = false;
            stopping = false;
            smoke.clear();
        }
    }

    int delta = 0;
    public void update() {

        if(running == true && stopping == false) {
            if (delta >= Constants.FPS / PARTICLES_PER_SEC) {
                emit();
                delta = 0;
            } else {
                delta++;
            }
        }
    }

    public void render() {

        if(running == true) {
            for (int i = 0; i < smoke.size; i++) {

                Particle p = smoke.get(i);
                Vector3 trans = new Vector3();
                p.particle.transform.getTranslation(trans);

                if (trans.y > Constants.TOP) {
                    smoke.removeIndex(i);
                } else {
                    p.particle.transform.scl(SCALE).translate(p.step);
                    batch.render(p.particle, environment);
                }
            }
        }
    }

    public void dispose() {

        if(smoke != null) smoke.clear();
        if(particles != null) particles.clear();

    }
}
