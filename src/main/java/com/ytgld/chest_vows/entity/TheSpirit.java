package com.ytgld.chest_vows.entity;

import com.ytgld.chest_item.Handler;
import com.ytgld.chest_item.items.InitItems;
import com.ytgld.chest_item.other.ChestInventory;
import com.ytgld.chest_item.other.DataReg;
import com.ytgld.chest_item.other.SetSoulData;
import com.ytgld.chest_item.renderer.light.Light;
import com.ytgld.chest_vows.render.particle.has_opt.CubeOption;
import com.ytgld.chest_vows.render.particle.has_opt.MagicColorOption;
import com.ytgld.chest_vows.render.particle.other.MagicParticles;
import com.ytgld.chest_vows.sounds.CVSounds;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileDeflection;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TheSpirit extends ItemEntity {
    public boolean canSee = true;
    public final List<Vec3> trailPositions = new ArrayList<>();

    public TheSpirit(EntityType<? extends TheSpirit> type, Level level) {
        super(type, level);
    }
    public TheSpirit(Level level, double x, double y, double z, ItemStack itemStack) {
        this(Entitys.TheSpirit_.get(), level);
        this.setPos(x, y, z);
        this.setItem(itemStack);
        this.setDeltaMovement(this.random.nextDouble() * 0.2 - 0.1, 0.2, this.random.nextDouble() * 0.2 - 0.1);
        this.lifespan = itemStack.getEntityLifespan(level);
        this.level().playSound(null,this.blockPosition(),
                CVSounds.soul_create.value(), SoundSource.PLAYERS,1,1);
    }

    public int live = 50;
    @Override
    public void tick() {
        super.tick();
        addParticle(InitItems.BloodSoul_.asItem(), Light.ARGB.color(155,255,50,100));
        addParticle(InitItems.SpiritSoul_.asItem(),Light.ARGB.color(155,100,150,250));
        addParticle(InitItems.CelestialSoul_.asItem(),Light.ARGB.color(155,250,250,100));
        addParticle(InitItems.DeathSoul_.asItem(),Light.ARGB.color(155,50,250,75));
        addParticle(InitItems.MagicSoul_.asItem(),Light.ARGB.color(155,255,70,220));
        this.noPhysics = true;
        this.setNoGravity(true);
        if (canSee) {
            Entity target = this.getOwner();
            if (target instanceof LivingEntity livingEntity) {
                if (tickCount > 30) {
                    float size = Math.min((tickCount - 30f) / 100f, 0.63f);

                    Vec3 targetPos = livingEntity.position();
                    Vec3 direction = targetPos.subtract(position()).normalize();

                    Vec3 targetVelocity = direction.scale(size)
                            .add(
                                    Math.cos(tickCount / 7.5f) / 20f,
                                    Math.sin(tickCount / 7.5f) / 20f,
                                    0
                            );

                    Vec3 smoothVelocity = getDeltaMovement()
                            .scale(0.9)
                            .lerp(targetVelocity, 0.08);

                    setDeltaMovement(smoothVelocity);
                }
            }
        }else {
            setDeltaMovement(0,0,0);
            live--;
            if (live <= 0) {
                this.discard();
            }
        }
        if (this.tickCount > 200) {
            setCanSee(false);
        }
        if(canSee){
            trailPositions.add(position());
        }
        if(trailPositions.size()>15){
            trailPositions.removeFirst();
        }
    }
    @Override
    public void playerTouch(Player player) {
        if (canSee) {
            if (!Handler.has(player, InitItems.SoulBottle_.asItem())) {
                player.level().addFreshEntity(new ItemEntity(level(), position().x, position().y, position().z, getItem()));
            } else {
                addSoul(player);
            }
            this.level().playSound(null, this.blockPosition(), CVSounds.soul_pickup.value(), SoundSource.PLAYERS, 1.75f, 1);
        }
        setCanSee(false);
        addItem(InitItems.BloodSoul_.asItem(), Light.ARGB.color(155,255,50,100));
        addItem(InitItems.SpiritSoul_.asItem(),Light.ARGB.color(155,100,150,250));
        addItem(InitItems.CelestialSoul_.asItem(),Light.ARGB.color(155,250,250,100));
        addItem(InitItems.DeathSoul_.asItem(),Light.ARGB.color(155,50,250,75));
        addItem(InitItems.MagicSoul_.asItem(),Light.ARGB.color(155,255,70,220));
    }
    public void addParticle(Item item, int color) {
        if (canSee) {
            if (getItem().is(item)) {
                if (this.level() instanceof ServerLevel serverLevel) {
                    float speed = 0.33f;
                    if (tickCount < 50) {
                        speed = 0;
                    }
                    serverLevel.sendParticles(CubeOption.createCubeOption(MagicParticles.colorCube.get(),
                            this.getDeltaMovement().scale(speed), true, color, 0.125f), getX(), getY(), getZ(), 1, 0, 0, 0, 0);

                    serverLevel.sendParticles(MagicColorOption.createMagicColorOption(MagicParticles.colorOption.get(),
                            this.getDeltaMovement().scale(speed), true, color, 0.35f), getX(), getY(), getZ(), 1, 0, 0, 0, 0);
                }
            }
        }
    }
    public void addItem(Item item, int color){
        if (!canSee) {
            return;
        }
        if (getItem().is(item)) {
            if (this.level() instanceof ServerLevel serverLevel) {
                serverLevel.sendParticles(CubeOption.createCubeOption(MagicParticles.colorCube.get(),
                        Vec3.ZERO,true,color,0.66f),getX(),getY(),getZ(),1,0,0,0,0);

                serverLevel.sendParticles(MagicColorOption.createMagicColorOption(MagicParticles.colorOption.get(),
                        Vec3.ZERO,true,color,1.25f),getX(),getY(),getZ(),1,0,0,0,0);
            }
        }
    }
    public void setCanSee(boolean canSee) {
        this.canSee = canSee;

    }

    public void addSoul(Player player){
        ChestInventory inventory = Handler.getItem(player);
        if (inventory != null) {
            for (int i = 0; i < inventory.getContainerSize(); i++) {
                ItemStack stack = inventory.getItem(i);
                if (stack.is(InitItems.SoulBottle_.asItem())) {
                    if (stack.get(DataReg.soulMap) == null) {
                        stack.set(DataReg.soulMap, new SetSoulData(new HashMap<>()));
                    }
                    Item spiritItem = this.getItem().getItem();
                    String name = BuiltInRegistries.ITEM.getKey(spiritItem).toString();
                    SetSoulData setSoulData = stack.get(DataReg.soulMap);
                    if (setSoulData != null) {
                        Integer integer = setSoulData.soulMap().get(name);
                        if (integer == null) {
                            integer = 0;
                        }
                        setSoulData.soulMap().put(name, integer + 1);
                        this.setItem(ItemStack.EMPTY);
                        return;
                    }
                }
            }
        }
    }

    @Override
    public ProjectileDeflection deflection(Projectile projectile) {
        return ProjectileDeflection.NONE;
    }

    public int color() {
        if (this.getItem().is(InitItems.BloodSoul_.asItem())){
            return Light.ARGB.color(155,255,50,100);
        }
        if (this.getItem().is(InitItems.SpiritSoul_.asItem())){
            return Light.ARGB.color(155,100,150,250);
        }
        if (this.getItem().is(InitItems.CelestialSoul_.asItem())){
            return Light.ARGB.color(155,250,250,100);
        }
        if (this.getItem().is(InitItems.DeathSoul_.asItem())){
            return Light.ARGB.color(155,50,250,75);
        }
        if (this.getItem().is(InitItems.MagicSoul_.asItem())){
            return Light.ARGB.color(155,255,70,220);
        }
        return 0xffffffff;
    }
}
