package org.michalt.tithe.titheData

import org.powbot.api.Tile

data class TithePatch(var count: Int, var status: TithePatchStatus, var watered: Boolean, var tile: Tile)
