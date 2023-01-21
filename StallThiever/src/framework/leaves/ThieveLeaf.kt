package framework.leaves

import Thiever
import data.Configs
import org.powbot.api.Area
import org.powbot.api.Condition
import org.powbot.api.Tile
import org.powbot.api.rt4.*
import org.powbot.api.script.tree.Leaf


class RunToStall(script: Thiever): Leaf<Thiever>(script, "Running to stall") {
    override fun execute() {
        Movement.walkTo(Configs.startTile)
    }
}

class Steal(script: Thiever): Leaf<Thiever>(script, "Stealing") {
    override fun execute() {
        val x = Configs.stall!![0].tile.x
        val y = Configs.stall!![0].tile.y
        val area = Area(Tile(x+1, y+ 1), Tile(x -1, y -1))
        val stall: GameObject = Objects.stream().within(area).action("Steal-from").first()
        if(stall.valid() && stall.interact("Steal-from")) {
            if(Condition.wait { Players.local().animation() != -1 }) {
                Condition.wait { Players.local().animation() == -1 }
            }
        }
    }
}