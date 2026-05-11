package dominio;

import java.awt.Color;

/**
 * Green Skin.
 * Resistant square; on first contact with an enemy it doesn't die, but loses velocity.
 */
public class GreenState extends PlayerState {

    private boolean hasContacted = false;

    public GreenState() {
        // Normal initial speed, normal size
        super(Color.GREEN, 6.0, 40.0);
    }

    @Override
    public boolean diesOnContact() {
        // If it already contacted an enemy once, the next time it will die
        return hasContacted;
    }

    @Override
    public void handleEnemyContact(Player player) {
        if (!hasContacted) {
            hasContacted = true;
            this.speed = 3.0; // Loses speed on first contact
        }
    }
    
    // Allow resetting the state if the player dies in other ways or level restarts
    public void resetContact() {
        this.hasContacted = false;
        this.speed = 6.0;
    }

    /**
     * Called by WorldHG.playerDies() — no instanceof needed.
     * Resets the contact flag so the next life starts fresh.
     */
    @Override
    public void onPlayerDeath(Player player) {
        resetContact();
    }
}
