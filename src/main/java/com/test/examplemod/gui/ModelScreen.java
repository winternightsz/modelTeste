package com.test.examplemod.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.test.examplemod.ExampleMod;
import com.test.examplemod.model.Model3DInfo;
import com.test.examplemod.model.ParserEBuilder;
import com.test.examplemod.util.CustomRenderType;
import com.test.examplemod.util.CustomResourceListener;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

import java.nio.file.Path;

@OnlyIn(Dist.CLIENT)
public class ModelScreen extends Screen {

    public static final ResourceLocation TEXTURE = new ResourceLocation(ExampleMod.MODID,"texture/ironhide.png");

    //private Model3DInfo modelInfo;

    private int ticksOpen;

    public ModelScreen(Component title) {
        super(title);
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        //LOGGER.info("Renderizando a GUI com a pirâmide.");

        graphics.pose().pushPose();
        PoseStack.Pose posestack$pose = graphics.pose().last();
        Matrix4f matrix4f = posestack$pose.pose();
        Matrix3f matrix3f = posestack$pose.normal();
        VertexConsumer vertexConsumer = graphics.bufferSource().getBuffer(CustomRenderType.polygon(TEXTURE, false));

        graphics.pose().translate(width / 2+0.25, height / 2 + 50, 400);
        graphics.pose().mulPose(Axis.XP.rotationDegrees(-20));
        graphics.pose().mulPose(Axis.YP.rotationDegrees(ticksOpen*4));
        graphics.pose().scale(2,-2,2);

        //modelInfo = new CustomResourceListener.modelInfo. ;
        //modelMaker();

        CustomResourceListener.modelInfo.renderModelAll(CustomResourceListener.modelInfo, matrix4f, matrix3f, vertexConsumer, 1,1,1,1);

        graphics.pose().popPose();

        super.render(graphics, mouseX, mouseY, partialTicks);
    }

    @Override
    protected void init() {
        super.init();
        //this.minecraft.getNarrator().clear();
    }

    @Override
    public void tick() {
        super.tick();
        ++ticksOpen;
    }

    //private void modelMaker (){
    //
    //    try {
    //        //LOGGER.info("Carregando modelo 3D a partir de: {}", modelFilePath);
    //
    //        // Parse o modelo 3D
    //        this.parser = new ParserEBuilder();
    //        this.modelInfo = this.parser.parseModel(modelPath);
    //
    //
    //        if (modelInfo.getParts().isEmpty()) {
    //            //LOGGER.error("Erro: Nenhuma parte foi carregada do modelo 3D.");
    //        } else {
    //            //LOGGER.info("Modelo 3D carregado com sucesso: {} partes encontradas.", this.modelInfo.getParts().size());
    //        }
    //    } catch (Exception e) {
    //        //LOGGER.error("Falha ao carregar o modelo 3D", e);
    //    }
    //
    //}


    private void renderSimplePyramids(Matrix4f matrix4f, Matrix3f matrix3f, VertexConsumer vertexConsumer) {
        //LOGGER.info("Renderizando triângulo");
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
    private static void renderQuad(Matrix4f matrix4f, Matrix3f matrix3f, VertexConsumer vertexConsumer, float red, float green, float blue, float alpha, int x1, int x2, int x3, int x4, float y1, float y2, float y3, float y4, float z1, float z2, float z3, float z4, float u1, float u2, float u3, float u4, float v1, float v2, float v3, float v4) {
        renderTris(matrix4f, matrix3f, vertexConsumer, 1.0f, 1.0f, 1.0f, 1.0f, x1, x2, x3, y1, y2, y3, z1, z2, z3, u1,u2,u3,v1,v2,v3);
        renderTris(matrix4f, matrix3f, vertexConsumer, 1.0f, 1.0f, 1.0f, 1.0f, x1, x3, x4, y1, y3, y4, z1, z3, z4, u1,u3,u4,v1,v3,v4);
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


