package myGameEngine;

import ray.input.action.AbstractInputAction;
import ray.rage.scene.*;
import ray.rml.*;
import net.java.games.input.Event;

public class BoardDolphinToggleAction extends AbstractInputAction {
    private Camera camera;
    private SceneManager sm;

    public BoardDolphinToggleAction(Camera c, SceneManager sm) {
        this.camera = c;
        this.sm = sm;
    }

    public void performAction(float time, Event e) {
        if (camera.getMode() == 'c') {
            camera.setMode('n');
            sm.getSceneNode("MainCameraNode").detachObject(this.camera.getName());
            sm.getSceneNode("cNode").attachObject(camera);
        } else {
            camera.setMode('c');
            Vector3 worldUp = Vector3f.createFrom(0.0f, 1.0f, 0.0f);

            // Set camera position to left of the dolphin
            sm.getSceneNode("myDolphinNode").moveBackward(1.0f);
            camera.setPo((Vector3f) sm.getSceneNode("myDolphinNode").getLocalPosition());
            sm.getSceneNode("myDolphinNode").moveForward(1.0f);

            // Look at dolphin
            Vector3 n = sm.getSceneNode("myDolphinNode").getLocalPosition().sub(camera.getPo());
            Vector3 v = n.cross(worldUp);
            Vector3 u = v.cross(n);

            camera.setFd((Vector3f) n.normalize());
            camera.setRt((Vector3f) v.normalize());
            camera.setUp((Vector3f) u.normalize());

            // Attach/detach camera
            sm.getSceneNode("cNode").detachObject(camera);
            sm.getSceneNode("MainCameraNode").attachObject(camera);
        }
    }
}