package dev.siea.global;

import dev.siea.storage.Storage;
import org.slf4j.Logger;

public record Universe(Storage storage, Messages messages, Logger logger, GlobalSettings settings) {
}
