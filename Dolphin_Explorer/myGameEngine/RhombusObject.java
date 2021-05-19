package myGameEngine;

import java.io.*;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import ray.rage.*;
import ray.rage.rendersystem.shader.*;
import ray.rage.scene.*;
import ray.rage.util.BufferUtil;
import ray.rage.rendersystem.states.*;
import ray.rage.asset.texture.*;
import ray.rage.rendersystem.*;
import ray.rage.rendersystem.Renderable.DataSource;

public class RhombusObject 
{
    private SceneManager sm;
    private Engine engine;

    public RhombusObject(SceneManager sm, Engine engine)
    {
        this.sm = sm;
        this.engine = engine;
    }

    public ManualObject makeObject(String name) throws IOException
    {
        ManualObject rhombus = sm.createManualObject(name);
        ManualObjectSection rhumbusSec = rhombus.createManualSection(name + "Section");
        rhombus.setGpuShaderProgram(sm.getRenderSystem().getGpuShaderProgram(GpuShaderProgram.Type.RENDERING));

        float[] vertices = new float[]
        {              
            0f, 2f, 0f, -.5f, 1f, 0f, .5f, 1f, 0f, //Front top
            .5f, 1f, 0f, -.5f, 1f, 0f, 0f, 0f, 0f, //Front bottom
            0f, 2f, -1f, 0f, 2f, 0f, .5f, 1f, 0f, //Side 1 (part 1)
            .5f, 1f, 0f, .5f, 1f, -1f, 0f, 2f, -1f, //Side 1 (part 2)
            .5f, 1f, -1f, .5f, 1f, 0f, 0f, 0f, 0f, //Side 2 (part 1)
            0f, 0f, -1f, .5f, 1f, -1f, 0f, 0f, 0f, //Side 2 (part 2)
            .5f, 1f, -1f, -.5f, 1f, -1f, 0f, 2f, -1f, //Back top
            0f, 0f, -1f, -.5f, 1f, -1f, .5f, 1f, -1f, //Back bottom
            -.5f, 1f, -1f, 0f, 2f, 0f, 0f, 2f, -1f, //Side 3 (part 1)
            -.5f, 1f, -1f, -.5f, 1f, 0f, 0f, 2f, 0f, //Side 3 (part 2)
            0f, 0f, -1f, -.5f, 1f, 0f, -.5f, 1f, -1f, //Side 4 (part 1)
            0f, 0f, -1f, 0f, 0f, 0f, -.5f, 1f, 0f //Side 4 (part 2)
        };

        float[] texcoords = new float[]
        {             
            .5f, 1f, 1f, 0f, 0f, 0f,
            0f, 1f, 1f, 1f, .5f, 0f,
            1f, 1f, 0f, 1f, 0f, 0f,
            0f, 0f, 1f, 0f, 1f, 1f,
            1f, 1f, 0f, 1f, 0f, 0f,
            1f, 0f, 1f, 1f, 0f, 0f,
            0f, 0f, 1f, 0f, .5f, 1f,
            .5f, 0f, 1f, 1f, 0f, 1f,
            0f, 0f, 1f, 1f, 0f, 1f,
            0f, 0f, 1f, 0f, 1f, 1f,
            0f, 0f, 1f, 1f, 0f, 1f,
            0f, 0f, 1f, 0f, 1f, 1f
        };

        float[] normals = new float[]
        { 
            0f, 2f, 1f, -.5f, 1f, 1f, .5f, 1f, 1f,
            .5f, 1f, 1f, -.5f, 1f, 1f, 0f, 0f, 1f, 
            .5f, 2.5f, -1f, .5f, 2.5f, 0f, 1.5f, 1.5f, 0f,
            1.5f, 1.5f, 0f, 1.5f, 1.5f, -1f, .5f, 2.5f, -1f,
            1f, .5f, -1f, 1f, .5f, 0f, .5f, -.5f, 0f,
            1f, 0f, -1.5f, 1f, 1f, -1.5f, .5f, 0f, -.5f,
            .5f, 1f, -2f, -.5f, 1f, -2f, 0f, 2f, -2f,
            0f, 0f, -2f, -.5f, 1f, -2f, .5f, 1f, -2f,
            -1.5f, 1.5f, -1f, -.5f, 2.5f, 0f, -.5f, 2.5f, -1f,
            -1.5f, 1.5f, -1f, -1.5f, 1.5f, 0f, -.5f, 2.5f, 0f,
            -.5f, -.5f, -1f, -1f, .5f, 0f, -1f, .5f, -1f,
            -.5f, -.5f, -1f, -.5f, -.5f, 0f, -1f, .5f, 0f
        };

        int[] indices = new int[] { 0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35};
        FloatBuffer vertBuf = BufferUtil.directFloatBuffer(vertices);
        FloatBuffer texBuf = BufferUtil.directFloatBuffer(texcoords);
        FloatBuffer normBuf = BufferUtil.directFloatBuffer(normals);
        IntBuffer indexBuf = BufferUtil.directIntBuffer(indices);
        rhumbusSec.setVertexBuffer(vertBuf);
        rhumbusSec.setTextureCoordsBuffer(texBuf);
        rhumbusSec.setNormalsBuffer(normBuf);
        rhumbusSec.setIndexBuffer(indexBuf);
        Texture tex = engine.getTextureManager().getAssetByPath("hexagons.jpeg");
        TextureState texState = (TextureState)sm.getRenderSystem().createRenderState(RenderState.Type.TEXTURE);
        texState.setTexture(tex);
        FrontFaceState faceState = (FrontFaceState) sm.getRenderSystem().createRenderState(RenderState.Type.FRONT_FACE);
        rhombus.setDataSource(DataSource.INDEX_BUFFER);
        rhombus.setRenderState(texState);
        rhombus.setRenderState(faceState);

        //Skybox fix
        RenderSystem rs = sm.getRenderSystem();
        ZBufferState zstate = (ZBufferState) rs.createRenderState(RenderState.Type.ZBUFFER);
        zstate.setTestEnabled(true);
        rhombus.setRenderState(zstate);
        return rhombus;
    }
}