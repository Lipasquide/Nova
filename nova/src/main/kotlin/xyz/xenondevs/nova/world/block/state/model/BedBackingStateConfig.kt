package xyz.xenondevs.nova.world.block.state.model

import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.BedBlock
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.core.Direction
import net.minecraft.world.level.block.state.properties.BedPart
import xyz.xenondevs.nova.util.intValue

class BedBackingStateConfig(
    val facing: Int,
    val occupied: Boolean,
    val part: BedPart
) : BackingStateConfig() {
    
    override val type = BedBackingStateConfig
    override val id = (facing shl 3) or ((if (occupied) 1 else 0) shl 2) or (part.ordinal)
    override val waterlogged = false
    
    override val variantMap = mapOf(
        "facing" to Direction.entries[facing].name.lowercase(),
        "occupied" to occupied.toString(),
        "part" to part.name.lowercase()
    )
    
    override val vanillaBlockState: BlockState = Blocks.RED_BED.defaultBlockState()
        .setValue(BedBlock.FACING, Direction.entries[facing])
        .setValue(BedBlock.OCCUPIED, occupied)
        .setValue(BedBlock.PART, part)
    
    companion object : DynamicDefaultingBackingStateConfigType<BedBackingStateConfig>(
        15, "bed"
    ) {
        override val properties = setOf("facing", "occupied", "part")
        override val isWaterloggable = false
        override val blockedIds = emptySet<Int>()
        
        override fun of(id: Int, waterlogged: Boolean): BedBackingStateConfig {
            val part = BedPart.entries[id and 0x3]
            val occupied = ((id shr 2) and 1) == 1
            val facing = id shr 3
            return BedBackingStateConfig(facing, occupied, part)
        }
        
        override fun of(properties: Map<String, String>): BedBackingStateConfig {
            return BedBackingStateConfig(
                facing = when (properties["facing"]?.lowercase()) {
                    "north" -> 0
                    "east" -> 1
                    "south" -> 2
                    "west" -> 3
                    else -> 0
                },
                occupied = properties["occupied"]?.toBoolean() ?: false,
                part = BedPart.valueOf(properties["part"]?.uppercase() ?: "FOOT")
            )
        }
    }
}
