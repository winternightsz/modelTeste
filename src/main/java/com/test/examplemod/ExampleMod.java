package com.test.examplemod;

import com.mojang.logging.LogUtils;
import com.test.examplemod.model.Model3DInfo;
import com.test.examplemod.model.ParserEBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.packs.resources.ReloadableResourceManager;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.slf4j.Logger;

import java.io.IOException;
import java.nio.file.Path;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(ExampleMod.MODID)
public class ExampleMod
{
    // Define mod id in a common place for everything to reference
    public static final String MODID = "examplemod";
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();
    // Create a Deferred Register to hold Blocks which will all be registered under the "examplemod" namespace
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);
    // Create a Deferred Register to hold Items which will all be registered under the "examplemod" namespace
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);
    // Create a Deferred Register to hold CreativeModeTabs which will all be registered under the "examplemod" namespace
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);

    // Creates a new Block with the id "examplemod:example_block", combining the namespace and path
    public static final RegistryObject<Block> EXAMPLE_BLOCK = BLOCKS.register("example_blocki", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.STONE)));
    // Creates a new BlockItem with the id "examplemod:example_block", combining the namespace and path
    public static final RegistryObject<Item> EXAMPLE_BLOCK_ITEM = ITEMS.register("example_blockii", () -> new BlockItem(EXAMPLE_BLOCK.get(), new Item.Properties()));

    // Creates a new food item with the id "examplemod:example_id", nutrition 1 and saturation 2
    public static final RegistryObject<Item> EXAMPLE_ITEM = ITEMS.register("example_item", () -> new Item(new Item.Properties().food(new FoodProperties.Builder()
            .alwaysEat().nutrition(1).saturationMod(2f).build())));

    // Creates a creative tab with the id "examplemod:example_tab" for the example item, that is placed after the combat tab
    public static final RegistryObject<CreativeModeTab> EXAMPLE_TAB = CREATIVE_MODE_TABS.register("example_tab", () -> CreativeModeTab.builder()
            .withTabsBefore(CreativeModeTabs.COMBAT)
            .icon(() -> EXAMPLE_ITEM.get().getDefaultInstance())
            .displayItems((parameters, output) -> {
                output.accept(EXAMPLE_ITEM.get()); // Add the example item to the tab. For your own tabs, this method is preferred over the event
            }).build());

    public ExampleMod()
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);

        ModItems.register(modEventBus);
        // Register the Deferred Register to the mod event bus so blocks get registered
        BLOCKS.register(modEventBus);
        // Register the Deferred Register to the mod event bus so items get registered
        ITEMS.register(modEventBus);
        // Register the Deferred Register to the mod event bus so tabs get registered
        CREATIVE_MODE_TABS.register(modEventBus);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

        // Register the item to a creative tab
        modEventBus.addListener(this::addCreative);

        // Register our mod's ForgeConfigSpec so that Forge can create and load the config file for us
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);
        // Registrar itens


        // Adicionar itens às abas criativas após o registro
        modEventBus.addListener(this::commonSetup);

    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
        // Some common setup code
        LOGGER.info("HELLO FROM COMMON SETUP");

        if (Config.logDirtBlock)
            LOGGER.info("DIRT BLOCK >> {}", ForgeRegistries.BLOCKS.getKey(Blocks.DIRT));

        LOGGER.info(Config.magicNumberIntroduction + Config.magicNumber);

        Config.items.forEach((item) -> LOGGER.info("ITEM >> {}", item.toString()));

        try {
            loadAndParseModel();
        } catch (IOException e) {
            LOGGER.error("Failed to load model file", e);
        }

    }

    private void loadAndParseModel() throws IOException {
        String modelFilePath = "G:\\GitHub\\modelTeste\\src\\main\\resources\\assets\\examplemod\\model\\model.tfm";
        Path modelPath = Path.of(modelFilePath);

        ParserEBuilder parser = new ParserEBuilder();
        Model3DInfo modelInfo = parser.parseModel(modelPath);

        // Log das principais informações do modelo
        LOGGER.info("Model Author: {}", modelInfo.getAuthor());
        LOGGER.info("Model Version: {}", modelInfo.getVersion());
        LOGGER.info("Texture File: {}", modelInfo.getTextureFile());
        LOGGER.info("Texture Dimensions: {}x{}", modelInfo.getTexWidth(), modelInfo.getTexHeight());
        LOGGER.info("Scale: X={}, Y={}, Z={}", modelInfo.getScaleX(), modelInfo.getScaleY(), modelInfo.getScaleZ());

        // Log de todas as partes do modelo
        for (Model3DInfo.Part part : modelInfo.getParts()) {
            logPartInfo(part, 1);
        }
    }

    private void logPartInfo(Model3DInfo.Part part, int depth) {
        String indent = " ".repeat(depth * 2);

        LOGGER.info("{}Part Name: {}", indent, part.getName());
        LOGGER.info("{}Identifier: {}", indent, part.getIdentifier());
        LOGGER.info("{}Rotation (P): X={}, Y={}, Z={}", indent, part.getRotPX(), part.getRotPY(), part.getRotPZ());
        LOGGER.info("{}Rotation (A): X={}, Y={}, Z={}", indent, part.getRotAX(), part.getRotAY(), part.getRotAZ());
        LOGGER.info("{}Texture Offset: X={}, Y={}", indent, part.getTexOffX(), part.getTexOffY());
        LOGGER.info("{}Texture Dimensions: {}x{}", indent, part.getTexWidth(), part.getTexHeight());
        LOGGER.info("{}Mirror: {}", indent, part.isMirror());
        LOGGER.info("{}Show Model: {}", indent, part.isShowModel());

        // Log das caixas (boxes)
        for (Model3DInfo.Box box : part.getBoxes()) {
            LOGGER.info("{}  Box Name: {}", indent, box.getName());
            LOGGER.info("{}  Position: X={}, Y={}, Z={}", indent, box.getPosX(), box.getPosY(), box.getPosZ());
            LOGGER.info("{}  Mesh Vertices: {}", indent, box.getMeshVertices().toString());
            LOGGER.info("{}  Mesh Faces: {}", indent, box.getMeshFaces().toString());
        }

        // Log das partes filhas (children)
        for (Model3DInfo.Part child : part.getChildren()) {
            logPartInfo(child, depth + 1);
        }
    }


    // Add the example block item to the building blocks tab
    private void addCreative(BuildCreativeModeTabContentsEvent event)
    {
        if (event.getTabKey() == CreativeModeTabs.BUILDING_BLOCKS)
            event.accept(EXAMPLE_BLOCK_ITEM);
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {
        // Do something when the server starts
        LOGGER.info("HELLO from server starting");
    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {


        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
            // Some client setup code
            LOGGER.info("HELLO FROM CLIENT SETUP");
            LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());
        }
    }
}
