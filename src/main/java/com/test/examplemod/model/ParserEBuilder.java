package com.test.examplemod.model;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ParserEBuilder {
    private static final Gson GSON = new Gson();

    public Model3DInfo parseModel(Path modelPath) throws IOException {
        String json = Files.readString(modelPath);
        JsonObject jsonObject = GSON.fromJson(json, JsonObject.class);

        Model3DInfo modelInfo = new Model3DInfo();

        // Set the properties using setters or directly if setters are not available
        modelInfo.setAuthor(jsonObject.get("author").getAsString());
        modelInfo.setVersion(jsonObject.get("version").getAsInt());
        modelInfo.setTextureFile(jsonObject.get("textureFile").getAsString());
        modelInfo.setScaleX(jsonObject.get("scaleX").getAsFloat());
        modelInfo.setScaleY(jsonObject.get("scaleY").getAsFloat());
        modelInfo.setScaleZ(jsonObject.get("scaleZ").getAsFloat());
        modelInfo.setTexWidth(jsonObject.get("texWidth").getAsInt());
        modelInfo.setTexHeight(jsonObject.get("texHeight").getAsInt());

        // Parsing das partes do modelo
        JsonArray partsArray = jsonObject.getAsJsonArray("parts");
        for (JsonElement partElement : partsArray) {
            Model3DInfo.Part part = parsePart(partElement.getAsJsonObject());
            modelInfo.getParts().add(part);
        }

        return modelInfo;
    }

    private static Model3DInfo.Part parsePart(JsonObject partObject) {
        Model3DInfo.Part part = new Model3DInfo.Part();

        // Usando setters para definir os valores
        part.setTexWidth(partObject.get("texWidth").getAsInt());
        part.setTexHeight(partObject.get("texHeight").getAsInt());
        part.setMatchProject(partObject.get("matchProject").getAsBoolean());
        part.setMirror(partObject.get("mirror").getAsBoolean());
        part.setShowModel(partObject.get("showModel").getAsBoolean());
        part.setRotPX(partObject.get("rotPX").getAsFloat());
        part.setRotPY(partObject.get("rotPY").getAsFloat());
        part.setRotPZ(partObject.get("rotPZ").getAsFloat());
        part.setRotAX(partObject.get("rotAX").getAsFloat());
        part.setRotAY(partObject.get("rotAY").getAsFloat());
        part.setRotAZ(partObject.get("rotAZ").getAsFloat());
        part.setIdentifier(partObject.has("identifier") ? partObject.get("identifier").getAsString() : "");
        part.setName(partObject.has("name") ? partObject.get("name").getAsString() : "");

        // Parsing das caixas (boxes)
        JsonArray boxesArray = partObject.getAsJsonArray("boxes");
        if (boxesArray != null) {
            for (JsonElement boxElement : boxesArray) {
                Model3DInfo.Box box = parseBox(boxElement.getAsJsonObject());
                part.getBoxes().add(box);
            }
        }

        // Parsing das children
        JsonArray childrenArray = partObject.getAsJsonArray("children");
        if (childrenArray != null) {
            for (JsonElement childElement : childrenArray) {
                Model3DInfo.Part childPart = parsePart(childElement.getAsJsonObject());
                part.getChildren().add(childPart);
            }
        }

        return part;
    }

    private static Model3DInfo.Box parseBox(JsonObject boxObject) {
        Model3DInfo.Box box = new Model3DInfo.Box();

        // Usando setters para definir os valores
        box.setPosX(boxObject.get("posX").getAsFloat());
        box.setPosY(boxObject.get("posY").getAsFloat());
        box.setPosZ(boxObject.get("posZ").getAsFloat());
        box.setMeshVertices(boxObject.getAsJsonObject("meshVertices"));
        box.setMeshFaces(boxObject.getAsJsonObject("meshFaces"));
        box.setIdentifier(boxObject.has("identifier") ? boxObject.get("identifier").getAsString() : "");
        box.setName(boxObject.has("name") ? boxObject.get("name").getAsString() : "");


        return box;
    }
}
