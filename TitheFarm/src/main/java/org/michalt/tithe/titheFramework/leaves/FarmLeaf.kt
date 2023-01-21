package org.michalt.tithe.titheFramework.leaves

import org.michalt.tithe.TitheFarm
import org.michalt.tithe.titheData.TitheApi
import org.michalt.tithe.titheData.TitheConfigs
import org.michalt.tithe.titheData.TithePatchStatus
import org.powbot.api.*
import org.powbot.api.rt4.*
import org.powbot.api.script.tree.Leaf
import org.powbot.mobile.script.ScriptManager


class Plant(script: TitheFarm) : Leaf<TitheFarm>(script, "Planting") {
    override fun execute() {
        if(TitheConfigs.newPatchList.first().status == TithePatchStatus.EMPTY && Inventory.stream().name(TitheConfigs.seed).first().stackSize() < TitheConfigs.plantAmOriginal && TitheConfigs.plantAm == TitheConfigs.plantAmOriginal) {
            script.setPatchesAmount(Inventory.stream().name(TitheConfigs.seed).first().stackSize())
        }
        if(TitheApi.getNextPatch() == null) {
            script.log.info("Next patch is null")
            return
        }
        val patch = TitheApi.getNextPatch()!!
        val tile = TitheApi.patchToTile(patch.tile)

        if (tile.distanceRounded() > 8 && Movement.step(tile)) {
            Condition.wait({ tile.distanceRounded() <= 8 }, 50, 30)
        } else {

            val obj = Objects.stream().within(expandTile(tile)).name("Tithe patch").first().downscale(true)

            if (!Inventory.selectedItem().valid()) {
                Inventory.stream().name(TitheConfigs.seed).first().interact("Use")
            }
            obj.click()
            Condition.wait({ Objects.stream().within(expandTile(tile)).filtered { it.name.contains("seedling") }.first().valid() }, 50, 400)
            val waterObj = Objects.stream().within(expandTile(tile)).filtered { it.name.contains("seedling") }.first().downscale(true)
            if (waterObj.valid() && waterObj.interact("Water")) {
                if (patch.count != TitheConfigs.plantAm) {
                    Inventory.stream().name(TitheConfigs.seed).first().interact("Use")
                }
                Condition.wait({ Players.local().animation() == 2293 }, 50, 100)
                if (TitheApi.getNextPatch() != null) {
                    TitheApi.getNextPatch()!!.status = TithePatchStatus.SEEDLING
                    if (patch.count == TitheConfigs.plantAm) {
                        TitheApi.state = TithePatchStatus.SEEDLING
                    }
                }
                Npcs.stream().first()
            }
        }
    }
}

class WaterHarvest(script: TitheFarm) : Leaf<TitheFarm>(script, "Watering/Harvesting") {
    override fun execute() {
        if(TitheApi.getNextPatch() == null) {
            script.log.info("Next patch is null 1")

            return
        }
        val patch = TitheApi.getNextPatch()!!
        val tile = TitheApi.patchToTile(patch.tile)

        if (tile.distanceRounded() > 8) {
            Movement.step(tile)
            Condition.wait({ tile.distanceRounded() <= 8 }, 100, 23)
        } else {
            val obj = Objects.stream().within(expandTile(tile)).action("Water", "Harvest").first().downscale(true)
            if (obj.valid() && obj.actions().contains("Water")) {
                obj.interact("Water")
                if (TitheApi.getNextPatch()!!.status == TithePatchStatus.GROW1) {
                    TitheApi.getNextPatch()!!.status = TithePatchStatus.HARVEST
                    if (patch.count == TitheConfigs.plantAm) {
                        TitheApi.state = TithePatchStatus.HARVEST
                    }
                } else {
                    TitheApi.getNextPatch()!!.status = TithePatchStatus.GROW1
                    if (patch.count == TitheConfigs.plantAm) {
                        TitheApi.state = TithePatchStatus.GROW1
                    }
                }

                Condition.wait({Players.local().inMotion() }, 50, 100)
                Condition.wait({ Players.local().animation() == 2293 }, 50, 100)


            } else {
                if(obj.name.lowercase().contains("blighted")) {
                    TitheApi.getNextPatch()!!.status = TithePatchStatus.EMPTY
                    if (patch.count == TitheConfigs.plantAm) {
                        TitheApi.state = TithePatchStatus.EMPTY
                    }
                } else {
                    if (obj.interact("Harvest")
                    ) {
                        TitheApi.getNextPatch()!!.status = TithePatchStatus.EMPTY
                        if (patch.count == TitheConfigs.plantAm) {
                            TitheApi.state = TithePatchStatus.EMPTY
                            TitheConfigs.goliancersCanFilled = false
                        }
                        Condition.wait({ Players.local().inMotion() }, 50, 100)
                        Condition.wait({ Players.local().animation() == 830 }, 50, 100)
                    }
                }
            }
        }
    }
}



class RefilCans(script: TitheFarm) : Leaf<TitheFarm>(script, "Refilling can") {
    override fun execute() {
        val water = Objects.stream().name("Water Barrel").at(TitheConfigs.baseObjectTile!!).viewable().nearest().first()
        if (!water.valid()) {
            if (Movement.step(TitheConfigs.baseObjectTile!!)) {
                Condition.wait { Objects.stream().name("Water Barrel").nearest().viewable().first().valid() }
            }
        } else {
            if (water.distanceRounded() > 8) {
                if (Movement.step(water.tile)) {
                    Condition.wait { water.distanceRounded() <= 8 }
                }
            } else {
                val golCan = Inventory.stream().name("Gricoller's can").first()
                if(golCan.valid()) {
                    if(golCan.interact("Use") && water.interact("Use")) {
                        Condition.wait({ Players.local().inMotion() }, 200, 100)
                        Condition.wait({ !Players.local().inMotion() }, 200, 100)
                        TitheConfigs.goliancersCanFilled = true
                    }
                } else {
                    val can = Inventory.stream().name("Watering can","Watering can(1)","Watering can(2)","Watering can(3)","Watering can(4)","Watering can(5)","Watering can(6)","Watering can(7)").first()
                    if(can.valid() && can.interact("Use") && water.interact("Use", false)) {
                        Condition.wait({ Inventory.stream().name("Watering can(8)").count() >= 10 }, 200, 500)
                    }
                }
            }
        }
    }
}

class HandleCrash(script: TitheFarm) : Leaf<TitheFarm>(script, "Handling crash") {
    override fun execute() {
        Notifications.showNotification("crashed")
        ScriptManager.stop()
    }
}
private fun expandTile(tile: Tile): Area {
    return Area(Tile(tile.x + 1, tile.y + 1), Tile(tile.x - 1, tile.y - 1, 0))
}
