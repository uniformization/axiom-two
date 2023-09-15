package me.ts.axiomtwo.mixin;

import com.moulberry.axiom.editor.CustomImGuiImplGlfw;
import imgui.ImVec2;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value = CustomImGuiImplGlfw.class, remap = false)
public interface IMixinCustomImGuiImplGlfw {
    @Accessor(value = "mousePosBackup", remap = false)
    ImVec2 mousePosBackup();

    @Accessor(value = "grabbed", remap = false)
    CustomImGuiImplGlfw.MouseHandledBy grabbed();
}
