package myGameEngine;

import ray.rage.scene.Camera;
import ray.rage.scene.SceneManager;
import ray.rage.scene.SceneNode;
import ray.rml.*;

public class DetectCollision {
    public static boolean planetCollisions(SceneManager sm, Camera camera, Vector3f newPos) {
        float colSphere = 2.6f;

        // Set collision distance if off of dolphin
        if (camera.getMode() == 'c')
            colSphere = 2.0f;

        // Iterate through all nodes
        for (SceneNode myNode : sm.getSceneNodes()) {
            // If its a planet node
            if (myNode.getName().contains("planet")) {
                // If the new position is within a certain range of the object...
                if (ObjectDistance.distanceBetweenVectors(newPos, (Vector3f) myNode.getLocalPosition()) < colSphere
                        * myNode.getLocalScale().x())
                    return true;
            }
        }
        return false;
    }
}
