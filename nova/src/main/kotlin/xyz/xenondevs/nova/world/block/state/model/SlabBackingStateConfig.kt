package xyz.xenondevs.nova.world.block.state.model

import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.SlabBlock
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.properties.SlabType
import xyz.xenondevs.nova.util.intValue

class SlabBackingStateConfig(
    val type: SlabType,
    val waterlogged: Boolean
) : BackingStateConfig() {
    
    override val type = SlabBackingStateConfig
    override val id = (type.ordinal shl 1) or waterlogged.intValue
    override val waterlogged = waterlogged
    
    override val variantMap = mapOf(
        "type" to type.name.lowercase(),
        "waterlogged" to waterlogged.toString()
    )
    
    override val vanillaBlockState: BlockState = Blocks.OAK_SLAB.defaultBlockState()
        .setValue(SlabBlock.TYPE, type)
        .setValue(SlabBlock.WATERLOGGED, waterlogged)
    
    companion object : DynamicDefaultingBackingStateConfigType<SlabBackingStateConfig>(
        5, "slab"
    ) {
        override val properties = setOf("type", "waterlogged")
        override val isWaterloggable = true
        override val blockedIds = emptySet<Int>()
        
        override fun of(id: Int, waterlogged: Boolean): SlabBackingStateConfig {
            val actualWaterlogged = waterlogged && ((id and 1) == 1)
            val type = SlabType.entries[id shr 1]
            return SlabBackingStateConfig(type, actualWaterlogged)
        }
        
        override fun of(properties: Map<String, String>): SlabBackingStateConfig {
            return SlabBackingStateConfig(
                type = SlabType.valueOf(properties["type"]?.uppercase() ?: "BOTTOM"),
                waterlogged = properties["waterlogged"]?.toBoolean() ?: false
            )
        }
    }
}
