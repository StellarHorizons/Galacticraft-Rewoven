/*
 * Copyright (c) 2019-2021 HRZN LTD
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.hrznstudio.galacticraft.client.gui.screen.ingame;

import com.hrznstudio.galacticraft.Constants;
import com.hrznstudio.galacticraft.api.screen.MachineHandledScreen;
import com.hrznstudio.galacticraft.screen.OxygenStorageModuleScreenHandler;
import com.hrznstudio.galacticraft.util.DrawableUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

import java.math.RoundingMode;

/**
 * @author <a href="https://github.com/StellarHorizons">StellarHorizons</a>
 */
@Environment(EnvType.CLIENT)
public class OxygenStorageModuleScreen extends MachineHandledScreen<OxygenStorageModuleScreenHandler> {
    private static final Identifier BACKGROUND = new Identifier(Constants.MOD_ID, Constants.ScreenTextures.getRaw(Constants.ScreenTextures.OXYGEN_STORAGE_MODULE_SCREEN));

    public OxygenStorageModuleScreen(OxygenStorageModuleScreenHandler handler, PlayerInventory inv, Text title) {
        super(handler, inv, inv.player.world, handler.machine.getPos(), title);
        this.backgroundWidth = 176;
        this.backgroundHeight = 166;
    }

    @Override
    protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
        this.renderBackground(matrices);
        this.client.getTextureManager().bindTexture(BACKGROUND);
        this.drawTexture(matrices, this.x, this.y, 0, 0, this.backgroundWidth, this.backgroundHeight);

        this.drawOxygenBufferBar(matrices);

        DrawableUtils.drawCenteredString(matrices, textRenderer, I18n.translate("ui.galacticraft-rewoven.machine.current_oxygen", (int)(this.handler.machine.getFluidTank().getInvFluid(0).getAmount_F().asInt(1000, RoundingMode.HALF_DOWN))), width / 2, y + 33, Formatting.DARK_GRAY.getColorValue());
        DrawableUtils.drawCenteredString(matrices, textRenderer, I18n.translate("ui.galacticraft-rewoven.machine.max_oxygen", (int)(this.handler.machine.getFluidTank().getMaxAmount_F(0).asInt(1000, RoundingMode.HALF_DOWN))), width / 2, y + 45, Formatting.DARK_GRAY.getColorValue());
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        super.render(matrices, mouseX, mouseY, delta);
        DrawableUtils.drawCenteredString(matrices, textRenderer, I18n.translate("block.galacticraft-rewoven.oxygen_storage_module"), (this.width / 2), this.y + 5, Formatting.DARK_GRAY.getColorValue());
        this.drawMouseoverTooltip(matrices, mouseX, mouseY);
    }

    private void drawOxygenBufferBar(MatrixStack matrices) {
        double oxygenScale = this.handler.machine.getFluidTank().getInvFluid(0).getAmount_F().div(this.handler.machine.getFluidTank().getMaxAmount_F(0)).asInexactDouble();

        this.client.getTextureManager().bindTexture(BACKGROUND);
        this.drawTexture(matrices, this.x + 52, this.y + 57, 176, 0, (int) (72.0D * oxygenScale), 3);
    }
}
