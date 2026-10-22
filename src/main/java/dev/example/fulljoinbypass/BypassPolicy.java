package dev.example.fulljoinbypass;

import java.util.UUID;

final class BypassPolicy {

    private BypassPolicy() {
    }

    static boolean shouldGrant(UUID playerId, String playerName, boolean hasPermission) {
        return playerId != null && playerName != null && hasPermission;
    }
}
