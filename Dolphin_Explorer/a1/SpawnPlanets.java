package a1;

import java.awt.*;
import java.io.*;
import java.util.List;
import java.util.Random;

import ray.rage.*;
import ray.rage.rendersystem.*;
import ray.rage.rendersystem.Renderable.*;
import ray.rage.scene.*;
import ray.rage.scene.controllers.*;
import ray.rml.Vector3f;
import ray.rage.rendersystem.states.*;
import ray.rage.asset.texture.*;

import myGameEngine.ObjectDistance;

public class SpawnPlanets {
    // String array of all available textures to randomly select from
    static String[] textureList = { "sun.jpg", "jupiter.jpg", "mars.jpg", "ceres.jpg", "neptune.jpg", "earth-day.jpeg",
            "moon.jpeg", "eris.jpg", "haumea.jpg", "makemake.jpg", "mercury.jpg", "saturn.jpg", "venus.jpg",
            "venusSurface.jpg" };

    public static List<Entity> createPlanet(Engine eng, SceneManager sm, List<Entity> planetList, RotationController rc,
            int nameOffset) throws IOException {
        Random myRand = new Random();
        String pName = "planet" + Integer.toString(nameOffset);

        // Set up Planet
        // NOTE: Using the earth.obj and retexturing seems to produce better results
        Entity planetE = sm.createEntity(pName, "earth.obj");
        planetE.setPrimitive(Primitive.TRIANGLES);
        SceneNode planetN = sm.getRootSceneNode().createChildSceneNode(planetE.getName() + "Node");
        planetN.attachObject(planetE);

        // Randomly set scale (0.5f - 3.5)
        float scale = myRand.nextFloat() * 3 + .5f;
        planetN.setLocalScale(scale, scale, scale);

        // Randomly posistion planet (-50 to 50)
        // Make sure new planet Position doesn't overlap with a current planet
        while (true) {
            planetN.setLocalPosition(myRand.nextInt(100) - 50, myRand.nextInt(100) - 50, myRand.nextInt(100) - 50);

            // Fix: don't spawn planets near the origin... this is a dumb fix
            if (planetN.getLocalPosition().x() < 0)
                planetN.setLocalPosition(planetN.getLocalPosition().x() - 10f, planetN.getLocalPosition().y(),
                        planetN.getLocalPosition().z());
            else
                planetN.setLocalPosition(planetN.getLocalPosition().x() + 10f, planetN.getLocalPosition().y(),
                        planetN.getLocalPosition().z());
            if (planetN.getLocalPosition().y() < 0)
                planetN.setLocalPosition(planetN.getLocalPosition().x(), planetN.getLocalPosition().y() - 10f,
                        planetN.getLocalPosition().z());
            else
                planetN.setLocalPosition(planetN.getLocalPosition().x(), planetN.getLocalPosition().y() + 10f,
                        planetN.getLocalPosition().z());
            if (planetN.getLocalPosition().z() < 0)
                planetN.setLocalPosition(planetN.getLocalPosition().x(), planetN.getLocalPosition().y(),
                        planetN.getLocalPosition().z() - 10f);
            else
                planetN.setLocalPosition(planetN.getLocalPosition().x(), planetN.getLocalPosition().y(),
                        planetN.getLocalPosition().z() + 10f);

            // If their is a collision with an existing planet returns true
            if (checkPlanetSpawnCollision(sm, planetE, planetList))
                continue;

            break;
        }

        // Assign random texture
        TextureManager tm = eng.getTextureManager();
        Texture planetTexture = tm.getAssetByPath(textureList[myRand.nextInt(14)]);
        RenderSystem rs = sm.getRenderSystem();
        TextureState state = (TextureState) rs.createRenderState(RenderState.Type.TEXTURE);
        state.setTexture(planetTexture);
        planetE.setRenderState(state);

        // Create a point light above the planet
        Light pLight = sm.createLight("light" + Integer.toString(nameOffset), Light.Type.POINT);
        pLight.setAmbient(new Color(.1f, .1f, .1f));
        pLight.setDiffuse(new Color(0.8f, 0.8f, 0.8f));
        pLight.setSpecular(new Color(1.0f, 1.0f, 1.0f));
        pLight.setRange(20f);
        SceneNode plighNode = sm.getRootSceneNode().createChildSceneNode("lightNode" + Integer.toString(nameOffset));
        plighNode.attachObject(pLight);
        plighNode.setLocalPosition(planetN.getLocalPosition());
        plighNode.moveUp(5.0f);

        // Attach a rotation controller
        rc.addNode(planetN);

        // Add planet entity to list and return
        planetList.add(planetE);
        return planetList;
    }

    private static boolean checkPlanetSpawnCollision(SceneManager sm, Entity planet, List<Entity> planetList) {
        Vector3f planetPos = (Vector3f) sm.getSceneNode(planet.getName() + "Node").getLocalPosition();

        for (Entity spawnedPlanet : planetList) {
            Vector3f spawnedPlanetPos = (Vector3f) sm.getSceneNode(spawnedPlanet.getName() + "Node").getLocalPosition();

            // If the distance between the new planet and a spawned planet overlaps...
            // return true
            if (ObjectDistance.distanceBetweenVectors(planetPos,
                    spawnedPlanetPos) <= (sm.getSceneNode(planet.getName() + "Node").getLocalScale().x() * 2.0f)
                            + sm.getSceneNode(spawnedPlanet.getName() + "Node").getLocalScale().x() * 2.0f)
                return true;
        }

        // If within 3f of dolphin return true
        if (ObjectDistance.distanceBetweenVectors((Vector3f) sm.getSceneNode("myDolphinNode").getLocalPosition(),
                planetPos) < 3)
            return true;

        // If within 3f of camera return true
        if (ObjectDistance.distanceBetweenVectors((Vector3f) sm.getCamera("MainCamera").getPo(), planetPos) < 3)
            return true;

        return false;
    }
}