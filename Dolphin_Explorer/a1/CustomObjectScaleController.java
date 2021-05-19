package a1;

import ray.rage.scene.*;
import ray.rml.Vector3;

public class CustomObjectScaleController {
    private int previousScore = 0;

    public void updateObjectScale(SceneManager sm, String nodeName, int score) {
        // If the score is greater than previously...
        if (score > previousScore) {
            // Increase scale
            Vector3 temp = sm.getSceneNode(nodeName).getLocalScale();
            sm.getSceneNode(nodeName).setLocalScale(temp.x() + .2f, temp.y() + .2f, temp.z() + .2f);
            previousScore = score;
        }
    }
}