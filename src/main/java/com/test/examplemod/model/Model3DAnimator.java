package com.test.examplemod.model;

public class Model3DAnimator {
    private final Model3DInfo modelInfo;

    public Model3DAnimator(Model3DInfo modelInfo) {
        this.modelInfo = modelInfo;
    }

    // Método para atualizar a rotação de uma parte específica
    public void updateRotation(String partName, float rotPX, float rotPY, float rotPZ, float rotAX, float rotAY, float rotAZ) {
        Model3DInfo.Part part = findPartByName(partName);
        if (part != null) {
            part.setRotPX(rotPX);
            part.setRotPY(rotPY);
            part.setRotPZ(rotPZ);
            part.setRotAX(rotAX);
            part.setRotAY(rotAY);
            part.setRotAZ(rotAZ);
        } else {
            System.out.println("Parte " + partName + " não encontrada.");
        }
    }

    // Método para encontrar uma parte pelo nome
    private Model3DInfo.Part findPartByName(String name) {
        for (Model3DInfo.Part part : modelInfo.getParts()) {
            Model3DInfo.Part foundPart = findPartByNameRecursive(part, name);
            if (foundPart != null) {
                return foundPart;
            }
        }
        return null;
    }

    // Método recursivo para procurar em partes aninhadas
    private Model3DInfo.Part findPartByNameRecursive(Model3DInfo.Part part, String name) {
        if (part.getName().equals(name)) {
            return part;
        }

        for (Model3DInfo.Part child : part.getChildren()) {
            Model3DInfo.Part foundPart = findPartByNameRecursive(child, name);
            if (foundPart != null) {
                return foundPart;
            }
        }

        return null;
    }


}
