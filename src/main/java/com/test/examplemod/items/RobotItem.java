package com.test.examplemod.items;

import com.test.examplemod.gui.ModelScreen;
import com.test.examplemod.model.Model3DInfo;
import com.test.examplemod.model.ParserEBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;

public class RobotItem extends Item {

    private static final Logger LOGGER = LoggerFactory.getLogger(RobotItem.class);
    public static int stackSize;

    public RobotItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
//        if (!level.isClientSide) {
//            return InteractionResultHolder.pass(player.getItemInHand(hand));
//        }
        ItemStack itemstack = player.getItemInHand(hand);
        try {

            String modelFilePath = "C:\\Users\\pc\\Documents\\IntelliJTestProjects\\ParserTest\\src\\main\\resources\\assets\\examplemod\\model\\model.tfm";
            Path modelPath = Path.of(modelFilePath);

            LOGGER.info("Carregando modelo 3D a partir de: {}", modelFilePath);

            // Parse o modelo 3D
            ParserEBuilder parser = new ParserEBuilder();
            Model3DInfo modelInfo = parser.parseModel(modelPath);


            if (modelInfo.getParts().isEmpty()) {
                LOGGER.error("Erro: Nenhuma parte foi carregada do modelo 3D.");
            } else {
                LOGGER.info("Modelo 3D carregado com sucesso: {} partes encontradas.", modelInfo.getParts().size());
            }

            player.playSound(SoundEvents.ANVIL_USE, 1.0F, 1.0F);

            // Abra a tela para renderizar o modelo 3D
            Minecraft.getInstance().setScreen(new ModelScreen(Component.literal("Robot Model"), modelInfo));


        } catch (Exception e) {
            LOGGER.error("Falha ao carregar o modelo 3D", e);
        }

        itemstack.shrink(stackSize);

        return super.use(level, player, hand);
        //return InteractionResultHolder.success(player.getItemInHand(hand));
    }
}
