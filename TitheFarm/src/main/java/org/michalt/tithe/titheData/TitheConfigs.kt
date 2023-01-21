package org.michalt.tithe.titheData

import org.powbot.api.Tile

class TitheConfigs {
    companion object {
        var plantAmOriginal: Int = 0
        val patches: Array<TithePatch> = arrayOf<TithePatch>(
            TithePatch(1, TithePatchStatus.EMPTY, false, Tile(-7, 11, 0)),
            TithePatch(2, TithePatchStatus.EMPTY, false, Tile(-2, 11, 0)),
            TithePatch(3, TithePatchStatus.EMPTY, false, Tile(-2, 8, 0)),
            TithePatch(4, TithePatchStatus.EMPTY, false, Tile(-7, 8, 0)),
            TithePatch(5, TithePatchStatus.EMPTY, false, Tile(-7, 5, 0)),
            TithePatch(6, TithePatchStatus.EMPTY, false, Tile(-2, 5, 0)),
            TithePatch(7, TithePatchStatus.EMPTY, false, Tile(-2, 2, 0)),
            TithePatch(8, TithePatchStatus.EMPTY, false, Tile(-7, 2, 0)),
            TithePatch(9, TithePatchStatus.EMPTY, false, Tile(-7, -4, 0)),
            TithePatch(10, TithePatchStatus.EMPTY, false, Tile(-2, -4, 0)),
            TithePatch(11, TithePatchStatus.EMPTY, false, Tile(-2, -7, 0)),
            TithePatch(12, TithePatchStatus.EMPTY, false, Tile(-7, -7, 0)),
            TithePatch(13, TithePatchStatus.EMPTY, false, Tile(-7, -10, 0)),
            TithePatch(14, TithePatchStatus.EMPTY, false, Tile(-2, -10, 0)),
            TithePatch(15, TithePatchStatus.EMPTY, false, Tile(-2, -13, 0)),
            TithePatch(16, TithePatchStatus.EMPTY, false, Tile(-7, -13, 0)),
            TithePatch(17, TithePatchStatus.EMPTY, false, Tile(-12, -13, 0)),
            TithePatch(18, TithePatchStatus.EMPTY, false, Tile(-12, -10, 0)),
            TithePatch(19, TithePatchStatus.EMPTY, false, Tile(-12, -7, 0)),
            TithePatch(20, TithePatchStatus.EMPTY, false, Tile(-12, -4, 0))
        )

        val patchesSecondRow: Array<TithePatch> = arrayOf(
            TithePatch(1, TithePatchStatus.EMPTY, false, Tile(-17, 11, 0)),
            TithePatch(2, TithePatchStatus.EMPTY, false, Tile(-17, 11, 0)),
            TithePatch(3, TithePatchStatus.EMPTY, false, Tile(-17, 8, 0)),
            TithePatch(4, TithePatchStatus.EMPTY, false, Tile(-22, 8, 0)),
            TithePatch(5, TithePatchStatus.EMPTY, false, Tile(-22, 5, 0)),
            TithePatch(6, TithePatchStatus.EMPTY, false, Tile(-17, 5, 0)),
            TithePatch(7, TithePatchStatus.EMPTY, false, Tile(-17, 2, 0)),
            TithePatch(8, TithePatchStatus.EMPTY, false, Tile(-22, 2, 0)),
            TithePatch(9, TithePatchStatus.EMPTY, false, Tile(-22, -4, 0)),
            TithePatch(10, TithePatchStatus.EMPTY, false, Tile(-17, -4, 0)),
            TithePatch(11, TithePatchStatus.EMPTY, false, Tile(-17, -7, 0)),
            TithePatch(12, TithePatchStatus.EMPTY, false, Tile(-22, -7, 0)),
            TithePatch(13, TithePatchStatus.EMPTY, false, Tile(-22, -10, 0)),
            TithePatch(14, TithePatchStatus.EMPTY, false, Tile(-17, -10, 0)),
            TithePatch(15, TithePatchStatus.EMPTY, false, Tile(-17, -13, 0)),
            TithePatch(16, TithePatchStatus.EMPTY, false, Tile(-22, -13, 0)),
            TithePatch(17, TithePatchStatus.EMPTY, false, Tile(-12, -13, 0)),
            TithePatch(18, TithePatchStatus.EMPTY, false, Tile(-12, -10, 0)),
            TithePatch(19, TithePatchStatus.EMPTY, false, Tile(-12, -7, 0)),
            TithePatch(20, TithePatchStatus.EMPTY, false, Tile(-12, -4, 0))
        )

        var newPatchList: List<TithePatch> = listOf()

        var baseObjectTile: Tile? = null
        var seed = "Bologano seed"
        var goliancersCanFilled = false;
        var usingCan = false;
        var plantAm = 20;

    }


}