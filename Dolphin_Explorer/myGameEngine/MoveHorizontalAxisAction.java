package myGameEngine;

import ray.input.action.AbstractInputAction;
import ray.rage.scene.*;
import ray.rml.*;
import net.java.games.input.Event;

public class MoveHorizontalAxisAction extends AbstractInputAction {
    private Camera camera;
    private SceneManager sm;

    public MoveHorizontalAxisAction(Camera c, SceneManager sm) {
        this.camera = c;
        this.sm = sm;
    }

    // Move left or right 5.0f every 1000ms or 1 second (assuming axis value = 1)
    public void performAction(float time, Event e) {
        // Deadzone
        if (e.getValue() > -.1 && e.getValue() < .1)
            return;

        if (camera.getMode() == 'c') {
            Vector3f u = camera.getRt();
            Vector3f p = camera.getPo();
            Vector3f p1 = (Vector3f) Vector3f.createFrom((time * (e.getValue() * u.x()) / 200),
                    (time * (e.getValue() * u.y()) / 200), (time * (e.getValue() * u.z()) / 200));
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
        }

        else {
            Vector3f v = (Vector3f) sm.getSceneNode("myDolphinNode").getLocalRightAxis();
            Vector3f p = (Vector3f) sm.getSceneNode("myDolphinNode").getLocalPosition();
            Vector3f p1 = (Vector3f) Vector3f.createFrom(-(time * (e.getValue() * v.x()) / 200),
                    -(time * (e.getValue() * v.y()) / 200), -(time * (e.getValue() * v.z()) / 200));
            Vector3f p2 = (Vector3f) p.add((Vector3) p1);

            if (DetectCollision.planetCollisions(sm, camera, p2))
                return;

            sm.getSceneNode("myDolphinNode").setLocalPosition(p2);
        }
    }
}