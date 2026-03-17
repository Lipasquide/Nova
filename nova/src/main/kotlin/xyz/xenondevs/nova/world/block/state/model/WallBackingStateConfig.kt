package xyz.xenondevs.nova.world.block.state.model

import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.WallBlock
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.properties.WallSide
import xyz.xenondevs.nova.util.intValue

class WallBackingStateConfig(
    val up: Boolean,
    val north: WallSide,
    val east: WallSide,
    val south: WallSide,
    val west: WallSide,
    val waterlogged: Boolean
) : BackingStateConfig() {
    
    override val type = WallBackingStateConfig
    override val id = ((if (up) 1 else 0) shl 8) or 
                     (north.ordinal shl 6) or 
                     (east.ordinal shl 4) or 
                     (south.ordinal shl 2) or 
                     (west.ordinal shl 0) or
                     (if (waterlogged) 1 else 0) shl 10
    override val waterlogged = waterlogged
    
    override val variantMap = mapOf(
        "up" to up.toString(),
        "north" to north.name.lowercase(),
        "east" to east.name.lowercase(),
        "south" to south.name.lowercase(),
        "west" to west.name.lowercase(),
        "waterlogged" to waterlogged.toString()
    )
    
    override val vanillaBlockState: BlockState = Blocks.COBBLESTONE_WALL.defaultBlockState()
        .setValue(WallBlock.UP, up)
        .setValue(WallBlock.NORTH_WALL, north)
        .setValue(WallBlock.EAST_WALL, east)
        .setValue(WallBlock.SOUTH_WALL, south)
        .setValue(WallBlock.WEST_WALL, west)
        .setValue(WallBlock.WATERLOGGED, waterlogged)
    
    companion object : DynamicDefaultingBackingStateConfigType<WallBackingStateConfig>(
        1023, "wall"
    ) {
        override val properties = setOf("up", "north", "east", "south", "west", "waterlogged")
        override val isWaterloggable = true
        override val blockedIds = emptySet<Int>()
        
        override fun of(id: Int, waterlogged: Boolean): WallBackingStateConfig {
            val west = WallSide.entries[id and 0x3]
            val south = WallSide.entries[(id shr 2) and 0x3]
            val east = WallSide.entries[(id shr 4) and 0x3]
            val north = WallSide.entries[(id shr 6) and 0x3]
            val up = ((id shr 8) and 1) == 1
            val actualWaterlogged = waterlogged && ((id shr 10) and 1) == 1
            return WallBackingStateConfig(up, north, east, south, west, actualWaterlogged)
        }
        
        override fun of(properties: Map<String, String>): WallBackingStateConfig {
            return WallBackingStateConfig(
                up = properties["up"]?.toBoolean() ?: false,
                north = WallSide.valueOf(properties["north"]?.uppercase() ?: "NONE"),
                east = WallSide.valueOf(properties["east"]?.uppercase() ?: "NONE"),
                south = WallSide.valueOf(properties["south"]?.uppercase() ?: "NONE"),
                west = WallSide.valueOf(properties["west"]?.uppercase() ?: "NONE"),
                waterlogged = properties["waterlogged"]?.toBoolean() ?: false
            )
        }
    }
}
