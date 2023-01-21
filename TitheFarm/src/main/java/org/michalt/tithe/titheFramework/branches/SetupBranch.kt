package org.michalt.tithe.titheFramework.branches

import org.powbot.api.rt4.*
import org.michalt.tithe.TitheFarm
import org.michalt.tithe.titheData.TitheConfigs
import org.michalt.tithe.titheData.TithePatchStatus
import org.powbot.api.script.tree.Branch
import org.powbot.api.script.tree.TreeComponent
import org.michalt.tithe.titheFramework.leaves.DepositFruits
import org.michalt.tithe.titheFramework.leaves.EnterFarm
import org.michalt.tithe.titheFramework.leaves.ExitFarm
import org.michalt.tithe.titheFramework.leaves.GetSeeds
import org.michalt.tithe.titheData.TitheApi

class NeedToGetSeeds(script: TitheFarm): Branch<TitheFarm>(script, "Need to get seeds?") {
    override val failedComponent: TreeComponent<TitheFarm> = EnterFarm(script)
    override val successComponent: TreeComponent<TitheFarm> = GetSeeds(script)

    override fun validate(): Boolean {
        script.log.info("Validating branch ${this.name}")
        return !Inventory.stream().name(TitheConfigs.seed).first().valid() && TitheApi.getNextPatch()!!.status == TithePatchStatus.EMPTY
    }
}

class NeedToExitFarm(script: TitheFarm): Branch<TitheFarm>(script, "Need to exit farm?") {
    override val failedComponent: TreeComponent<TitheFarm> = NeedToDepositFruit(script)
    override val successComponent: TreeComponent<TitheFarm> = ExitFarm(script)

    override fun validate(): Boolean {
        script.log.info("Validating branch ${this.name}")
        return Objects.stream(15).name("Water Barrel").nearest().reachable().first().valid() && !Inventory.stream().filtered { it.name().lowercase().contains("fruit") }.first().valid() && !Inventory.stream().filtered { !it.name().lowercase().contains("dib") && it.name().lowercase().contains("seed") }.first().valid()
                && TitheConfigs.newPatchList.last().status == TithePatchStatus.EMPTY
    }
}

class NeedToDepositFruit(script: TitheFarm): Branch<TitheFarm>(script, "Need to deposit fruit?") {
    override val failedComponent: TreeComponent<TitheFarm> = NeedToGetSeeds(script)
    override val successComponent: TreeComponent<TitheFarm> = DepositFruits(script)

    override fun validate(): Boolean {
        script.log.info("Validating branch ${this.name}")
        return Inventory.stream().filtered { it.name().lowercase().contains("fruit") }.first().valid()
    }
}

class CanStartGame(script: TitheFarm): Branch<TitheFarm>(script, "Can start?") {
    override val failedComponent: TreeComponent<TitheFarm> = NeedToExitFarm(script)
    override val successComponent: TreeComponent<TitheFarm> = NeedToRefillCans(script)

    override fun validate(): Boolean {
        script.log.info("Validating branch ${this.name}")
        return (Inventory.stream().filtered { it.name().contains("seed") }.first().valid() || TitheConfigs.newPatchList!!.last().status != TithePatchStatus.EMPTY) && Objects.stream().name("Water barrel").reachable().first().valid()
    }
}