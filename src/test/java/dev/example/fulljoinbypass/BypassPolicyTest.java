package dev.example.fulljoinbypass;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.UUID;
import org.junit.jupiter.api.Test;

class BypassPolicyTest {

    private static final UUID PLAYER_ID = UUID.fromString(
            "9b25a1a4-ea18-4e30-9d75-5d86b82ea90d"
    );

    @Test
    void grantsBypassWhenIdentityIsValidAndPermissionIsPresent() {
        assertTrue(BypassPolicy.shouldGrant(PLAYER_ID, "Player", true));
    }

    @Test
    void deniesBypassWhenPermissionIsMissing() {
        assertFalse(BypassPolicy.shouldGrant(PLAYER_ID, "Player", false));
    }

    @Test
    void deniesBypassWhenPlayerIdIsMissing() {
        assertFalse(BypassPolicy.shouldGrant(null, "Player", true));
    }

    @Test
    void deniesBypassWhenPlayerNameIsMissing() {
        assertFalse(BypassPolicy.shouldGrant(PLAYER_ID, null, true));
    }
}
