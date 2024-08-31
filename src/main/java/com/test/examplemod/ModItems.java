package com.test.examplemod;

import com.test.examplemod.items.RobotItem;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(modid = "examplemod", bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, "examplemod");
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, "examplemod");

    public static final RegistryObject<Item> ROBOT_ITEM = ITEMS.register("robot_item",
            () -> new RobotItem(new Item.Properties()));

    // Registrar a aba personalizada
    public static final RegistryObject<CreativeModeTab> EXAMPLE_TAB = CREATIVE_MODE_TABS.register("bot_tab", () -> CreativeModeTab.builder()
            .withTabsBefore(CreativeModeTabs.TOOLS_AND_UTILITIES)
            .icon(() -> ROBOT_ITEM.get().getDefaultInstance())
            .title(Component.translatable("Botzinho"))
            .displayItems((parameters, output) -> {
                output.accept(ROBOT_ITEM.get());  // Adiciona o RobotItem à aba personalizada
            }).build());

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
        CREATIVE_MODE_TABS.register(eventBus);  // Registrar as abas criativas
    }
}
