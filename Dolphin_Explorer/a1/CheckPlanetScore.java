package a1;

import java.util.List;

import myGameEngine.ObjectDistance;
import ray.rage.*;
import ray.rage.scene.*;
import ray.rage.scene.controllers.*;
import ray.rml.*;

public class CheckPlanetScore {
    private int nameOffset;

    public CheckPlanetScore(int nameOffset) {
        this.nameOffset = nameOffset;
    }

    public int checkPlanets(Engine eng, SceneManager sm, List<Entity> planetList, RotationController rc,
            DeletedPlanetsScaleHandler delplanets) {
        // If camera is in N mode this check is not necessary... return 0
        if (sm.getCamera("MainCamera").getMode() == 'n')
            return 0;

        // Else, check for any planets with scoring range and increment score if true
        else {
            // Iterate through all spawned planets
            for (Entity myplanet : planetList) {
                // If close enough to the planet, Do some stuff...
                if (ObjectDistance.distanceBetweenVectors(sm.getCamera("MainCamera").getPo(),
                        (Vector3f) sm.getSceneNode(myplanet.getName() + "Node").getLocalPosition()) <= 2.1
                                * sm.getSceneNode(myplanet.getName() + "Node").getLocalScale().x()) {
                    // Remove the planet and add it as a deleted planet
                    delplanets.addNewDeletedPlanet(myplanet);
                    planetList.remove(myplanet);

                    // Attempt to spawn a new planet
                    try {
                        planetList = SpawnPlanets.createPlanet(eng, sm, planetList, rc, nameOffset);
                    } catch (Exception e) {
                        System.out.println("Failure spawning a new planet... Texture error?");
                    }

                    this.nameOffset++;

                    // Return for score modifier
                    return 1;
                }
            }
        }
        return 0;
    }
}