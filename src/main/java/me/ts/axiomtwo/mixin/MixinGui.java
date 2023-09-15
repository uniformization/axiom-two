package me.ts.axiomtwo.mixin;

import com.moulberry.axiom.editor.EditorUI;
import me.ts.axiomtwo.AxiomTwo;
import imgui.ImVec2;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = Gui.class, priority = 1001)
public class MixinGui {
    @Shadow @Final private Minecraft minecraft;
    @Unique
    private static final ResourceLocation GRABBED_ICON = new ResourceLocation("axiom2", "pointer_grab.png");

    @Inject(method = "render(Lnet/minecraft/client/gui/GuiGraphics;F)V", at = @At("RETURN"))
    public void onRender(GuiGraphics context, float f, CallbackInfo ci) {
        if (AxiomTwo.showingGrab) {
            ImVec2 mousePos = ((IMixinCustomImGuiImplGlfw) EditorUI.imguiGlfw).mousePosBackup();
            int scaledX = (int) (EditorUI.getNewMouseX(mousePos.x)) * this.minecraft.getWindow().getGuiScaledWidth() / this.minecraft.getWindow().getScreenWidth();
            int scaledY = (int) (EditorUI.getNewMouseY(mousePos.y)) * this.minecraft.getWindow().getGuiScaledHeight() / this.minecraft.getWindow().getScreenHeight();
            final int textureScale = 10;
            context.blit(
                GRABBED_ICON,
                scaledX - (textureScale / 2), scaledY,
                0, 0,
                textureScale, textureScale,
                textureScale, textureScale
            );
        }
    }

    @Inject(method = "renderCrosshair", at = @At("HEAD"), cancellable = true)
    public void renderCrosshair(GuiGraphics context, CallbackInfo ci) {
        if (EditorUI.isActive() && AxiomTwo.showingGrab) {
            ci.cancel();
        }
    }
}
