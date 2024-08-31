package com.test.examplemod.util;

import com.test.examplemod.gui.ModelScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;

public class ClientOperations {

    private static Player player = Minecraft.getInstance().player;

    public static void displayGui(){
        Item item = player.getMainHandItem().getItem();
        Minecraft.getInstance().setScreen(new ModelScreen(Component.nullToEmpty("kek")));
    }
}
