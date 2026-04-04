package xyz.xenondevs.nova.world.block.state.model

import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.state.BlockState

class CarpetBackingStateConfig : BackingStateConfig() {
    override val type = CarpetBackingStateConfig
    override val id = 0
    override val waterlogged = false
    override val variantMap = emptyMap<String, String>()
    override val vanillaBlockState: BlockState = Blocks.WHITE_CARPET.defaultBlockState()
    
    companion object : SimpleBackingStateConfigType<CarpetBackingStateConfig>(
        0, "carpet", { CarpetBackingStateConfig() }
    ) {
        override val properties = emptySet<String>()
        override val isWaterloggable = false
    }
}
