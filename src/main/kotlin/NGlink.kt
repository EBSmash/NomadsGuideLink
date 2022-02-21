import com.lambda.client.event.events.PlayerTravelEvent
import com.lambda.client.module.Category
import com.lambda.client.plugin.api.PluginModule
import com.lambda.client.util.threads.safeListener
import java.io.File
import com.lambda.client.LambdaMod

import com.lambda.client.util.filesystem.FolderUtils
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


   val filename = "NGLink.txt"
   var fileObject = File(filename)


    init {
        onEnable {

            //check if we have a file to write info to

                var fileExists = fileObject.exists()
                    if(fileExists){
                        return@onEnable
                    }
                    else {

                    }
                }
        safeListener<TickEvent> {
            countPos++
        }

        safeListener<PlayerTravelEvent> {
            if(countPos > delay*20) {
                countPos = 0
                File(filename).writeText("P " + Companion.mc.player.posX.toString() + " " + Companion.mc.player.posZ.toString())
            }
        }
    }
}