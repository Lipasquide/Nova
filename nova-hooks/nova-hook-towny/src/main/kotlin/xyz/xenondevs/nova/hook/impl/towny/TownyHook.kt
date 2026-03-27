package xyz.xenondevs.nova.hook.impl.towny

import com.palmergames.bukkit.towny.`object`.TownyPermission
import com.palmergames.bukkit.towny.utils.PlayerCacheUtil
import org.bukkit.Location
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Entity
import org.bukkit.inventory.ItemStack
import xyz.xenondevs.nova.api.protection.ProtectionIntegration
import xyz.xenondevs.nova.api.protection.ProtectionIntegration.ExecutionMode
import xyz.xenondevs.nova.integration.Hook

@Hook(plugins = ["Towny"])
internal object TownyHook : ProtectionIntegration {
    
    override fun getExecutionMode(): ExecutionMode = ExecutionMode.SERVER
    
    override fun canBreak(player: OfflinePlayer, item: ItemStack?, location: Location) =
        hasPermission(player, location, TownyPermission.ActionType.DESTROY)
    
    override fun canPlace(player: OfflinePlayer, item: ItemStack, location: Location) =
        hasPermission(player, location, TownyPermission.ActionType.BUILD)
    
    override fun canUseBlock(player: OfflinePlayer, item: ItemStack?, location: Location) =
        hasPermission(player, location, TownyPermission.ActionType.SWITCH)
    
    override fun canUseItem(player: OfflinePlayer, item: ItemStack, location: Location) =
        hasPermission(player, location, TownyPermission.ActionType.ITEM_USE)
    
    override fun canInteractWithEntity(player: OfflinePlayer, entity: Entity, item: ItemStack?) =
        hasPermission(player, entity.location, TownyPermission.ActionType.ITEM_USE)
    
    override fun canHurtEntity(player: OfflinePlayer, entity: Entity, item: ItemStack?) =
        hasPermission(player, entity.location, TownyPermission.ActionType.DESTROY)
    
    // Towny offers no OfflinePlayer API, FakeOnlinePlayer breaks as tries to query LuckPerms with it directly
    // see: https://github.com/xenondevs/Nova/issues/791
    private fun hasPermission(player: OfflinePlayer, location: Location, actionType: TownyPermission.ActionType) =
        player.player?.let { PlayerCacheUtil.getCachePermission(it, location, location.block.type, actionType) } ?: false
    
}