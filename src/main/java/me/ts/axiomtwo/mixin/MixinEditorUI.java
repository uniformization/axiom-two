package me.ts.axiomtwo.mixin;

import com.moulberry.axiom.editor.CustomImGuiImplGlfw;
import com.moulberry.axiom.editor.EditorMovementControls;
import com.moulberry.axiom.editor.keybinds.Keybinds;
import me.ts.axiomtwo.AxiomTwo;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Pseudo
@Mixin(targets = "com.moulberry.axiom.editor.EditorUI", remap = false)
public class MixinEditorUI {
    @Shadow(remap = false) @Final public static CustomImGuiImplGlfw imguiGlfw;
    @Shadow(remap = false) public static EditorMovementControls movementControls;

    @Inject(method = "handleBasicInputs", at = @At("HEAD"), remap = false, cancellable = true)
    private static void onHandleBasicInputs(CallbackInfo ci) {
        if (Keybinds.ROTATE_CAMERA.isPressed(false)) {
            if (!AxiomTwo.showingGrab) AxiomTwo.showingGrab = true;
            movementControls = EditorMovementControls.rotate();
            if (movementControls != EditorMovementControls.none()) {
                int button = Keybinds.ROTATE_CAMERA.getKey();
                if (button < 0) {
                    double mouseX = ((IMixinCustomImGuiImplGlfw) imguiGlfw).mousePosBackup().x;
                    double mouseY = ((IMixinCustomImGuiImplGlfw) imguiGlfw).mousePosBackup().y;
                    imguiGlfw.setGrabbed(
                        true,
                        -button - 1,
                        mouseX, mouseY
                    );
                }
                ci.cancel();
            }
        }
    }

    @Inject(method = "handleBasicInputs", at = @At("TAIL"), remap = false)
    private static void onHandleBasicInputs2(CallbackInfo ci) {
        if (Keybinds.CROSSHAIR_CAMERA.isPressed(false) || ((IMixinCustomImGuiImplGlfw) imguiGlfw).grabbed() == null) {
            AxiomTwo.showingGrab = false;
        }
    }
}
