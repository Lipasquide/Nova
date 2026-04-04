package xyz.xenondevs.nova.world.block.state.model

import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.DoorBlock
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.core.Direction
import net.minecraft.world.level.block.state.properties.DoorHingeSide
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf
import xyz.xenondevs.nova.util.intValue

class DoorBackingStateConfig(
    val facing: Int,
    val half: DoubleBlockHalf,
    val hinge: DoorHingeSide,
    val open: Boolean,
    val powered: Boolean,
    val waterlogged: Boolean
) : BackingStateConfig() {
    
    override val type = DoorBackingStateConfig
    override val id = (facing shl 5) or (half.ordinal shl 4) or (hinge.ordinal shl 3) or
                      ((if (open) 1 else 0) shl 2) or ((if (powered) 1 else 0) shl 1) or
                      (if (waterlogged) 1 else 0)
    override val waterlogged = waterlogged
    
    override val variantMap = mapOf(
        "facing" to Direction.entries[facing].name.lowercase(),
        "half" to half.name.lowercase(),
        "hinge" to hinge.name.lowercase(),
        "open" to open.toString(),
        "powered" to powered.toString(),
        "waterlogged" to waterlogged.toString()
    )
    
    override val vanillaBlockState: BlockState = Blocks.OAK_DOOR.defaultBlockState()
        .setValue(DoorBlock.FACING, Direction.entries[facing])
        .setValue(DoorBlock.HALF, half)
        .setValue(DoorBlock.HINGE, hinge)
        .setValue(DoorBlock.OPEN, open)
        .setValue(DoorBlock.POWERED, powered)
    
    companion object : DynamicDefaultingBackingStateConfigType<DoorBackingStateConfig>(
        63, "door"
    ) {
        override val properties = setOf("facing", "half", "hinge", "open", "powered", "waterlogged")
        override val isWaterloggable = true
        override val blockedIds = emptySet<Int>()
        
        override fun of(id: Int, waterlogged: Boolean): DoorBackingStateConfig {
            val actualWaterlogged = waterlogged && ((id and 1) == 1)
            val powered = ((id shr 1) and 1) == 1
            val open = ((id shr 2) and 1) == 1
            val hinge = DoorHingeSide.entries[(id shr 3) and 0x1]
            val half = DoubleBlockHalf.entries[(id shr 4) and 0x1]
            val facing = id shr 5
            return DoorBackingStateConfig(facing, half, hinge, open, powered, actualWaterlogged)
        }
        
        override fun of(properties: Map<String, String>): DoorBackingStateConfig {
            return DoorBackingStateConfig(
                facing = when (properties["facing"]?.lowercase()) {
                    "north" -> 0
                    "east" -> 1
                    "south" -> 2
                    "west" -> 3
                    else -> 0
                },
                half = DoubleBlockHalf.valueOf(properties["half"]?.uppercase() ?: "LOWER"),
                hinge = DoorHingeSide.valueOf(properties["hinge"]?.uppercase() ?: "LEFT"),
                open = properties["open"]?.toBoolean() ?: false,
                powered = properties["powered"]?.toBoolean() ?: false,
                waterlogged = properties["waterlogged"]?.toBoolean() ?: false
            )
        }
    }
}
