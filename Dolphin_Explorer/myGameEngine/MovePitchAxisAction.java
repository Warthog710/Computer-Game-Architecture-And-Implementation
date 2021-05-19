package myGameEngine;

import ray.input.action.AbstractInputAction;
import ray.rage.scene.*;
import ray.rml.*;
import net.java.games.input.Event;

public class MovePitchAxisAction extends AbstractInputAction {
    private Camera camera;
    private SceneManager sm;

    public MovePitchAxisAction(Camera c, SceneManager sm) {
        this.camera = c;
        this.sm = sm;
    }

    // A full rotation takes 5 sec
    public void performAction(float time, Event e) {
        // Deadzone
        if (e.getValue() > -.1 && e.getValue() < .1)
            return;

        if (camera.getMode() == 'c') {
            // Grab rotation angle and current V and N vectors
            Angle rotAmt = Degreef.createFrom(-e.getValue() * .072f * time);
            Vector3f v = camera.getUp();
            Vector3f n = camera.getFd();

            // Set new V and N vectors
            camera.setUp((Vector3f) v.rotate(rotAmt, camera.getRt()).normalize());
            camera.setFd((Vector3f) n.rotate(rotAmt, camera.getRt()).normalize());
        } else {
            Angle rotAmt = Degreef.createFrom(e.getValue() * .072f * time);
            sm.getSceneNode("myDolphinNode").pitch(rotAmt);
        }
    }
}