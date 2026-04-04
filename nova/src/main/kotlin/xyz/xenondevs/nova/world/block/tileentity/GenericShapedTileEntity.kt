package xyz.xenondevs.nova.world.block.tileentity

import xyz.xenondevs.cbf.Compound
import xyz.xenondevs.nova.world.BlockPos
import xyz.xenondevs.nova.world.block.state.NovaBlockState
import xyz.xenondevs.nova.world.block.state.property.DefaultBlockStateProperties
import xyz.xenondevs.nova.world.block.tileentity.menu.TileEntityMenuClass
import org.bukkit.entity.Player
import xyz.xenondevs.nova.context.Context
import xyz.xenondevs.nova.context.intention.DefaultContextIntentions.BlockInteract
import xyz.xenondevs.nova.util.playClickSound

@TileEntityMenuClass
class GenericShapedTileEntity(
    pos: BlockPos,
    blockState: NovaBlockState,
    data: Compound
) : NetworkedTileEntity(pos, blockState, data) {
    
    private var poweredTicks = 0
    
    override fun handleTick() {
        // Blok tipine göre tick işlemleri
        when {
            blockState.block.id.value().contains("button") -> handleButtonTick()
            blockState.block.id.value().contains("lever") -> Unit
            blockState.block.id.value().contains("door") -> Unit
            blockState.block.id.value().contains("trapdoor") -> Unit
            blockState.block.id.value().contains("fence_gate") -> Unit
            blockState.block.id.value().contains("campfire") -> handleCampfireTick()
            blockState.block.id.value().contains("lantern") -> Unit
        }
    }
    
    private fun handleButtonTick() {
        val powered = blockState.getOrThrow(DefaultBlockStateProperties.POWERED)
        if (powered) {
            poweredTicks++
            if (poweredTicks > 20) { // 1 saniye 20 tick
                updateBlockState(blockState.with(DefaultBlockStateProperties.POWERED, false))
                poweredTicks = 0
                playClickSound()
            }
        }
    }
    
    private fun handleCampfireTick() {
        // Kamp ateşi yemek pişirme mantığı
    }
    
    override fun handleInteract(ctx: Context<BlockInteract>): Boolean {
        val player = ctx.getSourceEntity() as? Player ?: return false
        
        return when {
            blockState.block.id.value().contains("button") -> handleButtonInteract(player)
            blockState.block.id.value().contains("lever") -> handleLeverInteract(player)
            blockState.block.id.value().contains("door") -> handleDoorInteract(player)
            blockState.block.id.value().contains("trapdoor") -> handleTrapdoorInteract(player)
            blockState.block.id.value().contains("fence_gate") -> handleFenceGateInteract(player)
            blockState.block.id.value().contains("campfire") -> handleCampfireInteract(player)
            else -> false
        }
    }
    
    private fun handleButtonInteract(player: Player): Boolean {
        if (!blockState.getOrThrow(DefaultBlockStateProperties.POWERED)) {
            updateBlockState(blockState.with(DefaultBlockStateProperties.POWERED, true))
            poweredTicks = 0
            player.playClickSound()
            return true
        }
        return false
    }
    
    private fun handleLeverInteract(player: Player): Boolean {
        val currentPowered = blockState.getOrThrow(DefaultBlockStateProperties.POWERED)
        updateBlockState(blockState.with(DefaultBlockStateProperties.POWERED, !currentPowered))
        player.playClickSound()
        return true
    }
    
    private fun handleDoorInteract(player: Player): Boolean {
        val currentOpen = blockState.getOrThrow(DefaultBlockStateProperties.OPEN)
        updateBlockState(blockState.with(DefaultBlockStateProperties.OPEN, !currentOpen))
        player.playClickSound()
        return true
    }
    
    private fun handleTrapdoorInteract(player: Player): Boolean {
        val currentOpen = blockState.getOrThrow(DefaultBlockStateProperties.OPEN)
        updateBlockState(blockState.with(DefaultBlockStateProperties.OPEN, !currentOpen))
        player.playClickSound()
        return true
    }
    
    private fun handleFenceGateInteract(player: Player): Boolean {
        val currentOpen = blockState.getOrThrow(DefaultBlockStateProperties.OPEN)
        updateBlockState(blockState.with(DefaultBlockStateProperties.OPEN, !currentOpen))
        player.playClickSound()
        return true
    }
    
    private fun handleCampfireInteract(player: Player): Boolean {
        val currentLit = blockState.getOrThrow(DefaultBlockStateProperties.LIT)
        if (!currentLit && player.inventory.itemInMainHand.type.name.contains("FLINT_AND_STEEL")) {
            updateBlockState(blockState.with(DefaultBlockStateProperties.LIT, true))
            return true
        }
        return false
    }
    
    override fun handleNeighborChanged(pos: BlockPos, state: NovaBlockState) {
        // Redstone sinyali geldiğinde
        val powered = pos.block.isBlockPowered
        val currentPowered = state.getOrThrow(DefaultBlockStateProperties.POWERED)
        
        if (powered != currentPowered) {
            when {
                state.block.id.value().contains("door") ||
                state.block.id.value().contains("trapdoor") ||
                state.block.id.value().contains("fence_gate") -> {
                    updateBlockState(state.with(DefaultBlockStateProperties.OPEN, powered))
                }
                state.block.id.value().contains("lever") -> {
                    updateBlockState(state.with(DefaultBlockStateProperties.POWERED, powered))
                }
            }
        }
    }
    
    private fun playClickSound() {
        pos.world.playSound(pos.location, "minecraft:block.wooden_button.click_off", 1f, 1f)
    }
}
