package xyz.xenondevs.nova.world.block

import xyz.xenondevs.nova.addon.Addon
import xyz.xenondevs.nova.resources.builder.layout.block.BackingStateCategory
import xyz.xenondevs.nova.world.block.material.MaterialType
import xyz.xenondevs.nova.world.block.shape.BlockShape
import xyz.xenondevs.nova.world.block.tileentity.GenericShapedTileEntity
import xyz.xenondevs.nova.world.block.behavior.*
import xyz.xenondevs.nova.world.block.sound.SoundGroup
import xyz.xenondevs.nova.world.item.tool.VanillaToolCategories

object ShapedBlockFactory {
    
    private val materialCache = mutableMapOf<Pair<MaterialType, BlockShape>, NovaTileEntityBlock>()
    
    fun Addon.registerBlockForShape(material: MaterialType, shape: BlockShape): NovaTileEntityBlock {
        val cacheKey = material to shape
        materialCache[cacheKey]?.let { return it }
        
        val blockName = "${material.id}_${shape.id}"
        val blockId = Key(addon, blockName)
        
        val baseBehaviors = listOf(
            Breakable(
                hardness = material.hardness * shape.hardnessMultiplier,
                toolCategories = material.toolCategory,
                toolTier = material.toolTier,
                requiresToolForDrops = shape.requiresTool && material.requiresToolForDrops,
                breakParticles = material.baseMaterial
            ),
            BlockSounds(material.soundGroup)
        )
        
        val shapeBehaviors = when (shape) {
            BlockShape.STAIRS, BlockShape.SLAB, BlockShape.WALL,
            BlockShape.FENCE, BlockShape.DOOR, BlockShape.TRAPDOOR,
            BlockShape.LADDER, BlockShape.CHAIN, BlockShape.LANTERN,
            BlockShape.SOUL_LANTERN, BlockShape.SIGN, BlockShape.WALL_SIGN,
            BlockShape.HANGING_SIGN -> baseBehaviors + Waterloggable
            
            BlockShape.FENCE_GATE -> baseBehaviors + Waterloggable
            
            BlockShape.BUTTON, BlockShape.LEVER -> baseBehaviors
            
            BlockShape.PRESSURE_PLATE -> baseBehaviors
            
            BlockShape.TORCH, BlockShape.WALL_TORCH,
            BlockShape.SOUL_TORCH, BlockShape.SOUL_WALL_TORCH -> baseBehaviors
            
            BlockShape.CAMPFIRE, BlockShape.SOUL_CAMPFIRE -> baseBehaviors
            
            BlockShape.BED -> baseBehaviors
            
            BlockShape.CARPET -> baseBehaviors
            
            BlockShape.ANVIL, BlockShape.GRINDSTONE, BlockShape.STONECUTTER -> baseBehaviors
        }
        
        val block = tileEntity(blockName, ::GenericShapedTileEntity) {
            localizedName("block.${addon.id}.$blockName")
            behaviors(shapeBehaviors)
            stateProperties(shape.properties)
            
            when (shape) {
                BlockShape.STAIRS -> stateBacked(BackingStateCategory.NOTE_BLOCK) { defaultModel.rotated() }
                BlockShape.SLAB -> stateBacked(BackingStateCategory.NOTE_BLOCK) { defaultModel }
                BlockShape.WALL -> stateBacked(BackingStateCategory.NOTE_BLOCK) { defaultModel }
                BlockShape.FENCE -> stateBacked(BackingStateCategory.NOTE_BLOCK) { defaultModel }
                BlockShape.FENCE_GATE -> stateBacked(BackingStateCategory.NOTE_BLOCK) { defaultModel.rotated() }
                BlockShape.DOOR -> stateBacked(BackingStateCategory.NOTE_BLOCK) { defaultModel.rotated() }
                BlockShape.TRAPDOOR -> stateBacked(BackingStateCategory.NOTE_BLOCK) { defaultModel.rotated() }
                BlockShape.BUTTON -> stateBacked(BackingStateCategory.NOTE_BLOCK) { defaultModel.rotated() }
                BlockShape.LEVER -> stateBacked(BackingStateCategory.LEVER) { defaultModel.rotated() }
                BlockShape.PRESSURE_PLATE -> stateBacked(BackingStateCategory.NOTE_BLOCK) { defaultModel }
                BlockShape.TORCH -> stateBacked(BackingStateCategory.TORCH) { defaultModel }
                BlockShape.WALL_TORCH -> stateBacked(BackingStateCategory.WALL_TORCH) { defaultModel }
                BlockShape.LANTERN -> stateBacked(BackingStateCategory.LANTERN) { defaultModel }
                BlockShape.SOUL_LANTERN -> stateBacked(BackingStateCategory.SOUL_LANTERN) { defaultModel }
                BlockShape.CAMPFIRE -> stateBacked(BackingStateCategory.CAMPFIRE) { defaultModel.rotated() }
                BlockShape.SOUL_CAMPFIRE -> stateBacked(BackingStateCategory.SOUL_CAMPFIRE) { defaultModel.rotated() }
                BlockShape.BED -> stateBacked(BackingStateCategory.NOTE_BLOCK) { defaultModel.rotated() }
                BlockShape.SIGN -> stateBacked(BackingStateCategory.NOTE_BLOCK) { defaultModel }
                BlockShape.WALL_SIGN -> stateBacked(BackingStateCategory.NOTE_BLOCK) { defaultModel }
                BlockShape.HANGING_SIGN -> stateBacked(BackingStateCategory.NOTE_BLOCK) { defaultModel }
                BlockShape.CARPET -> stateBacked(BackingStateCategory.NOTE_BLOCK) { defaultModel }
                BlockShape.CHAIN -> stateBacked(BackingStateCategory.CHAIN) { defaultModel }
                BlockShape.LADDER -> stateBacked(BackingStateCategory.LADDER) { defaultModel }
                BlockShape.ANVIL -> stateBacked(BackingStateCategory.NOTE_BLOCK) { defaultModel.rotated() }
                BlockShape.GRINDSTONE -> stateBacked(BackingStateCategory.NOTE_BLOCK) { defaultModel.rotated() }
                BlockShape.STONECUTTER -> stateBacked(BackingStateCategory.NOTE_BLOCK) { defaultModel.rotated() }
            }
        }
        
        materialCache[cacheKey] = block
        return block
    }
    
    fun Addon.registerAllShapesForMaterial(material: MaterialType) {
        BlockShape.entries.forEach { shape ->
            registerBlockForShape(material, shape)
        }
    }
    
    fun getBlock(material: MaterialType, shape: BlockShape): NovaTileEntityBlock? =
        materialCache[material to shape]
}
