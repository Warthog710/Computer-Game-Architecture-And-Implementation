package myGameEngine;

import ray.input.action.AbstractInputAction;
import ray.rage.scene.*;
import ray.rml.*;
import net.java.games.input.Event;

public class MoveYawRightAction extends AbstractInputAction {
    private Camera camera;
    private SceneManager sm;

    public MoveYawRightAction(Camera c, SceneManager sm) {
        this.camera = c;
        this.sm = sm;
    }

    // A full rotation takes 5 sec
    public void performAction(float time, Event e) {
        if (camera.getMode() == 'c') {
            // Global Yaw
            Angle rotAmt = Degreef.createFrom(-.072f * time);
            Vector3 worldC = Vector3f.createFrom(0.0f, 1.0f, 0.0f);

            camera.setRt((Vector3f) camera.getRt().rotate(rotAmt, worldC).normalize());
            camera.setUp((Vector3f) camera.getUp().rotate(rotAmt, worldC).normalize());
            camera.setFd((Vector3f) camera.getFd().rotate(rotAmt, worldC).normalize());

            // Local Yaw
            // Grab rotation angle and current U and N vectors
            // Angle rotAmt = Degreef.createFrom(1.0f);
            // Vector3f u = camera.getRt();
            // Vector3f n = camera.getFd();

            // Set new U and N vectors
            // camera.setRt((Vector3f)u.rotate(rotAmt, camera.getUp()).normalize());
            // camera.setFd((Vector3f)n.rotate(rotAmt, camera.getUp()).normalize());

        } else {
            // Global Yaw
            Vector3 worldUp = Vector3f.createFrom(0.0f, 1.0f, 0.0f);
            Matrix3 matRot = Matrix3f.createRotationFrom(Degreef.createFrom(-.072f * time), worldUp);
            sm.getSceneNode("myDolphinNode")
                    .setLocalRotation(matRot.mult(sm.getSceneNode("myDolphinNode").getWorldRotation()));

            // Local Yaw
            // Angle rotAmt = Degreef.createFrom(-1.0f);
            // sm.getSceneNode("myDolphinNode").yaw(rotAmt);
            // System.out.println("Moving yaw...");
            // System.out.println(e.getValue());
        }
    }
}