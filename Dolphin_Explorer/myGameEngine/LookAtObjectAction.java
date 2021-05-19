package myGameEngine;

import ray.input.action.AbstractInputAction;
import ray.rage.scene.*;
import ray.rml.Vector3;
import ray.rml.Vector3f;
import net.java.games.input.Event;

public class LookAtObjectAction extends AbstractInputAction {
    private Camera camera;
    private SceneManager sm;

    public LookAtObjectAction(Camera c, SceneManager sm) {
        this.camera = c;
        this.sm = sm;
    }

    public void performAction(float time, Event e) {
        SceneNode minRangeNode = null;
        float minRange = 1000;

        // If camera is in node mode look at a planet
        if (camera.getMode() == 'n') {
            for (SceneNode myNodes : sm.getSceneNodes()) {
                if (myNodes.getName().contains("planet")) {
                    float temp = ObjectDistance.distanceBetweenVectors((Vector3f) myNodes.getLocalPosition(),
                            (Vector3f) sm.getSceneNode("myDolphinNode").getLocalPosition());

                    // Save the closet planet
                    if (temp < minRange) {
                        minRangeNode = myNodes;
                        minRange = temp;
                    }
                }
            }
            sm.getSceneNode("myDolphinNode").lookAt(minRangeNode);
        }
        // The camera must be in c mode
        else {
            Vector3 worldUp = Vector3f.createFrom(0.0f, 1.0f, 0.0f);

            // Lookat dolphin
            Vector3 n = sm.getSceneNode("myDolphinNode").getLocalPosition().sub(camera.getPo());
            Vector3 v = n.cross(worldUp);
            Vector3 u = v.cross(n);

            camera.setFd((Vector3f) n.normalize());
            camera.setRt((Vector3f) v.normalize());
            camera.setUp((Vector3f) u.normalize());
        }
    }
}