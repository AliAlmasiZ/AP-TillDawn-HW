package io.github.AliAlmasiZ.tillDawn.models.enums;

public enum WeaponType {
    REVOLVER(20, 1, 6, 1000f), // damage, projectilesPerShot, maxAmmo, reloadTimeMillis
    SHOTGUN(10, 4, 2, 1000f),
    DUAL_SMGS(8, 1, 24, 2000f); // Assuming 1 projectile per SMG, but fires fast (handled by player.shootCooldown)

    public final int damage;
    public final int projectilesPerShot;
    public final int maxAmmo;
    public final float reloadTimeMillis; // Reload time in milliseconds

    WeaponType(int damage, int projectilesPerShot, int maxAmmo, float reloadTimeMillis) {
        this.damage = damage;
        this.projectilesPerShot = projectilesPerShot;
        this.maxAmmo = maxAmmo;
        this.reloadTimeMillis = reloadTimeMillis;
    }
}
