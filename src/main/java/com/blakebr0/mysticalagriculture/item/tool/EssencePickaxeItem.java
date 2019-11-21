package com.blakebr0.mysticalagriculture.item.tool;

import com.blakebr0.cucumber.item.tool.BasePickaxeItem;
import com.blakebr0.mysticalagriculture.api.tinkering.AugmentType;
import com.blakebr0.mysticalagriculture.api.tinkering.IAugment;
import com.blakebr0.mysticalagriculture.api.tinkering.ITinkerableTool;
import com.blakebr0.mysticalagriculture.api.util.AugmentUtils;
import net.minecraft.block.BlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.IItemTier;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.EnumSet;
import java.util.List;
import java.util.function.Function;

public class EssencePickaxeItem extends BasePickaxeItem implements ITinkerableTool {
    private static final EnumSet<AugmentType> TYPES = EnumSet.of(AugmentType.TOOL, AugmentType.PICKAXE);
    private final int toolTier;
    private final int slots;

    public EssencePickaxeItem(IItemTier tier, int toolTier, int slots, Function<Properties, Properties> properties) {
        super(tier, properties);
        this.toolTier = toolTier;
        this.slots = slots;
    }

    @Override
    public ActionResultType onItemUse(ItemUseContext context) {
        List<IAugment> augments = AugmentUtils.getAugments(context.getItem());
        boolean success = false;
        for (IAugment augment : augments) {
            if (augment.onItemUse(context))
                success = true;
        }

        if (success)
            return ActionResultType.SUCCESS;

        return ActionResultType.PASS;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand) {
        ItemStack stack = player.getHeldItem(hand);
        List<IAugment> augments = AugmentUtils.getAugments(stack);
        boolean success = false;
        for (IAugment augment : augments) {
            if (augment.onRightClick(stack, world, player, hand))
                success = true;
        }

        if (success)
            return new ActionResult<>(ActionResultType.SUCCESS, stack);

        return new ActionResult<>(ActionResultType.PASS, stack);
    }

    @Override
    public boolean itemInteractionForEntity(ItemStack stack, PlayerEntity player, LivingEntity target, Hand hand) {
        List<IAugment> augments = AugmentUtils.getAugments(stack);
        boolean success = false;
        for (IAugment augment : augments) {
            if (augment.onRightClickEntity(stack, player, target, hand))
                success = true;
        }

        return success;
    }

    @Override
    public boolean hitEntity(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        List<IAugment> augments = AugmentUtils.getAugments(stack);
        boolean success = false;
        for (IAugment augment : augments) {
            if (augment.onHitEntity(stack, target, attacker))
                success = true;
        }

        return success;
    }

    @Override
    public boolean onBlockDestroyed(ItemStack stack, World world, BlockState state, BlockPos pos, LivingEntity entity) {
        List<IAugment> augments = AugmentUtils.getAugments(stack);
        boolean success = false;
        for (IAugment augment : augments) {
            if (augment.onBlockDestroyed(stack, world, state, pos, entity))
                success = true;
        }

        return success;
    }

    @Override
    public boolean onBlockStartBreak(ItemStack stack, BlockPos pos, PlayerEntity player) {
        List<IAugment> augments = AugmentUtils.getAugments(stack);
        boolean success = false;
        for (IAugment augment : augments) {
            if (augment.onBlockStartBreak(stack, pos, player))
                success = true;
        }

        return success;
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean isSelected) {
        AugmentUtils.getAugments(stack).forEach(a -> a.onInventoryTick(stack, world, entity, slot, isSelected));
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void addInformation(ItemStack stack, World world, List<ITextComponent> tooltip, ITooltipFlag flag) {
        AugmentUtils.getAugments(stack).forEach(a -> {
            tooltip.add(a.getDisplayName());
        });
    }

    @Override
    public int getAugmentSlots() {
        return this.slots;
    }

    @Override
    public EnumSet<AugmentType> getAugmentTypes() {
        return TYPES;
    }

    @Override
    public int getToolTier() {
        return this.toolTier;
    }
}
