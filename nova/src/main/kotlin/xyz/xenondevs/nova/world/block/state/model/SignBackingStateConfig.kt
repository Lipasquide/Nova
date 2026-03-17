package xyz.xenondevs.nova.world.block.state.model

import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.StandingSignBlock
import net.minecraft.world.level.block.WallSignBlock
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.core.Direction
import xyz.xenondevs.nova.util.intValue

class StandingSignBackingStateConfig(
    val rotation: Int,
    val waterlogged: Boolean
) : BackingStateConfig() {
    
    override val type = StandingSignBackingStateConfig
    override val id = (rotation shl 1) or (if (waterlogged) 1 else 0)
    override val waterlogged = waterlogged
    
    override val variantMap = mapOf(
        "rotation" to rotation.toString(),
        "waterlogged" to waterlogged.toString()
    )
    
    override val vanillaBlockState: BlockState = Blocks.OAK_SIGN.defaultBlockState()
        .setValue(StandingSignBlock.ROTATION, rotation)
        .setValue(StandingSignBlock.WATERLOGGED, waterlogged)
    
    companion object : DynamicDefaultingBackingStateConfigType<StandingSignBackingStateConfig>(
        31, "standing_sign"
    ) {
        override val properties = setOf("rotation", "waterlogged")
        override val isWaterloggable = true
        override val blockedIds = emptySet<Int>()
        
        override fun of(id: Int, waterlogged: Boolean): StandingSignBackingStateConfig {
            val actualWaterlogged = waterlogged && ((id and 1) == 1)
            val rotation = id shr 1
            return StandingSignBackingStateConfig(rotation, actualWaterlogged)
        }
        
        override fun of(properties: Map<String, String>): StandingSignBackingStateConfig {
            return StandingSignBackingStateConfig(
                rotation = properties["rotation"]?.toInt() ?: 0,
                waterlogged = properties["waterlogged"]?.toBoolean() ?: false
            )
        }
    }
}

class WallSignBackingStateConfig(
    val facing: Int,
    val waterlogged: Boolean
) : BackingStateConfig() {
    
    override val type = WallSignBackingStateConfig
    override val id = (facing shl 1) or (if (waterlogged) 1 else 0)
    override val waterlogged = waterlogged
    
    override val variantMap = mapOf(
        "facing" to Direction.entries[facing].name.lowercase(),
        "waterlogged" to waterlogged.toString()
    )
    
    override val vanillaBlockState: BlockState = Blocks.OAK_WALL_SIGN.defaultBlockState()
        .setValue(WallSignBlock.FACING, Direction.entries[facing])
        .setValue(WallSignBlock.WATERLOGGED, waterlogged)
    
    companion object : DynamicDefaultingBackingStateConfigType<WallSignBackingStateConfig>(
        7, "wall_sign"
    ) {
        override val properties = setOf("facing", "waterlogged")
        override val isWaterloggable = true
        override val blockedIds = emptySet<Int>()
        
        override fun of(id: Int, waterlogged: Boolean): WallSignBackingStateConfig {
            val actualWaterlogged = waterlogged && ((id and 1) == 1)
            val facing = id shr 1
            return WallSignBackingStateConfig(facing, actualWaterlogged)
        }
        
        override fun of(properties: Map<String, String>): WallSignBackingStateConfig {
            return WallSignBackingStateConfig(
                facing = when (properties["facing"]?.lowercase()) {
                    "north" -> 0
                    "east" -> 1
                    "south" -> 2
                    "west" -> 3
                    else -> 0
                },
                waterlogged = properties["waterlogged"]?.toBoolean() ?: false
            )
        }
    }
}

class HangingSignBackingStateConfig(
    val facing: Int,
    val attached: Boolean,
    val waterlogged: Boolean
) : BackingStateConfig() {
    
    override val type = HangingSignBackingStateConfig
    override val id = (facing shl 2) or ((if (attached) 1 else 0) shl 1) or (if (waterlogged) 1 else 0)
    override val waterlogged = waterlogged
    
    override val variantMap = mapOf(
        "facing" to Direction.entries[facing].name.lowercase(),
        "attached" to attached.toString(),
        "waterlogged" to waterlogged.toString()
    )
    
    override val vanillaBlockState: BlockState = Blocks.OAK_HANGING_SIGN.defaultBlockState()
        .setValue(net.minecraft.world.level.block.HangingSignBlock.FACING, Direction.entries[facing])
        .setValue(net.minecraft.world.level.block.HangingSignBlock.ATTACHED, attached)
        .setValue(net.minecraft.world.level.block.HangingSignBlock.WATERLOGGED, waterlogged)
    
    companion object : DynamicDefaultingBackingStateConfigType<HangingSignBackingStateConfig>(
        15, "hanging_sign"
    ) {
        override val properties = setOf("facing", "attached", "waterlogged")
        override val isWaterloggable = true
        override val blockedIds = emptySet<Int>()
        
        override fun of(id: Int, waterlogged: Boolean): HangingSignBackingStateConfig {
            val actualWaterlogged = waterlogged && ((id and 1) == 1)
            val attached = ((id shr 1) and 1) == 1
            val facing = id shr 2
            return HangingSignBackingStateConfig(facing, attached, actualWaterlogged)
        }
        
        override fun of(properties: Map<String, String>): HangingSignBackingStateConfig {
            return HangingSignBackingStateConfig(
                facing = when (properties["facing"]?.lowercase()) {
                    "north" -> 0
                    "east" -> 1
                    "south" -> 2
                    "west" -> 3
                    else -> 0
                },
                attached = properties["attached"]?.toBoolean() ?: false,
                waterlogged = properties["waterlogged"]?.toBoolean() ?: false
            )
        }
    }
}
