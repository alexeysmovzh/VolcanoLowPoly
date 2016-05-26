package com.fasthamster.volcanolw;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.DirectionalLightsAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.loader.G3dModelLoader;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.UBJsonReader;


// TODO: start on android and check performance
// TODO: airplane

public class VolcanoLowPoly extends ApplicationAdapter {

    private ModelInstance volcano;
    private ModelBatch batch;
    private PerspectiveCamera camera;
    private Environment environment;
    private DirectionalLight light;

    private CameraInputController controller;

    private Sky sky;
    private Smoke smoke;
    private Clouds clouds;

    private boolean paused;

    private Vector3 lightDir = LIGHT_DIR;
    private static final Vector3 LIGHT_DIR = new Vector3(-1f, 0f, 0f);

    private boolean windDirection;


    @Override
	public void create () {

        // Environment
        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
        light = new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f);
        environment.add(light);

        // Model batch
        batch = new ModelBatch();

        setupCam();
        setupModels();

        paused = false;

	}

    // Clockwise rotation
    private Vector3 lightRotate(float angle) {

        float a = angle * Constants.DEG_TO_RAD;
        float c = MathUtils.cos(a);
        float s = MathUtils.sin(a);

        return new Vector3(c * lightDir.x - s * lightDir.y,
                           s * lightDir.x + c * lightDir.y,
                           0f);
    }

    private void setupCam() {

        camera = new PerspectiveCamera(50f, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        camera.position.set(0f, 6f, 25f);
        camera.lookAt(0f, 4f, 0f);
        camera.near = 0.1f;
        camera.far = 100f;
        camera.update();

        controller = new CameraInputController(camera);
        Gdx.input.setInputProcessor(controller);

    }

    private void setupModels() {

        Array<ModelInstance> smokeParticles = new Array<ModelInstance>();
        Array<ModelInstance> cloudParticles = new Array<ModelInstance>();

        UBJsonReader jsonReader = new UBJsonReader();
        G3dModelLoader modelLoader = new G3dModelLoader(jsonReader);

        Model model = modelLoader.loadModel(Gdx.files.internal("scene.g3db"));

        for(int i = 0; i < model.nodes.size; i++) {

            String id = model.nodes.get(i).id;
            ModelInstance instance = new ModelInstance(model, id);

            if(id.equals("Volcano")) {
                volcano = instance;
            }
            if (id.startsWith("Smoke")) {
                smokeParticles.add(instance);
            }
            if(id.startsWith("Cloud")) {
                cloudParticles.add(instance);
            }
        }

        smoke = new Smoke(batch, environment, smokeParticles);
        clouds = new Clouds(batch, environment, cloudParticles);
        sky = new Sky(batch);

    }

    int timer = 0;
    private void update() {

        windDirection = Constants.rand.nextBoolean();             // Random wind direction

        // Scene update
        if(timer == 2*Gdx.graphics.getFramesPerSecond()) {
            clouds.start(windDirection);
        }
        if(timer == 18*Gdx.graphics.getFramesPerSecond()) {
            clouds.stop();
        }
        if(timer == 38*Gdx.graphics.getFramesPerSecond()) {
            smoke.start(windDirection);
        }
        if(timer == 58*Gdx.graphics.getFramesPerSecond()) {
            smoke.stop();
            timer = 0;
        }

        timer += 1;

        // Light update
        DirectionalLightsAttribute dirLights = ((DirectionalLightsAttribute)environment.get(DirectionalLightsAttribute.Type));
        dirLights.lights.get(0).setDirection(lightRotate(angle));

        if(angle >= 360f) angle = 0f;
        angle += Gdx.graphics.getDeltaTime() * 5f;

    }

    float angle = 0f;
	@Override
	public void render () {

        if(!paused) {

            controller.update();

            // Update cycle
            update();
            sky.update(angle);
            smoke.update();
            clouds.update();

            // Render cycle
            Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT | (Gdx.graphics.getBufferFormat().coverageSampling? GL20.GL_COVERAGE_BUFFER_BIT_NV:0));

            batch.begin(camera);

            sky.render();
            batch.render(volcano, environment);
            clouds.render();
            smoke.render();

            batch.end();

        }
    }

    @Override
    public void pause() {

        paused = true;

    }

    @Override
    public void resume() {

        paused = false;

    }

    @Override
    public void dispose() {

        environment.clear();
        batch.dispose();
        smoke.dispose();
        clouds.dispose();
        sky.dispose();

    }
}
