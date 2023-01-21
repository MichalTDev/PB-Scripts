package org.michalt.tithe

import com.google.common.eventbus.Subscribe
import org.powbot.api.Color
import org.michalt.tithe.titheData.TitheConfigs
import org.michalt.tithe.titheFramework.branches.CanStartGame
import org.powbot.api.Notifications
import org.powbot.api.event.MessageEvent
import org.powbot.api.event.RenderEvent
import org.powbot.api.rt4.Inventory
import org.powbot.api.rt4.Objects
import org.powbot.api.rt4.Players
import org.powbot.api.rt4.walking.model.Skill
import org.powbot.api.script.*
import org.powbot.api.script.paint.PaintBuilder
import org.powbot.api.script.tree.TreeComponent
import org.powbot.api.script.tree.TreeScript
import org.powbot.mobile.drawing.Rendering
import org.powbot.mobile.script.ScriptManager
import org.michalt.tithe.titheData.TitheApi
import org.michalt.tithe.titheData.TithePatchStatus

@ScriptConfiguration(
    name = "Amount",
    description = "How many plants to plant?",
    optionType = OptionType.INTEGER,
    defaultValue = "16"
)

@ScriptManifest(
    name = "PP Tithe Farm",
    version = "1.0",
    description = "Start next to seed table. Have at least 10 watering cans in inventory.",
    singleTapRequired = true,
    category = ScriptCategory.Farming,
    priv = false
)

class TitheFarm : TreeScript() {
    override val rootComponent: TreeComponent<*>
        get() = CanStartGame(this)

    override fun onStart() {
        TitheConfigs.plantAm = getOption("Amount")
        TitheConfigs.plantAmOriginal = getOption("Amount")
        TitheConfigs.newPatchList = TitheConfigs.patches.toMutableList()
        setPatchesAmount(TitheConfigs.plantAmOriginal)
        val golCan = Inventory.stream().name("Gricoller's can").first()

        if (!golCan.valid() && Inventory.stream().filtered { it.name().startsWith("Watering ca") }.count() < 10) {
            Notifications.showNotification("Make sure to have at least 10 watering cans in your inventory.")
            ScriptManager.stop()
        }

        if (golCan.valid()) {
            TitheConfigs.usingCan = true
        }

        if(!TitheApi.StartArea.contains(Players.local())) {
            TitheConfigs.baseObjectTile = Objects.stream().name("Water Barrel").nearest().first().tile
        }
        val paint = PaintBuilder()
            .addString("Status:") { lastLeaf.name }
            .addString("Plants") {
                (if (TitheApi.getNextPatch() != null) {
                    return@addString "${TitheConfigs.newPatchList.size}"
                } else {
                    return@addString "N/A"
                })
            }            .addString("Next plant") {
                (if (TitheApi.getNextPatch() != null) {
                    return@addString "${TitheApi.getNextPatch()!!.count}"
                } else {
                    return@addString "N/A"
                })
            }
            .trackSkill(Skill.Farming).build()
        addPaint(paint)

    }

    @Subscribe
    fun onMessage(messageEvent: MessageEvent) {
        if (messageEvent.message.contains("can is already full")) {
            TitheConfigs.goliancersCanFilled = true
        }
    }

    @ValueChanged(keyName = "Custom")
    fun CustomPlantChanged(valuePassed: Boolean?) {
        updateVisibility("Amount", valuePassed!!)
    }

    @Subscribe
    fun onRender(e: RenderEvent) {
        Rendering.setScale(1.0f)
        Rendering.setColor(Color.WHITE)
        if (!TitheApi.StartArea.contains(Players.local())) {
            TitheConfigs.newPatchList!!.forEach {
                when (it.status) {
                    TithePatchStatus.SEEDLING -> {
                        Rendering.setColor(Color.RED)
                        Rendering.drawPolygon(TitheApi.patchToTile(it.tile).matrix().bounds())
                        Rendering.setColor(Color.WHITE)
                        Rendering.drawString(it.count.toString(), TitheApi.patchToTile(it.tile).matrix().centerPoint().x, TitheApi.patchToTile(it.tile).matrix().centerPoint().y)


                    }

                    TithePatchStatus.GROW1 -> {
                        Rendering.setColor(Color.ORANGE)
                        Rendering.drawPolygon(TitheApi.patchToTile(it.tile).matrix().bounds())
                        Rendering.setColor(Color.WHITE)
                        Rendering.drawString(it.count.toString(), TitheApi.patchToTile(it.tile).matrix().centerPoint().x, TitheApi.patchToTile(it.tile).matrix().centerPoint().y)

                    }

                    TithePatchStatus.GROW2 -> {
                        Rendering.setColor(Color.ORANGE)
                        Rendering.drawPolygon(TitheApi.patchToTile(it.tile).matrix().bounds())
                        Rendering.setColor(Color.WHITE)
                        Rendering.drawString(it.count.toString(), TitheApi.patchToTile(it.tile).matrix().centerPoint().x, TitheApi.patchToTile(it.tile).matrix().centerPoint().y)

                    }

                    TithePatchStatus.HARVEST -> {
                        Rendering.setColor(Color.GREEN)
                        Rendering.drawPolygon(TitheApi.patchToTile(it.tile).matrix().bounds())
                        Rendering.setColor(Color.WHITE)
                        Rendering.drawString(it.count.toString(), TitheApi.patchToTile(it.tile).matrix().centerPoint().x, TitheApi.patchToTile(it.tile).matrix().centerPoint().y)

                    }

                    TithePatchStatus.EMPTY -> {
                        Rendering.setColor(Color.WHITE)
                        Rendering.drawPolygon(TitheApi.patchToTile(it.tile).matrix().bounds())
                        Rendering.setColor(Color.WHITE)
                        Rendering.drawString(it.count.toString(), TitheApi.patchToTile(it.tile).matrix().centerPoint().x, TitheApi.patchToTile(it.tile).matrix().centerPoint().y)
                    }
                }
            }
        }
    }

    fun setPatchesAmount(amm: Int) {
        val amount = amm - 1

        val tempArr = TitheConfigs.patches.toMutableList()
        TitheConfigs.newPatchList = tempArr.slice(0..amount)
        TitheConfigs.plantAm = TitheConfigs.newPatchList!!.size
        log.info("test " + TitheConfigs.newPatchList!!.size.toString())
    }
}

fun main() {
    TitheFarm().startScript("192.168.1.99", false)
}