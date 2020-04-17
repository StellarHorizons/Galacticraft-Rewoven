/*
 * Copyright (c) 2019 HRZN LTD
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

package com.hrznstudio.galacticraft.blocks.machines.rocketdesigner;

import com.hrznstudio.galacticraft.api.block.AbstractHorizontalDirectionalBlock;
import com.hrznstudio.galacticraft.container.GalacticraftContainers;
import com.hrznstudio.galacticraft.util.Rotatable;
import net.fabricmc.fabric.api.container.ContainerProviderRegistry;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import java.util.List;

/**
 * @author <a href="https://github.com/StellarHorizons">StellarHorizons</a>
 */
public class RocketDesignerBlock extends AbstractHorizontalDirectionalBlock implements Rotatable, BlockEntityProvider {
    public RocketDesignerBlock(Settings settings) {
        super(settings);
    }

    @Override
    public BlockEntity createBlockEntity(BlockView var1) {
        return new RocketDesignerBlockEntity();
    }

    @Override
    public final void buildTooltip(ItemStack itemStack_1, BlockView blockView_1, List<Text> list_1, TooltipContext tooltipContext_1) {
        if (Screen.hasShiftDown()) {
            list_1.add(new TranslatableText(getTooltipKey()).setStyle(new Style().setColor(Formatting.GRAY)));
        } else {
            list_1.add(new TranslatableText("tooltip.galacticraft-rewoven.press_shift").setStyle(new Style().setColor(Formatting.GRAY)));
        }
    }

    protected String getTooltipKey() {
        return "tooltip.galacticraft-rewoven.rocket_designer";
    }

    @Override
    public BlockRenderType getRenderType(BlockState blockState_1) {
        return BlockRenderType.MODEL;
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (world.isClient) {
            return ActionResult.SUCCESS;
        }

        ContainerProviderRegistry.INSTANCE.openContainer(GalacticraftContainers.ROCKET_DESIGNER_CONTAINER, player, packetByteBuf -> packetByteBuf.writeBlockPos(pos));
        return ActionResult.SUCCESS;
    }

    @Override
    public void onBreak(World world, BlockPos blockPos, BlockState blockState, PlayerEntity playerEntity) {
        super.onBreak(world, blockPos, blockState, playerEntity);

        BlockEntity blockEntity = world.getBlockEntity(blockPos);

        if (blockEntity != null) {
            if (blockEntity instanceof RocketDesignerBlockEntity) {
                RocketDesignerBlockEntity be = (RocketDesignerBlockEntity) blockEntity;

                ItemStack itemStack = be.getInventory().getInvStack(0);
                if (!itemStack.isEmpty()) {
                    world.spawnEntity(new ItemEntity(world, blockPos.getX(), blockPos.getY() + 1, blockPos.getZ(), itemStack.copy()));
                }
            }
        }
    }

}