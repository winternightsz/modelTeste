package com.test.examplemod.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.test.examplemod.ExampleMod;
import com.test.examplemod.model.Model3DInfo;
import com.test.examplemod.model.ModelFace;
import com.test.examplemod.model.ParserEBuilder;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterClientReloadListenersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD, modid = ExampleMod.MODID)
public enum CustomResourceListener implements ResourceManagerReloadListener {

    INSTANCE;

    public static Model3DInfo modelInfo;
    String modelFilePath = "G:\\Github\\modelTeste\\src\\main\\resources\\assets\\examplemod\\model\\ironhide.tfm";
    Path modelPath = Path.of(modelFilePath);
    ParserEBuilder parser;

    public static List<ModelFace> faceList = new ArrayList<>();


    @SubscribeEvent
    public static void onRegisterClientReloadListeners(RegisterClientReloadListenersEvent event)
    {
        event.registerReloadListener(INSTANCE);
    }

    @Override
    public void onResourceManagerReload(ResourceManager p_10758_) {
        modelMaker();
        renderModelCustom(this.modelInfo);
    }

    public void modelMaker(){

        try {
            this.parser = new ParserEBuilder();
            this.modelInfo = this.parser.parseModel(modelPath);

            if (modelInfo.getParts().isEmpty()) {
            } else {

            }
        } catch (Exception e) {

        }
    }
    /////////////////////////////////////////////////////////////////////////////////////

    public void renderModelCustom (Model3DInfo modelInfo){
        if (modelInfo.getParts().isEmpty()) {
            //LOGGER.error("Nenhuma parte disponível para renderizar.");
            return;
        }
        //pra poder transformar o poseStack em Pose pra poder pegar o Matrix4f e o Matrix[2]f
        //PoseStack.Pose poses = posestack.last();

        //pose = matrix4f
        //normal = matrix[2]f
        //LOGGER.info("Entra no metodo renderModelAll");
        for (Model3DInfo.Part part : modelInfo.getParts()) {

            renderPart(part);
        }
    }
    private void renderPart(Model3DInfo.Part part) {
        //LOGGER.info("Entra no metodo renderPart");

        for (Model3DInfo.Box box : part.getBoxes()) {
            renderBox(box);
        }

        for (Model3DInfo.Part childPart : part.getChildren()) {
            renderPart(childPart);
        }

    }
    private void renderBox(Model3DInfo.Box box) {
        //LOGGER.info("Entra no metodo renderBox");

        JsonObject vertices = box.getMeshVertices();
        JsonObject faces = box.getMeshFaces();
        int faceindex = 0;

        //aqui vai montar as faces
        for (Map.Entry<String, JsonElement> faceEntry : faces.entrySet()) {

            JsonObject face = faceEntry.getValue().getAsJsonObject();
            JsonArray verticesArray = face.get("vertices").getAsJsonArray();

            //cria um array de 3 vertices e uv points
            float[] x = new float[3];
            float[] y = new float[3];
            float[] z = new float[3];
            float[] u = new float[3];
            float[] v = new float[3];

            float normX = (y[1]-y[0]) * (z[2]-z[0]) - (z[1]-z[0]) * (y[2]-y[0]);
            float normY = (z[1]-z[0]) * (x[2]-x[0]) - (x[1]-x[0]) * (z[2]-z[0]);
            float normZ = (x[1]-x[0]) * (y[2]-y[0]) - (y[1]-y[0]) * (x[2]-x[0]);

            for (int i = 0; i < 3; i++) {
                String vertexKey = verticesArray.get(i).getAsString();
                JsonElement vertexElement = vertices.get(vertexKey);

                if (vertexElement == null) {
                    //LOGGER.error("Vértice '{}' não encontrado no objeto meshVertices.", vertexKey);
                    continue;
                }

                if (vertexElement.isJsonArray()) {
                    JsonArray vertexArray = vertexElement.getAsJsonArray();
                    if (vertexArray.size() != 3) {
                        //LOGGER.error("O array do vértice '{}' deve conter exatamente 3 valores. Encontrado: {}", vertexKey, vertexArray.size());
                        continue;
                    }
                    x[i] = vertexArray.get(0).getAsFloat() + box.getPosX();
                    y[i] = vertexArray.get(1).getAsFloat() + box.getPosY();
                    z[i] = vertexArray.get(2).getAsFloat() + box.getPosZ();
                } else if (vertexElement.isJsonObject()) {
                    JsonObject vertex = vertexElement.getAsJsonObject();
                    x[i] = vertex.get("x").getAsFloat() + box.getPosX();
                    y[i] = vertex.get("y").getAsFloat() + box.getPosY();
                    z[i] = vertex.get("z").getAsFloat() + box.getPosZ();
                } else {
                    //LOGGER.error("Formato inesperado do vértice: {}", vertexElement);
                    continue;
                }

                JsonArray uvArray = face.get("uv").getAsJsonObject().get(vertexKey).getAsJsonArray();
                if (uvArray.size() != 2) {
                    //LOGGER.error("As coordenadas UV devem conter exatamente 2 valores. Encontrado: {}", uvArray.size());
                    continue;
                }
                u[i] = uvArray.get(0).getAsFloat();
                v[i] = uvArray.get(1).getAsFloat();

            }
            ModelFace modelface = new ModelFace(x, y, z, u, v, normX, normY, normZ);

            faceList.add(faceindex,modelface);
            faceindex++;
        }


    }



}
