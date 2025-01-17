package com.blakebr0.mysticalagriculture.compat.jei;

import com.blakebr0.cucumber.helper.RecipeHelper;
import com.blakebr0.mysticalagriculture.MysticalAgriculture;
import com.blakebr0.mysticalagriculture.api.util.MobSoulUtils;
import com.blakebr0.mysticalagriculture.client.screen.EnchanterScreen;
import com.blakebr0.mysticalagriculture.client.screen.EssenceFurnaceScreen;
import com.blakebr0.mysticalagriculture.client.screen.ReprocessorScreen;
import com.blakebr0.mysticalagriculture.client.screen.SoulExtractorScreen;
import com.blakebr0.mysticalagriculture.client.screen.SouliumSpawnerScreen;
import com.blakebr0.mysticalagriculture.compat.jei.category.AwakeningCategory;
import com.blakebr0.mysticalagriculture.compat.jei.category.CruxCategory;
import com.blakebr0.mysticalagriculture.compat.jei.category.EnchanterCategory;
import com.blakebr0.mysticalagriculture.compat.jei.category.InfusionCategory;
import com.blakebr0.mysticalagriculture.compat.jei.category.ReprocessorCategory;
import com.blakebr0.mysticalagriculture.compat.jei.category.SoulExtractorCategory;
import com.blakebr0.mysticalagriculture.compat.jei.category.SouliumSpawnerCategory;
import com.blakebr0.mysticalagriculture.init.ModBlocks;
import com.blakebr0.mysticalagriculture.init.ModItems;
import com.blakebr0.mysticalagriculture.init.ModRecipeTypes;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.ISubtypeRegistration;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

@JeiPlugin
public final class JeiCompat implements IModPlugin {
    public static final ResourceLocation UID = MysticalAgriculture.resource("jei_plugin");

    @Override
    public ResourceLocation getPluginUid() {
        return UID;
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        var guiHelper = registration.getJeiHelpers().getGuiHelper();

        registration.addRecipeCategories(
                new InfusionCategory(guiHelper),
                new AwakeningCategory(guiHelper),
                new EnchanterCategory(guiHelper),
                new ReprocessorCategory(guiHelper),
                new SoulExtractorCategory(guiHelper),
                new SouliumSpawnerCategory(guiHelper),
                new CruxCategory(guiHelper)
        );
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.INFUSION_ALTAR.get()), InfusionCategory.RECIPE_TYPE);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.INFUSION_PEDESTAL.get()), InfusionCategory.RECIPE_TYPE);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.AWAKENING_ALTAR.get()), AwakeningCategory.RECIPE_TYPE);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.AWAKENING_PEDESTAL.get()), AwakeningCategory.RECIPE_TYPE);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.ESSENCE_VESSEL.get()), AwakeningCategory.RECIPE_TYPE);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.ENCHANTER.get()), EnchanterCategory.RECIPE_TYPE);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.FURNACE.get()), mezz.jei.api.constants.RecipeTypes.SMELTING);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.REPROCESSOR.get()), ReprocessorCategory.RECIPE_TYPE);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.SOUL_EXTRACTOR.get()), SoulExtractorCategory.RECIPE_TYPE);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.SOULIUM_SPAWNER.get()), SouliumSpawnerCategory.RECIPE_TYPE);
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        registration.addRecipes(InfusionCategory.RECIPE_TYPE, RecipeHelper.byTypeValues(ModRecipeTypes.INFUSION.get()));
        registration.addRecipes(AwakeningCategory.RECIPE_TYPE, RecipeHelper.byTypeValues(ModRecipeTypes.AWAKENING.get()));
        registration.addRecipes(EnchanterCategory.RECIPE_TYPE, RecipeHelper.byTypeValues(ModRecipeTypes.ENCHANTER.get()));
        registration.addRecipes(ReprocessorCategory.RECIPE_TYPE, RecipeHelper.byTypeValues(ModRecipeTypes.REPROCESSOR.get()));
        registration.addRecipes(SoulExtractorCategory.RECIPE_TYPE, RecipeHelper.byTypeValues(ModRecipeTypes.SOUL_EXTRACTION.get()));
        registration.addRecipes(SouliumSpawnerCategory.RECIPE_TYPE, RecipeHelper.byTypeValues(ModRecipeTypes.SOULIUM_SPAWNER.get()));
        registration.addRecipes(CruxCategory.RECIPE_TYPE, CruxRecipe.getGeneratedRecipes());

        registration.addIngredientInfo(
                new ItemStack(ModItems.COGNIZANT_DUST.get()),
                VanillaTypes.ITEM_STACK,
                Component.translatable("jei.desc.mysticalagriculture.cognizant_dust")
        );
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        registration.addRecipeClickArea(EnchanterScreen.class, 104, 41, 22, 15, EnchanterCategory.RECIPE_TYPE);
        registration.addRecipeClickArea(EssenceFurnaceScreen.class, 99, 52, 22, 15, mezz.jei.api.constants.RecipeTypes.SMELTING);
        registration.addRecipeClickArea(ReprocessorScreen.class, 99, 52, 22, 15, ReprocessorCategory.RECIPE_TYPE);
        registration.addRecipeClickArea(SoulExtractorScreen.class, 99, 52, 22, 15, SoulExtractorCategory.RECIPE_TYPE);
        registration.addRecipeClickArea(SouliumSpawnerScreen.class, 99, 52, 22, 15, SouliumSpawnerCategory.RECIPE_TYPE);
    }

    @Override
    public void registerItemSubtypes(ISubtypeRegistration registration) {
        registration.registerSubtypeInterpreter(VanillaTypes.ITEM_STACK, ModItems.SOUL_JAR.get(), (stack, context) -> {
            var type = MobSoulUtils.getType(stack);
            return type != null ? type.getEntityIds().toString() : "";
        });
    }
}
