package a1;

import java.awt.*;
import java.io.*;
import java.util.List;
import java.util.ArrayList;

import net.java.games.input.Controller;
import ray.rage.*;
import ray.rage.game.*;
import ray.rage.rendersystem.*;
import ray.rage.rendersystem.Renderable.*;
import ray.rage.scene.*;
import ray.rage.scene.Camera.Frustum.*;
import ray.rage.scene.controllers.*;
import ray.rml.*;
import ray.rage.rendersystem.gl4.GL4RenderSystem;
import ray.rage.asset.texture.*;
import ray.input.*;
import ray.input.action.*;

import myGameEngine.*;

//TO DO:

//DONE:
//10. Make sure you follow the coding style requirements listed in the lab 1 guide. DONE
//4. Add manual object x, y, z, world axis' DONE
//7. Instead of local yaw on both camera and dolphin it needs to be global... (check tech tips)
//2. Add some detection that limits how far the camera can move from the dolphin
//6. Fix input to rely on elapsed time instead of a fixed value (ask professor)
//Fix normal vectors on axis objects
//8. Add more light, requires ambient light + at least 1 positional light source
//1. At least 3 randomly positioned planets in space (different textures/scale)
//Skybox? See tech tips
//Implement deadzones make (0 if in between -.1 - .1)
//Add object collision detection
//3. Add some detection on when the camera gets close to a planet to increment a score (only while off the dolphin)
//Remove a planet and spawn a new one when it is added to your score? Maybe shrink scale over a few frames to make it dissapear using a ScalingController
//Implement some sort of detection so that planets can't spawn on top of each other
//5. Show game score on HUD (maybe a few other things?)
//Add the ability to ask for a hint (I.E posistion of planet in world)
//DetectCollision and LookAtPlanet currently look at/coollide with planets, lights, and other stuff. Possible fixes changing naming for non planet nodes or pass planet list?
//Play around with camera direction when you get off the dolphin
//9. Add two additional features (souvenir for each visited planet?), a manual game object that is handbuilt (with vertices, text cords, indices, and normal vectors) or a monster that attacks you?
//Every time the score is incremented increase the scale of the rhombus orbiting the origin

public class MyGame extends VariableFrameRateGame {

        GL4RenderSystem rs;
        float elapsTime = 0.0f;
        float lastUpdateTime = 0.0f;
        String elapsTimeStr, counterStr, dispStr;
        int elapsTimeSec, score = 0, nameOffset = 0;
        List<Entity> planetList = new ArrayList<>();
        List<Entity> deletedPlanets = new ArrayList<>();
        RotationController rc;
        OrbitController oc;
        CheckPlanetScore planetScoreChecker;
        DeletedPlanetsScaleHandler delPlanets;
        CustomObjectScaleController customScaleController;

        private InputManager im;
        private Action moveForwardAction, moveBackwardAction, boardDolphinToggleAction, moveLeftAction, moveRightAction,
                        moveYawLeftAction, moveYawRightAction, movePitchUpAction, movePitchDownAction,
                        moveYawAxisAction, movePitchAxisAction, moveHorizontalAxisAction, moveForwardAxisAction,
                        lookAtObjectAction;

        public MyGame() {
                super();
                System.out.println("Axis line colors: Z = Orange, Y = Red, X = Green\n");
                System.out.println("Keyboard Controls:");
                System.out.println("==================");
                System.out.println("Move Dolphin Forward: W");
                System.out.println("Move Dolphin Backward: S");
                System.out.println("Move Dolphin Left: A");
                System.out.println("Move Dolphin Right: D");
                System.out.println("Pitch Dolphin Up: Up");
                System.out.println("Pitch Dolphin Down: Down");
                System.out.println("Yaw Dolphin Left: Left");
                System.out.println("Yaw Dolphin Right: Right");
                System.out.println("Get On/Get Off Dolphin: Space");
                System.out.println("Look At Planet (while on dolphin): Tab");
                System.out.println("Look At Dolphin (while off dolphin): Tab\n");

                System.out.println("GamePad Controls:");
                System.out.println("==================");
                System.out.println("Move Dolphin Forward/Backward: Y Axis (Left Stick)");
                System.out.println("Move Dolphin Left/Right: X Axis (Left Stick)");
                System.out.println("Pitch Dolphin Up/Down: RY Axis (Right Stick)");
                System.out.println("Yaw Dolphin Left/Right: RX Axis (Right Stick)");
                System.out.println("Get On/Get Off Dolphin: Button 1");
                System.out.println("Look At Planet (while on dolphin): Button 6");
                System.out.println("Look At Dolphin (while off dolphin): Button 6\n");

                System.out.println("While on the dolphin travel towards planets. When near a planet get off the dolpin and travel even closer to collect the planet and increase your score! The rhombus orbiting the origin will grow every time you collect a planet.");
        }

        public static void main(String[] args) {
                Game game = new MyGame();
                try {
                        game.startup();
                        game.run();
                } catch (Exception e) {
                        e.printStackTrace(System.err);
                } finally {
                        game.shutdown();
                        game.exit();
                }
        }

        @Override
        protected void setupWindow(RenderSystem rs, GraphicsEnvironment ge) {
                rs.createRenderWindow(new DisplayMode(1000, 700, 24, 60), false);
        }

        @Override
        protected void setupCameras(SceneManager sm, RenderWindow rw) {
                SceneNode rootNode = sm.getRootSceneNode();
                Camera camera = sm.createCamera("MainCamera", Projection.PERSPECTIVE);
                rw.getViewport(0).setCamera(camera);

                camera.setRt((Vector3f) Vector3f.createFrom(1.0f, 0.0f, 0.0f));
                camera.setUp((Vector3f) Vector3f.createFrom(0.0f, 1.0f, 0.0f));
                camera.setFd((Vector3f) Vector3f.createFrom(0.0f, 0.0f, -1.0f));

                camera.setPo((Vector3f) Vector3f.createFrom(0.0f, 0.0f, 0.0f));
                camera.setMode('c');

                SceneNode cameraNode = rootNode.createChildSceneNode(camera.getName() + "Node");
                cameraNode.attachObject(camera);
        }

        @Override
        protected void setupScene(Engine eng, SceneManager sm) throws IOException {

                // Configure controller(s)
                setupInputs(sm.getCamera("MainCamera"), sm);

                // Place a dolphin at the backwards and to the right of the origin
                Entity dolphinE = sm.createEntity("myDolphin", "dolphinHighPoly.obj");
                dolphinE.setPrimitive(Primitive.TRIANGLES);
                SceneNode dolphinN = sm.getRootSceneNode().createChildSceneNode(dolphinE.getName() + "Node");
                dolphinN.attachObject(dolphinE);
                dolphinN.moveBackward(3.0f);
                dolphinN.moveRight(1.0f);

                // Attach a node as a child to the Dolphin to hold the camera
                SceneNode cNode = sm.getSceneNode("myDolphinNode").createChildSceneNode("cNode");
                cNode.moveUp(0.3f);
                cNode.moveBackward(0.4f);

                // Attach the camera to the Dolphin node
                sm.getCamera("MainCamera").setMode('n');
                cNode.attachObject(sm.getCamera("MainCamera"));

                // Set up X, Y, Z axis lines
                ManualObject axisObject = new AxisObject(sm, eng).makeAxisX();
                axisObject.setPrimitive(Primitive.LINES);
                SceneNode axisN = sm.getRootSceneNode().createChildSceneNode("xNode");
                axisN.attachObject(axisObject);

                axisObject = new AxisObject(sm, eng).makeAxisY();
                axisObject.setPrimitive(Primitive.LINES);
                axisN = sm.getRootSceneNode().createChildSceneNode("yNode");
                axisN.attachObject(axisObject);

                axisObject = new AxisObject(sm, eng).makeAxisZ();
                axisObject.setPrimitive(Primitive.LINES);
                axisN = sm.getRootSceneNode().createChildSceneNode("zNode");
                axisN.attachObject(axisObject);

                // Spawn custom object
                ManualObject customObject = new RhombusObject(sm, eng).makeObject("rhombus1");
                customObject.setPrimitive(Primitive.TRIANGLES);
                SceneNode customNode = sm.getRootSceneNode().createChildSceneNode("customObject");
                customNode.attachObject(customObject);
                customNode.setLocalScale(.3f, .3f, .3f);

                // Spawn node at center to attach custom object to as rotation
                sm.getRootSceneNode().createChildNode("orbitAttachmentNode");

                // Setup object controller(s)
                rc = new RotationController(Vector3f.createUnitVectorY(), .01f);
                oc = new OrbitController(sm.getSceneNode("orbitAttachmentNode"), .1f, 5.0f, .1f, true);
                sm.addController(rc);
                sm.addController(oc);
                oc.addNode(customNode);

                // Set up ambient light
                sm.getAmbientLight().setIntensity(new Color(.3f, .3f, .3f));

                // Set up Skybox
                Texture tex = eng.getTextureManager().getAssetByPath("../skyboxes/stars.jpg");
                SkyBox sk = sm.createSkyBox("skybox");
                sk.setTexture(tex, SkyBox.Face.BACK);
                sk.setTexture(tex, SkyBox.Face.FRONT);
                sk.setTexture(tex, SkyBox.Face.LEFT);
                sk.setTexture(tex, SkyBox.Face.RIGHT);
                sk.setTexture(tex, SkyBox.Face.TOP);
                sk.setTexture(tex, SkyBox.Face.BOTTOM);
                sm.setActiveSkyBox(sk);

                // Spawn 5 planets
                for (int count = 0; count < 5; count++) {
                        planetList = SpawnPlanets.createPlanet(eng, sm, planetList, rc, nameOffset);
                        nameOffset++;
                }

                planetScoreChecker = new CheckPlanetScore(nameOffset);
                delPlanets = new DeletedPlanetsScaleHandler(sm);
                customScaleController = new CustomObjectScaleController();
        }

        @Override
        protected void update(Engine engine) {
                // Get window, and calculate times
                rs = (GL4RenderSystem) engine.getRenderSystem();
                elapsTime += engine.getElapsedTimeMillis();
                elapsTimeSec = Math.round(elapsTime / 1000.0f);
                elapsTimeStr = Integer.toString(elapsTimeSec);

                // Calculate current FPS using the amount of time the last frame took
                String fps = Integer.toString(Math.round(1000 / (elapsTime - lastUpdateTime)));

                // Get camera and dolphin positions
                String cameraPos = "("
                                + Integer.toString(Math
                                                .round(engine.getSceneManager().getCamera("MainCamera").getPo().x()))
                                + ", "
                                + Integer.toString(Math
                                                .round(engine.getSceneManager().getCamera("MainCamera").getPo().y()))
                                + ", "
                                + Integer.toString(Math
                                                .round(engine.getSceneManager().getCamera("MainCamera").getPo().z()))
                                + ")";

                String dolphinPos = "(" + Integer.toString(Math
                                .round(engine.getSceneManager().getSceneNode("myDolphinNode").getLocalPosition().x()))
                                + ", "
                                + Integer.toString(Math.round(engine.getSceneManager().getSceneNode("myDolphinNode")
                                                .getLocalPosition().y()))
                                + ", " + Integer.toString(Math.round(engine.getSceneManager()
                                                .getSceneNode("myDolphinNode").getLocalPosition().z()))
                                + ")";

                // Set huds
                rs.setHUD("Time Elapsed: " + elapsTimeStr + "  Score: " + score + "  FPS: " + fps, 15, 15);
                rs.setHUD2("Dolphin Position: " + dolphinPos + "  Camera Position: " + cameraPos, 15,
                                rs.getCanvas().getHeight() - 30);

                // Calculate score and scale deleted planets
                score += planetScoreChecker.checkPlanets(engine, engine.getSceneManager(), planetList, rc, delPlanets);
                delPlanets.updateScale();

                // Process custom object scale
                customScaleController.updateObjectScale(engine.getSceneManager(), "customObject", score);

                // Process inputs
                im.update(elapsTime - lastUpdateTime);

                // Record last update in MS
                lastUpdateTime = elapsTime;
        }

        protected void setupInputs(Camera camera, SceneManager sm) {
                im = new GenericInputManager();
                List<Controller> controllerList = im.getControllers();

                // Build some action objects for doing things in response to user input
                moveForwardAction = new MoveForwardAction(camera, sm);
                moveBackwardAction = new MoveBackwardAction(camera, sm);
                moveLeftAction = new MoveLeftAction(camera, sm);
                moveRightAction = new MoveRightAction(camera, sm);
                boardDolphinToggleAction = new BoardDolphinToggleAction(camera, sm);
                moveYawLeftAction = new MoveYawLeftAction(camera, sm);
                moveYawRightAction = new MoveYawRightAction(camera, sm);
                movePitchDownAction = new MovePitchDownAction(camera, sm);
                movePitchUpAction = new MovePitchUpAction(camera, sm);
                moveYawAxisAction = new MoveYawAxisAction(camera, sm);
                movePitchAxisAction = new MovePitchAxisAction(camera, sm);
                moveHorizontalAxisAction = new MoveHorizontalAxisAction(camera, sm);
                moveForwardAxisAction = new MoveForwardAxisAction(camera, sm);
                lookAtObjectAction = new LookAtObjectAction(camera, sm);

                // Iterate over all input devices
                for (int index = 0; index < controllerList.size(); index++) {
                        // NOTE: This code also deals with no gamepads as it would not attempt to attach
                        // any gamepad controls unless it see's an item of Type.GAMEPAD

                        // If keyboard, attach inputs...
                        if (controllerList.get(index).getType() == Controller.Type.KEYBOARD) {
                                im.associateAction(controllerList.get(index),
                                                net.java.games.input.Component.Identifier.Key.W, moveForwardAction,
                                                InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
                                im.associateAction(controllerList.get(index),
                                                net.java.games.input.Component.Identifier.Key.S, moveBackwardAction,
                                                InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
                                im.associateAction(controllerList.get(index),
                                                net.java.games.input.Component.Identifier.Key.A, moveLeftAction,
                                                InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
                                im.associateAction(controllerList.get(index),
                                                net.java.games.input.Component.Identifier.Key.D, moveRightAction,
                                                InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
                                im.associateAction(controllerList.get(index),
                                                net.java.games.input.Component.Identifier.Key.LEFT, moveYawLeftAction,
                                                InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
                                im.associateAction(controllerList.get(index),
                                                net.java.games.input.Component.Identifier.Key.RIGHT, moveYawRightAction,
                                                InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
                                im.associateAction(controllerList.get(index),
                                                net.java.games.input.Component.Identifier.Key.UP, movePitchUpAction,
                                                InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
                                im.associateAction(controllerList.get(index),
                                                net.java.games.input.Component.Identifier.Key.DOWN, movePitchDownAction,
                                                InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
                                im.associateAction(controllerList.get(index),
                                                net.java.games.input.Component.Identifier.Key.SPACE,
                                                boardDolphinToggleAction, InputManager.INPUT_ACTION_TYPE.ON_PRESS_ONLY);
                                im.associateAction(controllerList.get(index),
                                                net.java.games.input.Component.Identifier.Key.TAB, lookAtObjectAction,
                                                InputManager.INPUT_ACTION_TYPE.ON_PRESS_ONLY);
                        }
                        // If gamepad, attach inputs...
                        else if (controllerList.get(index).getType() == Controller.Type.GAMEPAD) {
                                im.associateAction(controllerList.get(index),
                                                net.java.games.input.Component.Identifier.Button._0,
                                                boardDolphinToggleAction, InputManager.INPUT_ACTION_TYPE.ON_PRESS_ONLY);
                                im.associateAction(controllerList.get(index),
                                                net.java.games.input.Component.Identifier.Button._5, lookAtObjectAction,
                                                InputManager.INPUT_ACTION_TYPE.ON_PRESS_ONLY);
                                im.associateAction(controllerList.get(index),
                                                net.java.games.input.Component.Identifier.Axis.RX, moveYawAxisAction,
                                                InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
                                im.associateAction(controllerList.get(index),
                                                net.java.games.input.Component.Identifier.Axis.RY, movePitchAxisAction,
                                                InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
                                im.associateAction(controllerList.get(index),
                                                net.java.games.input.Component.Identifier.Axis.Y, moveForwardAxisAction,
                                                InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
                                im.associateAction(controllerList.get(index),
                                                net.java.games.input.Component.Identifier.Axis.X,
                                                moveHorizontalAxisAction,
                                                InputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
                        }
                }
        }
}
