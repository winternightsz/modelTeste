package com.test.examplemod.gui;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.test.examplemod.ExampleMod;
import com.test.examplemod.model.Model3DInfo;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class ModelScreen extends Screen {

    private static final Logger LOGGER = LoggerFactory.getLogger(ModelScreen.class);
    private final Model3DInfo modelInfo;
    public static final ResourceLocation TEXTURE = new ResourceLocation(ExampleMod.MODID,"texture/bricks.png");

    public ModelScreen(Component title, Model3DInfo modelInfo) {
        super(title);
        this.modelInfo = modelInfo;
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        LOGGER.info("Renderizando a GUI com a pirâmide.");
        // Setup the matrices
        //Matrix4f matrix4f = new Matrix4f().identity();
        //Matrix3f matrix3f = new Matrix3f().identity();

        graphics.pose().pushPose();
        PoseStack.Pose posestack$pose = graphics.pose().last();
        Matrix4f matrix4f = posestack$pose.pose();
        Matrix3f matrix3f = posestack$pose.normal();

        // Translate the model into view (you can adjust these values as needed)
        //matrix4f.translate(0.0f, 0.0f, -3.0f); // Z negativo para trazer o modelo para a frente da câmera

        // Get the VertexConsumer
//        VertexConsumer vertexConsumer = Minecraft.getInstance().renderBuffers().bufferSource().getBuffer(
//                RenderType.entitySolid(new ResourceLocation("examplemod", "texture/texture2.png"))
//        );

        graphics.renderFakeItem(Items.OAK_LOG.getDefaultInstance(), 0, 0);

       VertexConsumer vertexConsumer = graphics.bufferSource().getBuffer(CustomRenderType.polygon(TEXTURE, false));


        //modelInfo.renderModelAll(modelInfo, matrix4f, matrix3f, graphics.bufferSource().getBuffer(CustomRenderType.polygon(Model3DInfo.TEXTURE, false)), 1, 1, 1, 1);
        //graphics.pose().popPose();
        //renderTris(matrix4f, matrix3f, graphics.bufferSource().getBuffer(CustomRenderType.polygon(TEXTURE,false)), 1.0f, 1.0f, 1.0f, 1.0f, -8, 0, 8, 0, -10, 0, 8, 0, 8, 0,0.5F,1,0,1,0);
        // Render a pyramid
//        graphics.pose().pushPose();
        renderSimplePyramids(matrix4f, matrix3f, vertexConsumer);
        graphics.pose().popPose();

        super.render(graphics, mouseX, mouseY, partialTicks);
    }

    private void renderSimplePyramids(Matrix4f matrix4f, Matrix3f matrix3f, VertexConsumer vertexConsumer) {
        LOGGER.info("Renderizando triângulo");
        // Define os vértices do triângulo
        float x1 = 0.0f, y1 = 0.0f, z1 = 0.0f;
        float x2 = 1.0f, y2 = 1.0f, z2 = 0.0f;
        float x3 = -1.0f, y3 = 1.0f, z3 = 0.0f;

        // Renderiza as linhas do triângulo
        renderTris(matrix4f, matrix3f, vertexConsumer,
                1.0f, 0.0f, 0.0f, 1.0f,  // Vermelho
                x1, x2, x3,               // x1, x2, x3
                y1, y2, y3,               // y1, y2, y3
                z1, z2, z3,               // z1, z2, z3
                0.0f, 0.0f, 0.0f,         // UV não importa aqui
                0.0f, 0.0f, 0.0f
        );
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


