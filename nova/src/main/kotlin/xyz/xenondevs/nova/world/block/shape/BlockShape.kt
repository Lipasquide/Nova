package xyz.xenondevs.nova.world.block.shape

import net.kyori.adventure.key.Key
import xyz.xenondevs.nova.world.block.state.property.DefaultScopedBlockStateProperties

enum class BlockShape(
    val id: String,
    val displayName: String,
    val properties: List<ScopedBlockStateProperty<*>>,
    val hasTileEntity: Boolean = true,
    val hardnessMultiplier: Double = 1.0,
    val requiresTool: Boolean = true
) {
    // ============ TEMEL YAPI BLOKLARI ============
    STAIRS("stairs", "Basamak", listOf(
        DefaultScopedBlockStateProperties.FACING_HORIZONTAL,
        DefaultScopedBlockStateProperties.HALF,
        DefaultScopedBlockStateProperties.WATERLOGGED
    ), true, 1.5),

    SLAB("slab", "Levha", listOf(
        DefaultScopedBlockStateProperties.TYPE,
        DefaultScopedBlockStateProperties.WATERLOGGED
    ), true, 1.0),

    WALL("wall", "Duvar", listOf(
        DefaultScopedBlockStateProperties.UP,
        DefaultScopedBlockStateProperties.NORTH_WALL,
        DefaultScopedBlockStateProperties.EAST_WALL,
        DefaultScopedBlockStateProperties.SOUTH_WALL,
        DefaultScopedBlockStateProperties.WEST_WALL,
        DefaultScopedBlockStateProperties.WATERLOGGED
    ), true, 1.0),

    // ============ ÇİTLER VE KAPILAR ============
    FENCE("fence", "Çit", listOf(
        DefaultScopedBlockStateProperties.NORTH,
        DefaultScopedBlockStateProperties.EAST,
        DefaultScopedBlockStateProperties.SOUTH,
        DefaultScopedBlockStateProperties.WEST,
        DefaultScopedBlockStateProperties.WATERLOGGED
    ), true, 1.0),

    FENCE_GATE("fence_gate", "Çit Kapısı", listOf(
        DefaultScopedBlockStateProperties.FACING_HORIZONTAL,
        DefaultScopedBlockStateProperties.OPEN,
        DefaultScopedBlockStateProperties.POWERED,
        DefaultScopedBlockStateProperties.IN_WALL,
        DefaultScopedBlockStateProperties.WATERLOGGED
    ), true, 1.0),

    DOOR("door", "Kapı", listOf(
        DefaultScopedBlockStateProperties.FACING_HORIZONTAL,
        DefaultScopedBlockStateProperties.HALF,
        DefaultScopedBlockStateProperties.HINGE,
        DefaultScopedBlockStateProperties.OPEN,
        DefaultScopedBlockStateProperties.POWERED,
        DefaultScopedBlockStateProperties.WATERLOGGED
    ), true, 1.0),

    TRAPDOOR("trapdoor", "Kapak", listOf(
        DefaultScopedBlockStateProperties.FACING,
        DefaultScopedBlockStateProperties.HALF,
        DefaultScopedBlockStateProperties.OPEN,
        DefaultScopedBlockStateProperties.POWERED,
        DefaultScopedBlockStateProperties.WATERLOGGED
    ), true, 1.0),

    // ============ REDSTONE BİLEŞENLERİ ============
    BUTTON("button", "Buton", listOf(
        DefaultScopedBlockStateProperties.FACE,
        DefaultScopedBlockStateProperties.FACING,
        DefaultScopedBlockStateProperties.POWERED
    ), true, 0.5, false),

    LEVER("lever", "Şalter", listOf(
        DefaultScopedBlockStateProperties.FACE,
        DefaultScopedBlockStateProperties.FACING,
        DefaultScopedBlockStateProperties.POWERED
    ), true, 0.5, false),

    PRESSURE_PLATE("pressure_plate", "Baskı Plakası", listOf(
        DefaultScopedBlockStateProperties.POWERED
    ), true, 0.5, false),

    // ============ IŞIK KAYNAKLARI ============
    TORCH("torch", "Meşale", listOf(), true, 0.0, false),

    WALL_TORCH("wall_torch", "Duvar Meşalesi", listOf(
        DefaultScopedBlockStateProperties.FACING_HORIZONTAL
    ), true, 0.0, false),

    LANTERN("lantern", "Fener", listOf(
        DefaultScopedBlockStateProperties.HANGING,
        DefaultScopedBlockStateProperties.WATERLOGGED
    ), true, 3.5),

    SOUL_LANTERN("soul_lantern", "Ruh Feneri", listOf(
        DefaultScopedBlockStateProperties.HANGING,
        DefaultScopedBlockStateProperties.WATERLOGGED
    ), true, 3.5),

    CAMPFIRE("campfire", "Kamp Ateşi", listOf(
        DefaultScopedBlockStateProperties.FACING_HORIZONTAL,
        DefaultScopedBlockStateProperties.LIT,
        DefaultScopedBlockStateProperties.WATERLOGGED
    ), true, 2.0, false),

    SOUL_CAMPFIRE("soul_campfire", "Ruh Kamp Ateşi", listOf(
        DefaultScopedBlockStateProperties.FACING_HORIZONTAL,
        DefaultScopedBlockStateProperties.LIT,
        DefaultScopedBlockStateProperties.WATERLOGGED
    ), true, 2.0, false),

    // ============ YATAKLAR ============
    BED("bed", "Yatak", listOf(
        DefaultScopedBlockStateProperties.FACING_HORIZONTAL,
        DefaultScopedBlockStateProperties.OCCUPIED,
        DefaultScopedBlockStateProperties.PART
    ), true, 0.2, false),

    // ============ TABELA ============
    SIGN("sign", "Tabela", listOf(
        DefaultScopedBlockStateProperties.FACING_HORIZONTAL,
        DefaultScopedBlockStateProperties.WATERLOGGED
    ), true, 1.0, false),

    WALL_SIGN("wall_sign", "Duvar Tabelası", listOf(
        DefaultScopedBlockStateProperties.FACING_HORIZONTAL,
        DefaultScopedBlockStateProperties.WATERLOGGED
    ), true, 1.0, false),

    HANGING_SIGN("hanging_sign", "Asılı Tabela", listOf(
        DefaultScopedBlockStateProperties.FACING_HORIZONTAL,
        DefaultScopedBlockStateProperties.ATTACHED,
        DefaultScopedBlockStateProperties.WATERLOGGED
    ), true, 1.0, false),

    // ============ DİĞER ============
    CARPET("carpet", "Halı", listOf(
        DefaultScopedBlockStateProperties.WATERLOGGED
    ), false, 0.1, false),

    CHAIN("chain", "Zincir", listOf(
        DefaultScopedBlockStateProperties.AXIS,
        DefaultScopedBlockStateProperties.WATERLOGGED
    ), true, 5.0),

    LADDER("ladder", "Merdiven", listOf(
        DefaultScopedBlockStateProperties.FACING_HORIZONTAL,
        DefaultScopedBlockStateProperties.WATERLOGGED
    ), true, 0.4, false),

    ANVIL("anvil", "Örs", listOf(
        DefaultScopedBlockStateProperties.FACING_HORIZONTAL
    ), true, 5.0),

    GRINDSTONE("grindstone", "Bileği Taşı", listOf(
        DefaultScopedBlockStateProperties.FACING_HORIZONTAL,
        DefaultScopedBlockStateProperties.PART
    ), true, 2.0),

    STONECUTTER("stonecutter", "Taş Kesici", listOf(
        DefaultScopedBlockStateProperties.FACING_HORIZONTAL
    ), true, 3.5);

    val key: Key = Key.key("nova", id)
}
