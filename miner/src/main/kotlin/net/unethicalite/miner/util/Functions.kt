package net.unethicalite.miner.util

import net.runelite.api.Client
import net.runelite.api.TileObject
import net.storm.api.commons.Time
import net.storm.api.entities.Players
import net.storm.api.entities.TileObjects
import net.storm.api.items.Inventory
import net.storm.client.Static
import net.unethicalite.miner.MinerPlugin
import net.unethicalite.miner.States
import java.util.function.BooleanSupplier
import javax.inject.Inject

class Functions {
    @Inject
    lateinit var client: Client

    fun MinerPlugin.sleepDelay(): Long {
        sleepLength = calculation.randomDelay(
            config.sleepWeightedDistribution(),
            config.sleepMin(),
            config.sleepMax(),
            config.sleepDeviation(),
            config.sleepTarget()
        )
        return sleepLength
    }

    fun MinerPlugin.getState(): States {
        if(chinBreakHandler.shouldBreak(this)){
            return States.HANDLE_BREAK
        }
        if(Inventory.isFull()){
            return States.DROP_INVENTORY
        }
        if(!Players.getLocal().isAnimating){
            val rock: TileObject? = TileObjects.getNearest { config.rockType().rockId.contains(it.id) && it.distanceTo(startLocation) < config.radius() }
                ?: return States.UNKNOWN
            return States.MINE_ROCK
        }
        return States.UNKNOWN
    }

}