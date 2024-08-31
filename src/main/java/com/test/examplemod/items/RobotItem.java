package com.test.examplemod.items;

import com.test.examplemod.gui.ModelScreen;
import com.test.examplemod.model.Model3DInfo;
import com.test.examplemod.model.ParserEBuilder;
import com.test.examplemod.util.ClientOperations;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
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

            player.playSound(SoundEvents.ANVIL_USE, 1.0F, 1.0F);

        if(level.isClientSide)
            DistExecutor.safeRunWhenOn(Dist.CLIENT, ()-> ClientOperations::displayGui);
        //Minecraft.getInstance().setScreen(new ModelScreen(Component.nullToEmpty("kek")));
        itemstack.shrink(stackSize);

        return super.use(level, player, hand);
        //return InteractionResultHolder.success(player.getItemInHand(hand));
    }
}
