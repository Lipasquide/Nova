package xyz.xenondevs.nova.world.block.state.model

import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.FenceBlock
import net.minecraft.world.level.block.state.BlockState
import xyz.xenondevs.nova.util.intValue

class FenceBackingStateConfig(
    val north: Boolean,
    val east: Boolean,
    val south: Boolean,
    val west: Boolean,
    val waterlogged: Boolean
) : BackingStateConfig() {
    
    override val type = FenceBackingStateConfig
    override val id = ((if (north) 1 else 0) shl 4) or
                     ((if (east) 1 else 0) shl 3) or
                     ((if (south) 1 else 0) shl 2) or
                     ((if (west) 1 else 0) shl 1) or
                     (if (waterlogged) 1 else 0)
    override val waterlogged = waterlogged
    
    override val variantMap = mapOf(
        "north" to north.toString(),
        "east" to east.toString(),
        "south" to south.toString(),
        "west" to west.toString(),
        "waterlogged" to waterlogged.toString()
    )
    
    override val vanillaBlockState: BlockState = Blocks.OAK_FENCE.defaultBlockState()
        .setValue(FenceBlock.NORTH, north)
        .setValue(FenceBlock.EAST, east)
        .setValue(FenceBlock.SOUTH, south)
        .setValue(FenceBlock.WEST, west)
        .setValue(FenceBlock.WATERLOGGED, waterlogged)
    
    companion object : DynamicDefaultingBackingStateConfigType<FenceBackingStateConfig>(
        31, "fence"
    ) {
        override val properties = setOf("north", "east", "south", "west", "waterlogged")
        override val isWaterloggable = true
        override val blockedIds = emptySet<Int>()
        
        override fun of(id: Int, waterlogged: Boolean): FenceBackingStateConfig {
            val actualWaterlogged = waterlogged && ((id and 1) == 1)
            val west = ((id shr 1) and 1) == 1
            val south = ((id shr 2) and 1) == 1
            val east = ((id shr 3) and 1) == 1
            val north = ((id shr 4) and 1) == 1
            return FenceBackingStateConfig(north, east, south, west, actualWaterlogged)
        }
        
        override fun of(properties: Map<String, String>): FenceBackingStateConfig {
            return FenceBackingStateConfig(
                north = properties["north"]?.toBoolean() ?: false,
                east = properties["east"]?.toBoolean() ?: false,
                south = properties["south"]?.toBoolean() ?: false,
                west = properties["west"]?.toBoolean() ?: false,
                waterlogged = properties["waterlogged"]?.toBoolean() ?: false
            )
        }
    }
}
