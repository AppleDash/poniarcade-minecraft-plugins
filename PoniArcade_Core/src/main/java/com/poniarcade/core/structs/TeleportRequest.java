package com.poniarcade.core.structs;

import java.util.UUID;

/**
 * Created by appledash on 7/17/17.
 * Blackjack is best pony.
 */
public record TeleportRequest(UUID otherPlayerId, com.poniarcade.core.structs.TeleportRequest.Direction direction) {
    public enum Direction {
        THEM_TO_OTHER,
        OTHER_TO_THEM
    }
}
