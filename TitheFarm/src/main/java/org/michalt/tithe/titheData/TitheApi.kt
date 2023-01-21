package org.michalt.tithe.titheData

import org.powbot.api.Area
import org.powbot.api.Tile


class TitheApi {
    companion object {
        var state = TithePatchStatus.EMPTY
        var StartArea: Area = Area(Tile(1791, 3508, 0), Tile(1806, 3492, 0))
        fun getNextPatch(): TithePatch? {
            TitheConfigs.newPatchList!!.forEach {
                if(it.status == TithePatchStatus.EMPTY && state == TithePatchStatus.EMPTY) {
                    return it
                } else if (it.status == TithePatchStatus.SEEDLING && state == TithePatchStatus.SEEDLING) {
                    return it
                } else if (it.status == TithePatchStatus.GROW1 && state == TithePatchStatus.GROW1) {
                    return it
                } else if (it.status == TithePatchStatus.HARVEST && state == TithePatchStatus.HARVEST) {
                    return it
                }
            }
            return null
        }
        fun patchToTile(t: Tile): Tile {
            return Tile(TitheConfigs.baseObjectTile?.x?.minus(t.x)!!, TitheConfigs.baseObjectTile?.y?.minus(t.y)!!,0 )
        }
    }
}