package com.test.examplemod.util;

import com.test.examplemod.ExampleMod;
import com.test.examplemod.model.Model3DInfo;
import com.test.examplemod.model.ParserEBuilder;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterClientReloadListenersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD, modid = ExampleMod.MODID)
public enum CustomResourceListener implements ResourceManagerReloadListener {

    INSTANCE;

    public static Model3DInfo modelInfo;
    String modelFilePath = "G:\\GitHub\\modelTeste\\src\\main\\resources\\assets\\examplemod\\model\\model_test.tfm";
    Path modelPath = Path.of(modelFilePath);
    ParserEBuilder parser;

    @SubscribeEvent
    public static void onRegisterClientReloadListeners(RegisterClientReloadListenersEvent event)
    {
        event.registerReloadListener(INSTANCE);
    }

    @Override
    public void onResourceManagerReload(ResourceManager p_10758_) {
        modelMaker();
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

}
