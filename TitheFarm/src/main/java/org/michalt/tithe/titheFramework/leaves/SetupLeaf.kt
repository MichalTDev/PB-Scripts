package org.michalt.tithe.titheFramework.leaves

import org.michalt.tithe.TitheFarm
import org.michalt.tithe.titheData.TitheConfigs
import org.powbot.api.Condition
import org.powbot.api.rt4.*
import org.powbot.api.rt4.walking.model.Skill
import org.powbot.api.script.tree.Leaf

class EnterFarm(script: TitheFarm) : Leaf<TitheFarm>(script, "Entering farm") {
    override fun execute() {
        script.log.info("getting door")
        val farmDoor = Objects.stream().name("Farm door").nearest().viewable().first()
        script.log.info("interating with door")
        if (farmDoor.valid() && farmDoor.interact("Open")) {
            script.log.info("waiting for door")
            Condition.wait { Objects.stream().name("Water Barrel").viewable().reachable().nearest().first().valid() }
            script.log.info("setting base object")
            TitheConfigs.baseObjectTile = Objects.stream().name("Water Barrel").nearest().first().tile

        }
    }
}

class ExitFarm(script: TitheFarm) : Leaf<TitheFarm>(script, "Exiting farm") {
    override fun execute() {
        script.log.info("getting door 2")
        val farmDoor = Objects.stream().name("Farm door").nearest().viewable().first()
        script.log.info("interacting with door 2")
        if (farmDoor.valid() && farmDoor.interact("Open")) {
            script.log.info("Waiting for door 2")
            Condition.wait { Objects.stream().name("Seed table").nearest().viewable().reachable().first().valid() }
        } else {
            script.log.info("WALKING TO EXIT")
            if(Movement.step(TitheConfigs.baseObjectTile!!)) {
                Condition.wait { Objects.stream().name("Farm door").nearest().viewable().first().valid() }
            }
        }
    }
}

class GetSeeds(script: TitheFarm) : Leaf<TitheFarm>(script, "Getting seeds") {
    override fun execute() {
        script.setPatchesAmount(TitheConfigs.plantAmOriginal)

        script.log.info("getting seeds")
        if (Chat.chatting()) {
            script.log.info("checking for level")
            if (Skills.level(Skill.Farming.index) >= 74) {
                script.log.info("Interacting with seed 1")
                if(Chat.completeChat("Logavano seed (level 74)")) {

                    Condition.wait { Inventory.stream().name("Logavano seed").first().valid() }
                    TitheConfigs.seed = "Logavano seed"
                }
            } else if (Skills.level(Skill.Farming.index) >= 54) {
                if(Chat.completeChat("Bologano seed (level 54)")) {
                    script.log.info("Interacting with seed 2")
                    Condition.wait { Inventory.stream().name("Bologano seed").first().valid() }
                    TitheConfigs.seed = "Bologano seed"
                }
            } else if (Skills.level(Skill.Farming.index) >= 34) {
                if(Chat.completeChat("Golovanova seed (level 34)")) {
                    script.log.info("Interacting with seed 3")
                    Condition.wait { Inventory.stream().name("Golovanova seed").first().valid() }
                    TitheConfigs.seed = "Golovanova seed"
                }
            }
        } else {
            script.log.info("getting seed table")
            val seedTable = Objects.stream().name("Seed table").nearest().viewable().first()
            script.log.info("Interacting with seed table")
            if (seedTable.valid() && seedTable.interact("Search")) {
                Condition.wait( { Chat.canContinue() }, 50, 20)
            }
        }
    }
}

class DepositFruits(script: TitheFarm) : Leaf<TitheFarm>(script, "Depositing fruits") {
    override fun execute() {
        script.log.info("depositing fruits")
        val sack = Objects.stream().name("Sack").viewable().first()
        script.log.info("Inteacting with deposit fruit")
        if(sack.valid() && sack.interact("Deposit")) {
            script.log.info("waiting for fruit")
            Condition.wait { !Inventory.stream().filtered { it.name().lowercase().contains("fruit") }.first().valid() }
        }
    }
}
