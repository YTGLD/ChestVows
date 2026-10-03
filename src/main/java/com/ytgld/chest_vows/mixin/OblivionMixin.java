package com.ytgld.chest_vows.mixin;

import com.ytgld.chest_item.items.memory.Oblivion;
import com.ytgld.chest_vows.VowHandler;
import com.ytgld.chest_vows.items.BaseVows;
import com.ytgld.chest_vows.other.CVAttReg;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.HashSet;

@Mixin(Oblivion.class)
public class OblivionMixin {
    @Inject(method = "finishUsingItem", at = @At(value = "RETURN"))
    private void finishUsingItem(ItemStack itemStack, Level level, LivingEntity entity, CallbackInfoReturnable<ItemStack> cir) {
        if (entity instanceof Player player) {
            player.setData(CVAttReg.vows,new HashSet<>());
            VowHandler.clearModify(player);
        }
    }
}
