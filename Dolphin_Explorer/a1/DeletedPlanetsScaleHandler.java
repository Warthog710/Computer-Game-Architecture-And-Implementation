package a1;

import java.util.ArrayList;
import java.util.List;

import ray.rage.scene.*;
import ray.rml.*;

public class DeletedPlanetsScaleHandler {
    private List<Entity> deletedPlanets;
    private SceneManager sm;

    public DeletedPlanetsScaleHandler(SceneManager sm) {
        deletedPlanets = new ArrayList<>();
        this.sm = sm;
    }

    // Add a new planet
    public void addNewDeletedPlanet(Entity planet) {
        this.deletedPlanets.add(planet);
    }

    // Called to update the scale of a deleted planet
    public void updateScale() {
        // Iterate through all deleted planets
        for (int count = 0; count < deletedPlanets.size(); count++) {
            // Reduce the scale
            Vector3 temp = sm.getSceneNode(deletedPlanets.get(count).getName() + "Node").getLocalScale();
            Vector3 newScale = (Vector3) Vector3f.createFrom(temp.x() - .05f, temp.y() - .05f, temp.z() - .05f);
            sm.getSceneNode(deletedPlanets.get(count).getName() + "Node").setLocalScale(newScale);

            removePlanets(deletedPlanets.get(count));
        }
    }

    private void removePlanets(Entity planet) {
        // If the scale of the planet is small, remove it entirely along with its
        // associated light/lightNode
        if (sm.getSceneNode(planet.getName() + "Node").getLocalScale().x() <= .01f) {
            // Grab planet number... I wish I didn't have to do this...
            String plNum = planet.getName().replace("planet", "");
            plNum = plNum.replace("Node", "");

            // Destry all the NODES... with these names...
            String pName = planet.getName();
            deletedPlanets.remove(planet);
            sm.destroySceneNode(pName + "Node");
            sm.destroySceneNode("lightNode" + plNum);
            sm.destroyLight("light" + plNum);
            sm.destroyEntity(pName);
        }
    }
}
