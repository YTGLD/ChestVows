package com.ytgld.chest_vows.event;

import com.ytgld.chest_item.Handler;
import com.ytgld.chest_item.items.InitItems;
import com.ytgld.chest_vows.entity.TheSpirit;
import com.ytgld.chest_vows.sounds.CVSounds;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.WitherSkeleton;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;

public class SpiritSoulHandler {
    public static void event(LivingDeathEvent event) {
        if (event.getSource().getEntity() instanceof Player player && event.getEntity() instanceof LivingEntity livingEntity) {
            playerSounds(player);
            for (int i = 0; i < 2; i++) {
                ArrayList<Item> soul = soul();
                Item item = soul.get(player.getRandom().nextInt((soul.size())));
                TheSpirit spirit = new TheSpirit(player.level(), livingEntity.getX(),
                        livingEntity.getEyeY(), livingEntity.getZ(), item.getDefaultInstance());

                spirit.setThrower(player);

                spirit.setDeltaMovement(Mth.nextFloat(player.getRandom(), -0.125f, 0.125f), 0.05, Mth.nextFloat(livingEntity.getRandom(), -0.125f, 0.125f));

                player.level().addFreshEntity(spirit);
            }
        }
    }

    private static void playerSounds(Player player) {
        player.level().playSound(null, player.blockPosition(), CVSounds.soul_fly.value(), SoundSource.PLAYERS, 1, 1);
    }

    private static ArrayList<Item> soul() {
        ArrayList<Item> list = new ArrayList<>();
        list.add(InitItems.BloodSoul_.asItem());
        list.add(InitItems.CelestialSoul_.asItem());
        list.add(InitItems.SpiritSoul_.asItem());
        list.add(InitItems.MagicSoul_.asItem());
        list.add(InitItems.DeathSoul_.asItem());
        return list;
    }

    public static void mixinSoulBottle(CallbackInfo ci) {
        ci.cancel();
    }

    public static void mixinChestSoulHandler(LivingDeathEvent event, CallbackInfo ci) {
        LivingEntity livingEntity = event.getEntity();
        Item spiritSoulItem = InitItems.SpiritSoul_.asItem();
        Item magicSoulItem = InitItems.MagicSoul_.asItem();
        Item deathSoulItem = InitItems.DeathSoul_.asItem();
        Item bloodSoulItem = InitItems.BloodSoul_.asItem();
        Item celestialSoulItem = InitItems.CelestialSoul_.asItem();
        if (event.getSource().getEntity() instanceof OwnableEntity ownableEntity) {
            LivingEntity var10 = ownableEntity.getOwner();
            if (var10 instanceof Player player) {
                if (Handler.has(player,InitItems.SoulBottle_.asItem())) {
                    int countCelestial = RandomSource.create().nextInt(2);
                    addSoul(event.getEntity(), player, celestialSoulItem, countCelestial);
                }
            }
        }
        if (event.getSource().getEntity() instanceof Player player) {
            if (Handler.has(player, InitItems.SoulBottle_.asItem())) {
                if (livingEntity instanceof Animal) {
                    int countSpirit = RandomSource.create().nextInt(2);
                    int countMagic = RandomSource.create().nextInt(2);
                    addSoul(event.getEntity(), player, spiritSoulItem, countSpirit);
                    addSoul(event.getEntity(), player, magicSoulItem, countMagic);
                }

                if (livingEntity instanceof Monster monster) {
                    int countSpirit = RandomSource.create().nextInt(3) + 1;
                    int countMagic = RandomSource.create().nextInt(2);
                    addSoul(event.getEntity(), player, spiritSoulItem, countSpirit);
                    addSoul(event.getEntity(), player, magicSoulItem, countMagic);
                    if (monster.isInvertedHealAndHarm()) {
                        int countDie = RandomSource.create().nextInt(3);
                        addSoul(event.getEntity(), player, deathSoulItem, countDie);
                    }
                }

                if (livingEntity instanceof EnderMan || livingEntity instanceof WitherSkeleton) {
                    int countSpirit = RandomSource.create().nextInt(4) + 1;
                    int countMagic = RandomSource.create().nextInt(3) + 1;
                    int countBlood = RandomSource.create().nextInt(4) + 2;
                    addSoul(event.getEntity(), player, spiritSoulItem, countSpirit);
                    addSoul(event.getEntity(), player, magicSoulItem, countMagic);
                    addSoul(event.getEntity(), player, bloodSoulItem, countBlood);
                }
            }
        }
    }

    public static void addSoul(LivingEntity target,Player player, Item item ,int v){
        for (int i = 0; i < v; i++) {
            TheSpirit spirit = new TheSpirit(target.level(), target.getX(),
                    target.getEyeY(), target.getZ(), item.getDefaultInstance());

            spirit.setThrower(player);

            spirit.setDeltaMovement(Mth.nextFloat(player.getRandom(), -0.125f, 0.125f), 0.05,
                    Mth.nextFloat(player.getRandom(), -0.125f, 0.125f));

            player.level().addFreshEntity(spirit);
        }
    }
}
