package com.ytgld.chest_vows.render.book;

import com.ytgld.chest_item.renderer.book.CIBookScreen;
import com.ytgld.chest_item.renderer.book.tool.AddBookPage;
import com.ytgld.chest_item.renderer.book.tool.RegisterBookPage;
import com.ytgld.chest_item.renderer.light.Light;
import com.ytgld.chest_vows.items.BaseVows;
import com.ytgld.chest_vows.items.CVItems;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.phys.Vec2;

import java.util.List;

@AddBookPage
public class VowsPage implements RegisterBookPage {
    private final int aInt = 24;
    private final int color = Light.ARGB.color(255, 160, 80, 255);
    private final int lightColor = Light.ARGB.color(255, 80, 40, 128);

    @Override
    public void addPage(List<CIBookScreen.CIBookGuiAdd> list) {
        list.add(new CIBookScreen.CIBookGuiAdd(
                CVItems.Silence_.get(), new Vec2(-aInt - 48, -aInt * 3),
                Component.translatable("chest_vows.book.silence.main"),
                List.of(Component.translatable("chest_vows.book.silence.text")),
                this.color, this.color, CIBookScreen.ThePage.BLACK, getColor(CVItems.Silence_.get()), List.of(),
                true, true));

        list.add(new CIBookScreen.CIBookGuiAdd(
                CVItems.MutualSupport_.get(), new Vec2(-aInt - 48, -aInt * 4),
                Component.translatable("chest_vows.book.mutual_support.main"),
                List.of(Component.translatable("chest_vows.book.mutual_support.text")),
                this.color, this.color, CIBookScreen.ThePage.BLACK, getColor(CVItems.MutualSupport_.get()), List.of(),
                true, true));

        list.add(new CIBookScreen.CIBookGuiAdd(
                CVItems.Abandon_.get(), new Vec2(-aInt - 48, -aInt * 5),
                Component.translatable("chest_vows.book.abandon.main"),
                List.of(Component.translatable("chest_vows.book.abandon.text")),
                this.color, this.color, CIBookScreen.ThePage.BLACK, getColor(CVItems.Abandon_.get()), List.of(),
                true, true));

        list.add(new CIBookScreen.CIBookGuiAdd(
                CVItems.Trauma_.get(), new Vec2(aInt - 48, -aInt * 3),
                Component.translatable("chest_vows.book.trauma.main"),
                List.of(Component.translatable("chest_vows.book.trauma.text")),
                this.color, this.color, CIBookScreen.ThePage.BLACK, getColor(CVItems.Trauma_.get()), List.of(),
                true, true));

        list.add(new CIBookScreen.CIBookGuiAdd(
                CVItems.CtrlMagic_.get(), new Vec2(aInt - 48, -aInt *4),
                Component.translatable("chest_vows.book.ctrl_magic.main"),
                List.of(Component.translatable("chest_vows.book.ctrl_magic.text")),
                this.color, this.color, CIBookScreen.ThePage.BLACK, getColor(CVItems.CtrlMagic_.get()), List.of(),
                true, true));

        list.add(new CIBookScreen.CIBookGuiAdd(
                CVItems.PainSoul_.get(), new Vec2(aInt*2 - 48, -aInt * 4),
                Component.translatable("chest_vows.book.pain_soul.main"),
                List.of(Component.translatable("chest_vows.book.pain_soul.text")),
                this.color, this.color, CIBookScreen.ThePage.BLACK, getColor(CVItems.PainSoul_.get()), List.of(),
                true, true));



        list.add(new CIBookScreen.CIBookGuiAdd(
                CVItems.InnerDemon_.get(), new Vec2(aInt*2 - 48, -aInt * 5),
                Component.translatable("chest_vows.book.inner_demon.main"),
                List.of(Component.translatable("chest_vows.book.inner_demon.text")),
                this.color, this.color, CIBookScreen.ThePage.BLACK,
                getColor(CVItems.InnerDemon_.get()), List.of(),
                true, true));

        list.add(new CIBookScreen.CIBookGuiAdd(
                CVItems.BloodSacrifice_.get(), new Vec2(aInt*4 - 48, -aInt - 48),
                Component.translatable("chest_vows.book.blood_sacrifice.main"),
                List.of(Component.translatable("chest_vows.book.blood_sacrifice.text")),
                this.color, this.color, CIBookScreen.ThePage.BLACK,
                getColor(CVItems.BloodSacrifice_.get()), List.of(),
                true, true));

        list.add(new CIBookScreen.CIBookGuiAdd(
                CVItems.SwordHolder_.get(), new Vec2(aInt*4 - 48, -aInt * 2- 48),
                Component.translatable("chest_vows.book.sword_holder.main"),
                List.of(Component.translatable("chest_vows.book.sword_holder.text")),
                this.color, this.color, CIBookScreen.ThePage.BLACK,
                getColor(CVItems.SwordHolder_.get()), List.of(),
                true, true));

        list.add(new CIBookScreen.CIBookGuiAdd(
                CVItems.BloodArmy_.get(), new Vec2(aInt*4 - 48, -aInt * 3- 48),
                Component.translatable("chest_vows.book.blood_army.main"),
                List.of(Component.translatable("chest_vows.book.blood_army.text")),
                this.color, this.color, CIBookScreen.ThePage.BLACK,
                getColor(CVItems.BloodArmy_.get()), List.of(),
                true, true));

        list.add(new CIBookScreen.CIBookGuiAdd(
                CVItems.Symbiosis_.get(), new Vec2(aInt*4 - 48, -aInt * 4- 48),
                Component.translatable("chest_vows.book.symbiosis.main"),
                List.of(Component.translatable("chest_vows.book.symbiosis.text")),
                this.color, this.color, CIBookScreen.ThePage.BLACK,
                getColor(CVItems.Symbiosis_.get()), List.of(),
                true, true));

        list.add(new CIBookScreen.CIBookGuiAdd(
                CVItems.ChiefPriest_.get(), new Vec2(aInt*4 - 48, -aInt * 5- 48),
                Component.translatable("chest_vows.book.chief_priest.main"),
                List.of(Component.translatable("chest_vows.book.chief_priest.text")),
                this.color, this.color, CIBookScreen.ThePage.BLACK,
                getColor(CVItems.ChiefPriest_.get()), List.of(),
                true, true));

    }
    private int getColor(Item item){
        if (item instanceof BaseVows vows) {
            int color = vows.color(item.getDefaultInstance());
            int as = (color >> 24) & 0xFF;
            int rs = (color >> 16) & 0xFF;
            int gs = (color >> 8) & 0xFF;
            int bs = color & 0xFF;
            return Light.ARGB.color(as, (int) (rs / 2.5f), (int) (gs / 2.5f), (int) (bs / 2.5f));
        }
        return 0;
    }
}
