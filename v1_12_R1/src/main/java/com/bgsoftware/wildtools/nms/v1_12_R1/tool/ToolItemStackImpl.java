package com.bgsoftware.wildtools.nms.v1_12_R1.tool;

import com.bgsoftware.wildtools.utils.items.ToolItemStack;
import net.minecraft.server.v1_12_R1.EntityPlayer;
import net.minecraft.server.v1_12_R1.ItemStack;
import net.minecraft.server.v1_12_R1.NBTTagCompound;
import net.minecraft.server.v1_12_R1.StatisticList;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_12_R1.event.CraftEventFactory;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;

public class ToolItemStackImpl extends ToolItemStack {

    private final ItemStack nmsItem;

    public ToolItemStackImpl(ItemStack nmsItem) {
        this.nmsItem = nmsItem;
        this.setItem(CraftItemStack.asCraftMirror(nmsItem));
    }

    @Override
    public ToolItemStack copy() {
        return new ToolItemStackImpl(nmsItem.cloneItemStack());
    }

    @Override
    public void setTag(String key, int value) {
        getOrCreateTagInternal().setInt(key, value);
    }

    @Override
    public void setTag(String key, String value) {
        getOrCreateTagInternal().setString(key, value);
    }

    @Override
    public int getTag(String key, int def) {
        NBTTagCompound tagCompound = this.nmsItem.getTag();
        return tagCompound == null || !tagCompound.hasKeyOfType(key, 3) ? def : tagCompound.getInt(key);
    }

    @Override
    public String getTag(String key, String def) {
        NBTTagCompound tagCompound = this.nmsItem.getTag();
        return tagCompound == null || !tagCompound.hasKeyOfType(key, 8) ? def : tagCompound.getString(key);
    }

    @Override
    public void breakTool(Player player) {
        EntityPlayer entityPlayer = ((CraftPlayer) player).getHandle();

        entityPlayer.b(this.nmsItem);

        if (this.nmsItem.getCount() == 1)
            CraftEventFactory.callPlayerItemBreakEvent(entityPlayer, this.nmsItem);

        this.nmsItem.subtract(1);

        entityPlayer.b(StatisticList.c(this.nmsItem.getItem()));

        this.nmsItem.setData(0);
    }

    private NBTTagCompound getOrCreateTagInternal() {
        NBTTagCompound tagCompound = this.nmsItem.getTag();

        if (tagCompound == null) {
            tagCompound = new NBTTagCompound();
            this.nmsItem.setTag(tagCompound);
        }

        return tagCompound;
    }

}
