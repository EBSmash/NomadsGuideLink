import com.lambda.client.event.events.PlayerTravelEvent
import com.lambda.client.module.Category
import com.lambda.client.plugin.api.PluginModule
import com.lambda.client.util.threads.safeListener
import java.io.File

import com.lambda.client.LambdaMod

import com.lambda.client.event.events.PacketEvent
import com.lambda.client.util.TickTimer
import com.lambda.client.util.TimeUnit
import com.lambda.client.util.filesystem.FolderUtils
import com.lambda.client.util.graphics.ProjectionUtils
import com.lambda.client.util.threads.defaultScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import net.minecraft.entity.projectile.ProjectileHelper
import net.minecraft.init.Items
import net.minecraft.network.play.client.*
import net.minecraft.util.math.BlockPos
import net.minecraftforge.event.entity.ProjectileImpactEvent
import net.minecraftforge.event.world.WorldEvent
import net.minecraftforge.fml.common.gameevent.TickEvent
import scala.tools.nsc.doc.model.Public

internal object NGlink : PluginModule(
    name = "NomadsGuideLink",
    category = Category.MISC,
    description = "Links information to nomads guide application",
    pluginMain = NGlinkController
) {

   private val location by setting("location", true)
   private val pearls by setting("Pearls", true)
   private val logger by setting("Stash Logger", false)
   private val delay by setting("Delay", 5.0f, 0.0f..30.0f, 0.25f, description = "Update Delay in seconds")
   private var countPos = 0
   private val timer = TickTimer(TimeUnit.SECONDS)


   val FLocation = "NGLinkLocation.txt"
   var fileObject1 = File(FLocation)
   val FRotation = "NGLinkRotation.txt"
   var fileObject2 = File(FRotation)
   val FPearls = "NGLinkPearls.txt"
   var fileObject3 = File(FRotation)



    init {
        onEnable {
            //check if we have a file to write info to
                var fileExists = fileObject1.exists() && fileObject2.exists() && fileObject3.exists()
                    if(fileExists){
                        return@onEnable
                    }
                    else {
                        File(FLocation).createNewFile()
                        File(FRotation).createNewFile()
                        File(FPearls).createNewFile()
                    }
                }
        //timer
        safeListener<TickEvent.ClientTickEvent> {
            countPos++
        }
        //Wait for Travel
        safeListener<PlayerTravelEvent> {
            if(countPos > delay*20) {
                countPos = 0
                File(FLocation).writeText("L " + Companion.mc.player.posX.toString() + " " + Companion.mc.player.posZ.toString())
                File(FRotation).writeText("R " + mc.player.rotationYaw.toString())
            }
        }
        //List for Packet Receive
        safeListener<PacketEvent.Receive> {
            //Check for pearl setting
            if(pearls){
                //check if packet is CPacketPlayerDigging and is using an item
                if(it.packet is CPacketPlayerTryUseItem)
                    //if im holding a Pearl then write to file
                    if(mc.player.inventory.currentItem.equals(Items.ENDER_PEARL))
                        File(FPearls).writeText("P " + Companion.mc.player.posX.toString() + " " + Companion.mc.player.posZ.toString())
                    }
                }
            }
        }