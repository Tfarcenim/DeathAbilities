package tfar.deathabilities.data;

import com.google.gson.JsonElement;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.models.ItemModelGenerators;
import net.minecraft.data.models.model.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import tfar.deathabilities.DeathAbilities;
import tfar.deathabilities.init.ModItems;

import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class ModItemModelProvider extends ItemModelGenerators {

    public ModItemModelProvider(BiConsumer<ResourceLocation, Supplier<JsonElement>> pOutput) {
        super(pOutput);
    }

    @Override
    public void run() {
        this.generateFlatItems(ModItems.EARTH_SCROLL,ModItems.FIRE_SCROLL,ModItems.LIGHTNING_SCROLL,ModItems.WATER_SCROLL
                );

        generateFlatItem(ModItems.WATER_WAND,FULL_WATER_WAND_TEMPLATE);
    }

    public void makePerspectiveModel(Item item, ModelTemplate template) {
        generateRegularModel(item,template);
        generateSpriteModel(item,ModelTemplates.FLAT_HANDHELD_ITEM);
    }

    public void generateRegularModel(Item pItem, ModelTemplate pModelTemplate) {
        pModelTemplate.create(ModelLocationUtils.getModelLocation(pItem), d3(pItem), this.output);
    }

    public void generateSpriteModel(Item pItem, ModelTemplate pModelTemplate) {
        pModelTemplate.create(getItemSpriteTexture(pItem), sprite(pItem), this.output);
    }

    public static TextureMapping sprite(Item pLayerZeroItem) {
        return new TextureMapping().put(TextureSlot.LAYER0, getItemSpriteTexture(pLayerZeroItem));
    }

    public static TextureMapping d3(Item pLayerZeroItem) {
        return new TextureMapping().put(TextureSlot.LAYER0, getItem3dTexture(pLayerZeroItem));
    }

    public static ResourceLocation getItem3dTexture(Item pItem) {
        ResourceLocation resourcelocation = BuiltInRegistries.ITEM.getKey(pItem);
        return resourcelocation.withPrefix("item/3d/");
    }

    public static ResourceLocation getItemSpriteTexture(Item pItem) {
        ResourceLocation resourcelocation = BuiltInRegistries.ITEM.getKey(pItem);
        return resourcelocation.withPrefix("item/sprite/");
    }
/*
    public void generateSai(Item item) {
        ResourceLocation base = ModelLocationUtils.getModelLocation(item);
        ResourceLocation texture = TextureMapping.getItemTexture(item);
        ModelTemplates.FLAT_HANDHELD_ITEM.create(base, TextureMapping.layer0(texture), this.output,
                this::generateBaseSai);

        BLOCKING_HANDHELD_ITEM.create(base.withSuffix("_blocking"),
                TextureMapping.layer0(texture), this.output);
    }

    public JsonObject generateBaseSai(ResourceLocation pModelLocation, Map<TextureSlot, ResourceLocation> template) {
        JsonObject jsonobject = ModelTemplates.FLAT_HANDHELD_ITEM.createBaseTemplate(pModelLocation, template);
        JsonArray jsonarray = new JsonArray();

        JsonObject overrideJson = new JsonObject();
        JsonObject predicateJson = new JsonObject();
        predicateJson.addProperty(ClientMisc.BLOCKING_PREDICATE.toString(), 1);
        overrideJson.add("predicate", predicateJson);
        overrideJson.addProperty("model",pModelLocation.withSuffix("_blocking").toString());
        jsonarray.add(overrideJson);

        jsonobject.add("overrides", jsonarray);
        return jsonobject;
    }

    public void generateCutlass(Item item) {
        ResourceLocation base = ModelLocationUtils.getModelLocation(item);
        ResourceLocation texture = TextureMapping.getItemTexture(item);
        ModelTemplates.FLAT_HANDHELD_ITEM.create(base, TextureMapping.layer0(texture), this.output,
                this::generateBaseCutlass);

        BLOCKING_HANDHELD_ITEM.create(base.withSuffix("_blocking"),
                TextureMapping.layer0(texture), this.output);
    }

    public JsonObject generateBaseCutlass(ResourceLocation pModelLocation, Map<TextureSlot, ResourceLocation> template) {
        JsonObject jsonobject = ModelTemplates.FLAT_HANDHELD_ITEM.createBaseTemplate(pModelLocation, template);
        JsonArray jsonarray = new JsonArray();

        JsonObject overrideJson = new JsonObject();
        JsonObject predicateJson = new JsonObject();
        predicateJson.addProperty(ClientMisc.BLOCKING_PREDICATE.toString(), 1);
        overrideJson.add("predicate", predicateJson);
        overrideJson.addProperty("model",pModelLocation.withSuffix("_blocking").toString());
        jsonarray.add(overrideJson);

        jsonobject.add("overrides", jsonarray);
        return jsonobject;
    }

    public void generateKusarigama(KusarigamaItem kusarigamaItem) {
        ResourceLocation resourcelocation = ModelLocationUtils.getModelLocation(kusarigamaItem);
        ResourceLocation resourcelocation1 = TextureMapping.getItemTexture(kusarigamaItem);
        TWO_LAYERED_HANDHELD_ITEM.create(resourcelocation, TextureMapping.layered(resourcelocation1,
                        new ResourceLocation(WarSmith.MOD_ID,"item/kusarigama_chain")), this.output,
                this::generateBaseKusarigama);

        ModelTemplates.FLAT_HANDHELD_ITEM.create(ModelLocationUtils.getModelLocation(kusarigamaItem).withSuffix("_cast"),
                TextureMapping.layer0(kusarigamaItem), this.output);
    }

    public JsonObject generateBaseKusarigama(ResourceLocation pModelLocation, Map<TextureSlot, ResourceLocation> template) {
        JsonObject jsonobject = TWO_LAYERED_HANDHELD_ITEM.createBaseTemplate(pModelLocation, template);
        JsonArray jsonarray = new JsonArray();

            JsonObject overrideJson = new JsonObject();
            JsonObject predicateJson = new JsonObject();
            predicateJson.addProperty(ClientMisc.CAST_PREDICATE.toString(), 1);
            overrideJson.add("predicate", predicateJson);
            overrideJson.addProperty("model",pModelLocation.withSuffix("_cast").toString());
            jsonarray.add(overrideJson);

        jsonobject.add("overrides", jsonarray);
        return jsonobject;
    }*/

    public void generateFlatItems(Item... items) {
        for (Item item : items) {
            generateFlatItem(item,ModelTemplates.FLAT_ITEM);
        }
    }

    public void generateFlatHandheldItems(Item... items) {
        for (Item item : items) {
            generateFlatItem(item,ModelTemplates.FLAT_HANDHELD_ITEM);
        }
    }

  /*  public static final ModelTemplate MACE = createItem(WarSmith.MOD_ID,"mace", TextureSlot.LAYER0);
    public static final ModelTemplate BASEBALL_BAT = createItem(WarSmith.MOD_ID,"baseball_bat_3d", TextureSlot.LAYER0);

    public static final ModelTemplate BLOCKING_HANDHELD_ITEM = createItem(WarSmith.MOD_ID,"cutlass_blocking", TextureSlot.LAYER0);

    public static final ModelTemplate LARGE_FLAT_HANDHELD_ITEM = createItem(WarSmith.MOD_ID,"large_handheld", TextureSlot.LAYER0);
    public static final ModelTemplate EXTRA_LARGE_FLAT_HANDHELD_ITEM = createItem(WarSmith.MOD_ID,"extra_large_handheld", TextureSlot.LAYER0);

*/
    public static final ModelTemplate FULL_WATER_WAND_TEMPLATE = createItem(new ResourceLocation(DeathAbilities.MOD_ID,"full_water_wand"), TextureSlot.LAYER0);

    private static ModelTemplate createItem(String pItemModelLocation, TextureSlot... pRequiredSlots) {
        return createItem(new ResourceLocation(pItemModelLocation),pRequiredSlots);
    }

    private static ModelTemplate createItem(ResourceLocation pItemModelLocation, TextureSlot... pRequiredSlots) {
        return new ModelTemplate(Optional.of(pItemModelLocation.withPrefix("item/")), Optional.empty(), pRequiredSlots);
    }


}
