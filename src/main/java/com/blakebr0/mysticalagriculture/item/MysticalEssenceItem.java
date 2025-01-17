package com.blakebr0.mysticalagriculture.item;

import com.blakebr0.cucumber.item.BaseItem;
import com.blakebr0.cucumber.util.Localizable;
import com.blakebr0.mysticalagriculture.MysticalAgriculture;
import com.blakebr0.mysticalagriculture.api.crop.Crop;
import com.blakebr0.mysticalagriculture.api.crop.ICropProvider;
import com.blakebr0.mysticalagriculture.lib.ModTooltips;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

public class MysticalEssenceItem extends BaseItem implements ICropProvider {
    private final Crop crop;

    public MysticalEssenceItem(Crop crop) {
        super();
        this.crop = crop;
    }

    @Override
    public Component getName(ItemStack stack) {
        return Localizable.of("item.mysticalagriculture.mystical_essence").args(this.crop.getDisplayName()).build();
    }

    @Override
    public Component getDescription() {
        return this.getName(ItemStack.EMPTY);
    }

    @Override
    public String getDescriptionId() {
        return Localizable.of("item.mysticalagriculture.mystical_essence").args(this.crop.getDisplayName()).buildString();
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return this.crop.hasEffect(stack) || super.isFoil(stack);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        if (!this.crop.getModId().equals(MysticalAgriculture.MOD_ID)) {
            tooltip.add(ModTooltips.getAddedByTooltip(this.crop.getModId()));
        }
    }

    @Override
    public Crop getCrop() {
        return this.crop;
    }
}
