package tfar.deathabilities.data;

import com.google.gson.JsonElement;
import net.minecraft.data.models.BlockModelGenerators;
import net.minecraft.data.models.blockstates.BlockStateGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import tfar.deathabilities.init.ModBlocks;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class ModBlockModelProvider extends BlockModelGenerators {

    public ModBlockModelProvider(Consumer<BlockStateGenerator> pBlockStateOutput, BiConsumer<ResourceLocation, Supplier<JsonElement>> pModelOutput, Consumer<Item> pSkippedAutoModelsOutput) {
        super(pBlockStateOutput, pModelOutput, pSkippedAutoModelsOutput);
    }

    @Override
    public void run() {
        createTrivialCube(ModBlocks.QUICKSAND);
    }

   /* public void createSpeedrunnerSculkSensor(Block block) {
        ResourceLocation resourcelocation = ModelLocationUtils.getModelLocation(block, "_inactive");
        ResourceLocation resourcelocation1 = ModelLocationUtils.getModelLocation(block, "_active");
        this.delegateItemModel(block, resourcelocation);
        this.blockStateOutput.accept(MultiVariantGenerator.multiVariant(block)
                .with(PropertyDispatch.property(BlockStateProperties.SCULK_SENSOR_PHASE).generate((p_284650_) -> Variant.variant().with(VariantProperties.MODEL, p_284650_ != SculkSensorPhase.ACTIVE && p_284650_ != SculkSensorPhase.COOLDOWN ? resourcelocation : resourcelocation1))));
    }

    public void createBlankSpeedrunnerSculkSensor() {
        ResourceLocation resourcelocation = ModelLocationUtils.getModelLocation(Blocks.SCULK_SENSOR, "_inactive");
        ResourceLocation resourcelocation1 = ModelLocationUtils.getModelLocation(Blocks.SCULK_SENSOR, "_active");
        this.delegateItemModel(Init.SPEEDRUNNER_SCULK_SENSOR_BLANK, resourcelocation);
        this.blockStateOutput.accept(MultiVariantGenerator.multiVariant(Init.SPEEDRUNNER_SCULK_SENSOR_BLANK)
                .with(PropertyDispatch.property(BlockStateProperties.SCULK_SENSOR_PHASE).generate((p_284650_) -> Variant.variant().with(VariantProperties.MODEL, p_284650_ != SculkSensorPhase.ACTIVE && p_284650_ != SculkSensorPhase.COOLDOWN ? resourcelocation : resourcelocation1))));
    }*/

}
