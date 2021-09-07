package com.blakebr0.mysticalagriculture.tileentity;

import com.blakebr0.cucumber.inventory.BaseItemStackHandler;
import com.blakebr0.cucumber.tileentity.BaseInventoryTileEntity;
import com.blakebr0.cucumber.util.Localizable;
import com.blakebr0.mysticalagriculture.api.tinkering.IAugmentProvider;
import com.blakebr0.mysticalagriculture.api.tinkering.ITinkerable;
import com.blakebr0.mysticalagriculture.container.TinkeringTableContainer;
import com.blakebr0.mysticalagriculture.init.ModTileEntities;
import com.blakebr0.mysticalagriculture.lib.ModCrops;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

public class TinkeringTableTileEntity extends BaseInventoryTileEntity implements MenuProvider {
    private final BaseItemStackHandler inventory = new BaseItemStackHandler(7, () -> {
        if (this.getLevel() != null && !this.getLevel().isClientSide()) {
            this.markDirtyAndDispatch();
        }
    });

    public TinkeringTableTileEntity(BlockPos pos, BlockState state) {
        super(ModTileEntities.TINKERING_TABLE.get(), pos, state);
        this.inventory.setDefaultSlotLimit(1);
        this.inventory.setSlotValidator(this::canInsertStack);
    }

    @Override
    public BaseItemStackHandler getInventory() {
        return this.inventory;
    }

    @Override
    public Component getDisplayName() {
        return Localizable.of("container.mysticalagriculture.tinkering_table").build();
    }

    @Override
    public AbstractContainerMenu createMenu(int windowId, Inventory playerInventory, Player player) {
        return TinkeringTableContainer.create(windowId, playerInventory, this::isUsableByPlayer, this.inventory);
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        return LazyOptional.empty();
    }

    private boolean canInsertStack(int slot, ItemStack stack) {
        var item = stack.getItem();
        return switch (slot) {
            case 0 -> item instanceof ITinkerable;
            case 1, 2 -> item instanceof IAugmentProvider;
            case 3 -> item == ModCrops.AIR.getEssenceItem();
            case 4 -> item == ModCrops.EARTH.getEssenceItem();
            case 5 -> item == ModCrops.WATER.getEssenceItem();
            case 6 -> item == ModCrops.FIRE.getEssenceItem();
            default -> true;
        };
    }
}
