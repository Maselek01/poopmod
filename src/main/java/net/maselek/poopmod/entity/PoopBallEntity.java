package net.maselek.poopmod.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.projectile.thrown.EggEntity;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.particle.ItemStackParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class PoopBallEntity extends ThrownItemEntity {
    public PoopBallEntity(EntityType<? extends EggEntity> entityType, World world) {
        super(entityType, world);
    }

    public PoopBallEntity(World world, LivingEntity owner) {
        super(EntityType.EGG, owner, world);
    }

    public PoopBallEntity(World world, double x, double y, double z) {
        super(EntityType.EGG, x, y, z, world);
    }

    public void handleStatus(byte status) {
        if (status == 3) {
            double d = 0.08;

            for(int i = 0; i < 8; ++i) {
                this.world.addParticle(new ItemStackParticleEffect(ParticleTypes.ITEM, this.getStack()), this.getX(), this.getY(), this.getZ(), ((double)this.random.nextFloat() - 0.5) * 0.08, ((double)this.random.nextFloat() - 0.5) * 0.08, ((double)this.random.nextFloat() - 0.5) * 0.08);
            }
        }

    }

    protected void onEntityHit(EntityHitResult entityHitResult) {
        super.onEntityHit(entityHitResult);
        entityHitResult.getEntity().damage(DamageSource.thrownProjectile(this, this.getOwner()), 0.0F);
        entityHitResult.getEntity().slowMovement(getBlockStateAtPos(), new Vec3d(0.2, 0.2, 0.2));
        if (entityHitResult.getEntity() instanceof LivingEntity livingEntity) {
            livingEntity.addStatusEffect((new StatusEffectInstance(StatusEffects.SLOWNESS, 60, 1)));
        }
    }

    protected void onCollision(HitResult hitResult) {
        super.onCollision(hitResult);
        if (!this.world.isClient) {
            this.world.sendEntityStatus(this, (byte)3);
            this.discard();
        }

    }

    protected Item getDefaultItem() {
        return Items.EGG;
    }
}
