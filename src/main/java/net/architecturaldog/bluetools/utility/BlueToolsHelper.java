package net.architecturaldog.bluetools.utility;

import org.jetbrains.annotations.ApiStatus.NonExtendable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.architecturaldog.bluetools.BlueTools;
import net.minecraft.util.Identifier;
import net.minecraft.util.InvalidIdentifierException;

@NonExtendable
public interface BlueToolsHelper {

    String NAMESPACE = "bluetools";
    Logger LOGGER = LoggerFactory.getLogger(BlueTools.class);

    static Identifier createIdentifier(final String path) throws InvalidIdentifierException {
        return Identifier.of(BlueToolsHelper.NAMESPACE, path);
    }

    static Logger createLogger(final String name) {
        return LoggerFactory.getLogger("%s/%s".formatted(BlueTools.class.getSimpleName(), name));
    }

}
