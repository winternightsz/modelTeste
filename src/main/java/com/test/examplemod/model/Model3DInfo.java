package com.test.examplemod.model;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.test.examplemod.ExampleMod;
import net.minecraft.client.renderer.texture.OverlayTexture;

import net.minecraft.resources.ResourceLocation;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class Model3DInfo {
    private String author;
    private int version;
    private List<String> notes;
    private float scaleX, scaleY, scaleZ;
    private int texWidth, texHeight;
    private String textureFile;
    private List<Part> parts;

    public static class Part {
        private int texWidth, texHeight;
        private boolean matchProject, mirror, showModel;
        public float rotPX, rotPY, rotPZ;
        public float rotAX, rotAY, rotAZ;
        private int texOffX, texOffY;
        private List<Box> boxes;
        private List<Part> children;
        private String identifier, name;

        public Part() {
            this.boxes = new ArrayList<>();
            this.children = new ArrayList<>();
        }

        // Getters e setters da classe Part
        public int getTexWidth() { return texWidth; }
        public void setTexWidth(int texWidth) { this.texWidth = texWidth; }

        public int getTexHeight() { return texHeight; }
        public void setTexHeight(int texHeight) { this.texHeight = texHeight; }

        public boolean isMatchProject() { return matchProject; }
        public void setMatchProject(boolean matchProject) { this.matchProject = matchProject; }

        public boolean isMirror() { return mirror; }
        public void setMirror(boolean mirror) { this.mirror = mirror; }

        public boolean isShowModel() { return showModel; }
        public void setShowModel(boolean showModel) { this.showModel = showModel; }

        public float getRotPX() { return rotPX; }
        public void setRotPX(float rotPX) { this.rotPX = rotPX; }

        public float getRotPY() { return rotPY; }
        public void setRotPY(float rotPY) { this.rotPY = rotPY; }

        public float getRotPZ() { return rotPZ; }
        public void setRotPZ(float rotPZ) { this.rotPZ = rotPZ; }

        public float getRotAX() { return rotAX; }
        public void setRotAX(float rotAX) { this.rotAX = rotAX; }

        public float getRotAY() { return rotAY; }
        public void setRotAY(float rotAY) { this.rotAY = rotAY; }

        public float getRotAZ() { return rotAZ; }
        public void setRotAZ(float rotAZ) { this.rotAZ = rotAZ; }

        public int getTexOffX() { return texOffX; }
        public void setTexOffX(int texOffX) { this.texOffX = texOffX; }

        public int getTexOffY() { return texOffY; }
        public void setTexOffY(int texOffY) { this.texOffY = texOffY; }

        public List<Box> getBoxes() { return boxes; }
        public List<Part> getChildren() { return children; }

        public String getIdentifier() { return identifier; }
        public void setIdentifier(String identifier) { this.identifier = identifier; }

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
    }

    public static class Box {
        private float posX, posY, posZ;
        private JsonObject meshVertices;
        private JsonObject meshFaces;
        private String identifier, name;

        public Box() {
            this.posX = 0;
            this.posY = 0;
            this.posZ = 0;
        }

        // Getters e setters da classe Box
        public float getPosX() { return posX; }
        public void setPosX(float posX) { this.posX = posX; }

        public float getPosY() { return posY; }
        public void setPosY(float posY) { this.posY = posY; }

        public float getPosZ() { return posZ; }
        public void setPosZ(float posZ) { this.posZ = posZ; }

        public JsonObject getMeshVertices() { return meshVertices; }
        public void setMeshVertices(JsonObject meshVertices) { this.meshVertices = meshVertices; }

        public JsonObject getMeshFaces() { return meshFaces; }
        public void setMeshFaces(JsonObject meshFaces) { this.meshFaces = meshFaces; }

        public String getIdentifier() { return identifier; }
        public void setIdentifier(String identifier) { this.identifier = identifier; }

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
    }

    // Getters e setters para a classe principal (Model3dInfo)
    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public int getVersion() { return version; }
    public void setVersion(int version) { this.version = version; }

    public String getTextureFile() { return textureFile; }
    public void setTextureFile(String textureFile) { this.textureFile = textureFile; }

    public float getScaleX() { return scaleX; }
    public void setScaleX(float scaleX) { this.scaleX = scaleX; }

    public float getScaleY() { return scaleY; }
    public void setScaleY(float scaleY) { this.scaleY = scaleY; }

    public float getScaleZ() { return scaleZ; }
    public void setScaleZ(float scaleZ) { this.scaleZ = scaleZ; }

    public int getTexWidth() { return texWidth; }
    public void setTexWidth(int texWidth) { this.texWidth = texWidth; }

    public int getTexHeight() { return texHeight; }
    public void setTexHeight(int texHeight) { this.texHeight = texHeight; }

    public List<Part> getParts() {
        if (parts == null) {
            parts = new ArrayList<>();
        }
        return parts;
    }

    public void renderModelAll (Model3DInfo modelInfo, Matrix4f matrix4f, Matrix3f matrix3f, VertexConsumer vertexConsumer, float red, float green, float blue, float alpha){
        if (modelInfo.getParts().isEmpty()) {
            //LOGGER.error("Nenhuma parte disponível para renderizar.");
            return;
        }
        //pra poder transformar o poseStack em Pose pra poder pegar o Matrix4f e o Matrix3f
        //PoseStack.Pose poses = posestack.last();

        //pose = matrix4f
        //normal = matrix3f
        //LOGGER.info("Entra no metodo renderModelAll");
        for (Part part : modelInfo.getParts()) {

            renderPart(part, matrix4f, matrix3f, vertexConsumer);
        }
    }
    private void renderPart(Part part, Matrix4f matrix4f, Matrix3f matrix3f, VertexConsumer vertexConsumer) {
        //LOGGER.info("Entra no metodo renderPart");
        for (Box box : part.getBoxes()) {
            renderBox(box, matrix4f, matrix3f, vertexConsumer);
        }

        for (Part childPart : part.getChildren()) {
            renderPart(childPart, matrix4f, matrix3f, vertexConsumer);
        }
    }
    private void renderBox(Box box, Matrix4f matrix4f, Matrix3f matrix3f, VertexConsumer vertexConsumer) {
        //LOGGER.info("Entra no metodo renderBox");
        JsonObject vertices = box.getMeshVertices();
        JsonObject faces = box.getMeshFaces();

        // Limite de triângulos para renderizar (para depuração)
        int triangleCount = 0;
        int maxTriangles = 3;  // Ajuste esse valor conforme necessário
        //aqui vai montar as faces
        for (Map.Entry<String, JsonElement> faceEntry : faces.entrySet()) {
            JsonObject face = faceEntry.getValue().getAsJsonObject();
            JsonArray verticesArray = face.get("vertices").getAsJsonArray();

//            if (verticesArray.size() != 3) {
//                LOGGER.error("Cada face deve ter exatamente 3 vértices. Encontrado: {}", verticesArray.size());
//                continue;
//            }
            //cria um array de 3 vertices e uv points
            float[] x = new float[3];
            float[] y = new float[3];
            float[] z = new float[3];
            float[] u = new float[3];
            float[] v = new float[3];

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
                u[i] = uvArray.get(0).getAsFloat() / this.getTexWidth();
                v[i] = uvArray.get(1).getAsFloat() / this.getTexHeight();
            }

            //LOGGER.info("Renderizando triângulo {}: x1={}, y1={}, z1={}", triangleCount, x[0], y[0], z[0]);

            renderTris(matrix4f, matrix3f, vertexConsumer, 1.0f, 1.0f, 1.0f, 1.0f, x[0], x[1], x[2], y[0], y[1], y[2], z[0], z[1], z[2], u[0], u[1], u[2], v[0], v[1],v[2]);

//            triangleCount++;
//            if (triangleCount >= maxTriangles) {
//                LOGGER.warn("Limite de {} triângulos atingido. Interrompendo a renderização.", maxTriangles);
//                break;
//            }
        }
    }

    private static void renderTris(Matrix4f matrix4f, Matrix3f matrix3f, VertexConsumer vertexConsumer, float red, float green, float blue, float alpha, float x1, float x2, float x3, float y1, float y2, float y3, float z1, float z2, float z3, float u1, float u2, float u3, float v1, float v2, float v3) {
        float normX = (y2-y1) * (z3-z1) - (z2-z1) * (y3-y1);
        float normY = (z2-z1) * (x3-x1) - (x2-x1) * (z3-z1);
        float normZ = (x2-x1) * (y3-y1) - (y2-y1) * (x3-x1);
        addVertex(matrix4f, matrix3f, vertexConsumer, red, green, blue, alpha, x1, y1, z1, u1, v1, normX, normY, normZ);
        addVertex(matrix4f, matrix3f, vertexConsumer, red, green, blue, alpha, x2, y2, z2, u2, v2, normX, normY, normZ);
        addVertex(matrix4f, matrix3f, vertexConsumer, red, green, blue, alpha, x3, y3, z3, u3, v3, normX, normY, normZ);
    }

    private static void addVertex(Matrix4f matrix4f, Matrix3f matrix3f, VertexConsumer vertexConsumer, float red, float green, float blue, float alpha, float x, float y, float z, float u, float v, float normX, float normY, float normZ) {
        vertexConsumer.vertex(matrix4f, x, y, z).color(red, green, blue, alpha).uv(u, v).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(15728880).normal(matrix3f, normX, normY, normZ).endVertex();
    }

}
