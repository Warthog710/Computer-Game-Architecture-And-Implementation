package myGameEngine;

import ray.input.action.AbstractInputAction;
import ray.rage.scene.*;
import ray.rml.*;
import net.java.games.input.Event;

public class MoveRightAction extends AbstractInputAction {
    private Camera camera;
    private SceneManager sm;

    public MoveRightAction(Camera c, SceneManager sm) {
        this.camera = c;
        this.sm = sm;
    }

    // Moves left 5.0f every 1000ms or 1 second
    public void performAction(float time, Event e) {
        if (camera.getMode() == 'c') {
            Vector3f u = camera.getRt();
            Vector3f p = camera.getPo();
            Vector3f p1 = (Vector3f) Vector3f.createFrom((time * 0.005f) * u.x(), (time * 0.005f) * u.y(),
                    (time * 0.005f) * u.z());
            Vector3f p2 = (Vector3f) p.add((Vector3) p1);

            // Limit player range from dolphin
            if (ObjectDistance.distanceBetweenVectors(p2,
                    (Vector3f) sm.getSceneNode("myDolphinNode").getLocalPosition()) >= 5) {
                return;
            } else {
                if (DetectCollision.planetCollisions(sm, camera, p2))
                    return;
                else
                    camera.setPo(p2);
            }
        } else {
            Vector3f v = (Vector3f) sm.getSceneNode("myDolphinNode").getLocalRightAxis();
            Vector3f p = (Vector3f) sm.getSceneNode("myDolphinNode").getLocalPosition();
            Vector3f p1 = (Vector3f) Vector3f.createFrom(-(time * 0.005f) * v.x(), -(time * 0.005f) * v.y(),
                    -(time * 0.005f) * v.z());
            Vector3f p2 = (Vector3f) p.add((Vector3) p1);

            if (DetectCollision.planetCollisions(sm, camera, p2))
                return;

            sm.getSceneNode("myDolphinNode").setLocalPosition(p2);
        }
    }
}