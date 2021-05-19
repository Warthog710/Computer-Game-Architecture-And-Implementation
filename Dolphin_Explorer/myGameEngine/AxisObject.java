package myGameEngine;

import java.io.*;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import ray.rage.*;
import ray.rage.rendersystem.shader.*;
import ray.rage.scene.*;
import ray.rage.util.BufferUtil;
import ray.rage.rendersystem.states.*;
import ray.rage.asset.material.Material;
import ray.rage.asset.texture.*;
import ray.rage.rendersystem.*;

public class AxisObject {
    private SceneManager sm;
    private Engine engine;

    public AxisObject(SceneManager sm, Engine engine) {
        this.sm = sm;
        this.engine = engine;
    }

    public ManualObject makeAxisZ() throws IOException {
        ManualObject axis = sm.createManualObject("zAxis");
        ManualObjectSection axisSec = axis.createManualSection("axisSection");
        axis.setGpuShaderProgram(sm.getRenderSystem().getGpuShaderProgram(GpuShaderProgram.Type.RENDERING));

        float[] vertices = new float[] { 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 100.0f };

        float[] texcoords = new float[] { 0.0f, 0.0f, 0.0f, 1.0f };

        float[] normals = new float[] { 1.0f, 1.0f, 0.0f, -1.0f, -1.0f, 100.0f };

        int[] indices = new int[] { 0, 1 };
        FloatBuffer vertBuf = BufferUtil.directFloatBuffer(vertices);
        FloatBuffer texBuf = BufferUtil.directFloatBuffer(texcoords);
        FloatBuffer normBuf = BufferUtil.directFloatBuffer(normals);
        IntBuffer indexBuf = BufferUtil.directIntBuffer(indices);
        axisSec.setVertexBuffer(vertBuf);
        axisSec.setTextureCoordsBuffer(texBuf);
        axisSec.setNormalsBuffer(normBuf);
        axisSec.setIndexBuffer(indexBuf);
        Material mat = sm.getMaterialManager().getAssetByPath("default.mtl");
        Texture tex = engine.getTextureManager().getAssetByPath("flatOrange.jpg");
        TextureState tstate = (TextureState) sm.getRenderSystem().createRenderState(RenderState.Type.TEXTURE);
        tstate.setTexture(tex);
        axis.setRenderState(tstate);
        axis.setMaterial(mat);

        // Skybox fix
        RenderSystem rs = sm.getRenderSystem();
        ZBufferState zstate = (ZBufferState) rs.createRenderState(RenderState.Type.ZBUFFER);
        zstate.setTestEnabled(true);
        axis.setRenderState(zstate);
        return axis;
    }

    public ManualObject makeAxisX() throws IOException {
        ManualObject axis = sm.createManualObject("xAxis");
        ManualObjectSection axisSec = axis.createManualSection("axisSection");
        axis.setGpuShaderProgram(sm.getRenderSystem().getGpuShaderProgram(GpuShaderProgram.Type.RENDERING));

        float[] vertices = new float[] { 0.0f, 0.0f, 0.0f, 100.0f, 0.0f, 0.0f };

        float[] texcoords = new float[] { 0.0f, 0.0f, 0.0f, 1.0f };

        float[] normals = new float[] { 0.0f, 1.0f, 1.0f, 100.0f, -1.0f, -1.0f };

        int[] indices = new int[] { 0, 1 };
        FloatBuffer vertBuf = BufferUtil.directFloatBuffer(vertices);
        FloatBuffer texBuf = BufferUtil.directFloatBuffer(texcoords);
        FloatBuffer normBuf = BufferUtil.directFloatBuffer(normals);
        IntBuffer indexBuf = BufferUtil.directIntBuffer(indices);
        axisSec.setVertexBuffer(vertBuf);
        axisSec.setTextureCoordsBuffer(texBuf);
        axisSec.setNormalsBuffer(normBuf);
        axisSec.setIndexBuffer(indexBuf);
        Material mat = sm.getMaterialManager().getAssetByPath("default.mtl");
        Texture tex = engine.getTextureManager().getAssetByPath("flatGreen.jpg");
        TextureState tstate = (TextureState) sm.getRenderSystem().createRenderState(RenderState.Type.TEXTURE);
        tstate.setTexture(tex);
        axis.setRenderState(tstate);
        axis.setMaterial(mat);

        // Skybox fix
        RenderSystem rs = sm.getRenderSystem();
        ZBufferState zstate = (ZBufferState) rs.createRenderState(RenderState.Type.ZBUFFER);
        zstate.setTestEnabled(true);
        axis.setRenderState(zstate);
        return axis;
    }

    public ManualObject makeAxisY() throws IOException {
        ManualObject axis = sm.createManualObject("yAxis");
        ManualObjectSection axisSec = axis.createManualSection("axisSection");
        axis.setGpuShaderProgram(sm.getRenderSystem().getGpuShaderProgram(GpuShaderProgram.Type.RENDERING));

        float[] vertices = new float[] { 0.0f, 0.0f, 0.0f, 0.0f, 100.0f, 0.0f };

        float[] texcoords = new float[] { 0.0f, 0.0f, 0.0f, 1.0f };

        float[] normals = new float[] { 1.0f, 0f, 1.0f, -1.0f, 100.0f, -1.0f };

        int[] indices = new int[] { 0, 1 };
        FloatBuffer vertBuf = BufferUtil.directFloatBuffer(vertices);
        FloatBuffer texBuf = BufferUtil.directFloatBuffer(texcoords);
        FloatBuffer normBuf = BufferUtil.directFloatBuffer(normals);
        IntBuffer indexBuf = BufferUtil.directIntBuffer(indices);
        axisSec.setVertexBuffer(vertBuf);
        axisSec.setTextureCoordsBuffer(texBuf);
        axisSec.setNormalsBuffer(normBuf);
        axisSec.setIndexBuffer(indexBuf);
        Material mat = sm.getMaterialManager().getAssetByPath("default.mtl");
        Texture tex = engine.getTextureManager().getAssetByPath("flatRed.jpg");
        TextureState tstate = (TextureState) sm.getRenderSystem().createRenderState(RenderState.Type.TEXTURE);
        tstate.setTexture(tex);
        axis.setRenderState(tstate);
        axis.setMaterial(mat);

        // Skybox fix
        RenderSystem rs = sm.getRenderSystem();
        ZBufferState zstate = (ZBufferState) rs.createRenderState(RenderState.Type.ZBUFFER);
        zstate.setTestEnabled(true);
        axis.setRenderState(zstate);
        return axis;
    }
}
