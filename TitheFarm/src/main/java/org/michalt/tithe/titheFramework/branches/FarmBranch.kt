package org.michalt.tithe.titheFramework.branches

import org.powbot.api.Area
import org.michalt.tithe.TitheFarm
import org.michalt.tithe.titheData.TitheApi
import org.michalt.tithe.titheData.TitheConfigs
import org.michalt.tithe.titheData.TithePatchStatus
import org.powbot.api.rt4.Inventory
import org.powbot.api.script.tree.Branch
import org.powbot.api.script.tree.TreeComponent
import org.michalt.tithe.titheFramework.leaves.Plant
import org.michalt.tithe.titheFramework.leaves.RefilCans
import org.michalt.tithe.titheFramework.leaves.WaterHarvest
import org.powbot.api.Tile
import org.powbot.api.rt4.Objects
import org.michalt.tithe.titheFramework.leaves.HandleCrash

class NeedToRefillCans(script: TitheFarm): Branch<TitheFarm>(script, "Need to refill cans?") {
    override val failedComponent: TreeComponent<TitheFarm> = hasBeenCrashed(script)
    override val successComponent: TreeComponent<TitheFarm> = RefilCans(script)

    override fun validate(): Boolean {
        script.log.info("Validating branch ${this.name}")
        if(TitheConfigs.usingCan) {
            script.log.info("amount: ${TitheConfigs.newPatchList!!.size}")
            return TitheConfigs.newPatchList!![TitheConfigs.newPatchList!!.size - 1].status == TithePatchStatus.EMPTY &&  !TitheConfigs.goliancersCanFilled  && TitheConfigs.newPatchList.last().status == TithePatchStatus.EMPTY
        }
        return TitheConfigs.newPatchList!![TitheConfigs.newPatchList!!.size - 1].status == TithePatchStatus.EMPTY && Inventory.stream().name("Watering can(8)").count() < 10 &&  TitheConfigs.newPatchList.first().status == TithePatchStatus.EMPTY
    }
}

class CanPlant(script: TitheFarm): Branch<TitheFarm>(script, "Can plant?") {
    override val failedComponent: TreeComponent<TitheFarm> = WaterHarvest(script)
    override val successComponent: TreeComponent<TitheFarm> = Plant(script)

    override fun validate(): Boolean {
        script.log.info("Validating branch ${this.name}")
        return TitheApi.getNextPatch()?.status == TithePatchStatus.EMPTY
    }
}
class hasBeenCrashed(script: TitheFarm): Branch<TitheFarm>(script, "Been crashed?") {
    override val failedComponent: TreeComponent<TitheFarm> = CanPlant(script)
    override val successComponent: TreeComponent<TitheFarm> = HandleCrash(script)

    override fun validate(): Boolean {
        if(TitheConfigs.newPatchList.last().status == TithePatchStatus.EMPTY) {
            TitheConfigs.newPatchList.forEach {
                val x = TitheApi.patchToTile(it.tile).x
                val y = TitheApi.patchToTile(it.tile).y
                val area = Area(Tile(x -2, y -2,0), Tile(x+ 2, y +2, 0))
                if(it.status == TithePatchStatus.EMPTY && Objects.stream().at(TitheApi.patchToTile(it.tile)).action("Water", "Harvest").first().valid()) {
                    return true
                }
            }
        }
        return false
    }
}

