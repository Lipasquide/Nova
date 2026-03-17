package xyz.xenondevs.nova.world.block.state.model

import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.StairBlock
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.core.Direction
import net.minecraft.world.level.block.state.properties.Half
import net.minecraft.world.level.block.state.properties.StairsShape
import xyz.xenondevs.nova.util.intValue

class StairsBackingStateConfig(
    val facing: Int,
    val half: Half,
    val shape: StairsShape,
    val waterlogged: Boolean
) : BackingStateConfig() {
    
    override val type = StairsBackingStateConfig
    override val id = (facing shl 4) or (half.ordinal shl 3) or (shape.ordinal shl 1) or waterlogged.intValue
    override val waterlogged = waterlogged
    
    override val variantMap = mapOf(
        "facing" to Direction.entries[facing].name.lowercase(),
        "half" to half.name.lowercase(),
        "shape" to shape.name.lowercase(),
        "waterlogged" to waterlogged.toString()
    )
    
    override val vanillaBlockState: BlockState = Blocks.OAK_STAIRS.defaultBlockState()
        .setValue(StairBlock.FACING, Direction.entries[facing])
        .setValue(StairBlock.HALF, half)
        .setValue(StairBlock.SHAPE, shape)
        .setValue(StairBlock.WATERLOGGED, waterlogged)
    
    companion object : DynamicDefaultingBackingStateConfigType<StairsBackingStateConfig>(
        191, "stairs"
    ) {
        override val properties = setOf("facing", "half", "shape", "waterlogged")
        override val isWaterloggable = true
        override val blockedIds = emptySet<Int>()
        
        override fun of(id: Int, waterlogged: Boolean): StairsBackingStateConfig {
            val actualWaterlogged = waterlogged && ((id and 1) == 1)
            val shape = StairsShape.entries[(id shr 1) and 0x3]
            val half = Half.entries[(id shr 3) and 0x1]
            val facing = id shr 4
            return StairsBackingStateConfig(facing, half, shape, actualWaterlogged)
        }
        
        override fun of(properties: Map<String, String>): StairsBackingStateConfig {
            return StairsBackingStateConfig(
                facing = when (properties["facing"]?.lowercase()) {
                    "north" -> 0
                    "east" -> 1
                    "south" -> 2
                    "west" -> 3
                    else -> 0
                },
                half = Half.valueOf(properties["half"]?.uppercase() ?: "BOTTOM"),
                shape = StairsShape.valueOf(properties["shape"]?.uppercase() ?: "STRAIGHT"),
                waterlogged = properties["waterlogged"]?.toBoolean() ?: false
            )
        }
    }
}
