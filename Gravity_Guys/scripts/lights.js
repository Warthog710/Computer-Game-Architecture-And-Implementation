//Import necessary packages
var JavaPackages = new JavaImporter
(
    Packages.ray.rage.scene.SceneManager,  
    Packages.ray.rage.scene.Light,  
    Packages.ray.rage.scene.Light.Type,  
    Packages.ray.rage.scene.Light.Type.DIRECTIONAL, 
    Packages.ray.rml.Vector3f, 
    Packages.java.awt.Color
);

with (JavaPackages)
{
    //Create directional light
    var lightName = "dLight1";
    var dLightPos = Vector3f.createFrom(0, 15, -5);
    var dLight = sm.createLight("dLight1", Light.Type.DIRECTIONAL);
    dLight.setAmbient(new Color(.1, .1, .1));
    dLight.setDiffuse(new Color(.4, .4, .4));
    dLight.setSpecular(new Color(.7, .7, .7));
    dLight.setRange(80);
    
    //Create lamp light
    var lampLightName = "lampLight";
    var lampLight = sm.createLight("lampLight", Light.Type.POINT);
    lampLight.setAmbient(new Color(0, 0, 0));
    lampLight.setDiffuse(new Color(.7, .7, .7));
    lampLight.setSpecular(new Color(.9, .9, .9));
    lampLight.setRange(10);

    //Ambient light color
    var ambColor = new Color(.3, .3, .3);
}