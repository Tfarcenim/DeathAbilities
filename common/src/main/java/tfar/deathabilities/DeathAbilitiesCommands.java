package tfar.deathabilities;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.server.level.ServerPlayer;
import tfar.deathabilities.ducks.PlayerDuck;
import tfar.deathabilities.network.S2CActivateItemPacket;
import tfar.deathabilities.network.S2CSetHunterPacket;
import tfar.deathabilities.platform.Services;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Stream;

public class DeathAbilitiesCommands {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal(DeathAbilities.MOD_ID)
                .then(Commands.argument("player", EntityArgument.player())
                        .then(Commands.literal("enable")
                                .then(Commands.argument("ability", StringArgumentType.string()).suggests(DISABLED_EFFECTS)
                                        .executes(DeathAbilitiesCommands::enableAbility)))
                        .then(Commands.literal("disable")
                                .then(Commands.argument("ability", StringArgumentType.string()).suggests(ENABLED_EFFECTS)
                                        .executes(DeathAbilitiesCommands::disableAbility)))
                        .then(Commands.literal("runner")
                                        .executes(DeathAbilitiesCommands::setRunner)))
                );
    }

    private static int setRunner(CommandContext<CommandSourceStack> commandContext) throws CommandSyntaxException {
        ServerPlayer player = EntityArgument.getPlayer(commandContext, "player");
        HunterData.runner = player.getUUID();
        Services.PLATFORM.sendToClient(new S2CSetHunterPacket(true),player);
        return 1;
    }

    private static int enableAbility(CommandContext<CommandSourceStack> commandContext) throws CommandSyntaxException {
        ServerPlayer player = EntityArgument.getPlayer(commandContext, "player");
        try {
            DeathAbility ability = DeathAbility.valueOf(StringArgumentType.getString(commandContext, "ability"));
            if (enableAbility(player, ability)) {
                return 1;
            }
        } catch (Exception e) {
            return 0;
        }
        return 0;
    }

    private static int disableAbility(CommandContext<CommandSourceStack> commandContext) throws CommandSyntaxException {
        ServerPlayer player = EntityArgument.getPlayer(commandContext, "player");
        try {
            DeathAbility ability = DeathAbility.valueOf(StringArgumentType.getString(commandContext, "ability"));
            if (disableAbility(player, ability)) {
                return 1;
            }
        } catch (Exception e) {
            return 0;
        }
        return 0;
    }

    public static boolean disableAbility(ServerPlayer player, DeathAbility ability) {
        PlayerDeathAbilities playerDeathAbilities = PlayerDuck.of(player).getDeathAbilities();
        if (playerDeathAbilities.disable(ability)) {
            //Services.PLATFORM.sendToClient(new S2CActivateItemPacket(ability.icon().getDefaultInstance()), ModPacket.activate_item, player);
            return true;
        }
        return false;
    }

    public static boolean enableAbility(ServerPlayer player, DeathAbility ability) {
        PlayerDeathAbilities playerDeathAbilities = PlayerDuck.of(player).getDeathAbilities();
        if (playerDeathAbilities.enable(ability)) {
            ability.onEnable.accept(player);
            Services.PLATFORM.sendToClient(new S2CActivateItemPacket(ability.icon().getDefaultInstance()),player);
            return true;
        }
        return false;
    }

    private static <R extends Enum<R>> Stream<String> convertEnumsToStrings(Collection<R> enums) {
        return enums.stream().map(Enum::name);
    }

    private static final SuggestionProvider<CommandSourceStack> DISABLED_EFFECTS = (commandContext, suggestionsBuilder) -> {
        ServerPlayer player = commandContext.getSource().getPlayer();
        PlayerDeathAbilities playerDeathAbilities = PlayerDuck.of(player).getDeathAbilities();
        Set<DeathAbility> disabled = playerDeathAbilities.getDisabled();
        return SharedSuggestionProvider.suggest(convertEnumsToStrings(disabled), suggestionsBuilder);
    };

    private static final SuggestionProvider<CommandSourceStack> ENABLED_EFFECTS = (commandContext, suggestionsBuilder) -> {
        ServerPlayer player = commandContext.getSource().getPlayer();
        PlayerDeathAbilities playerDeathAbilities = PlayerDuck.of(player).getDeathAbilities();
        Set<DeathAbility> enabled = playerDeathAbilities.getEnabled();
        return SharedSuggestionProvider.suggest(convertEnumsToStrings(enabled), suggestionsBuilder);
    };

}
