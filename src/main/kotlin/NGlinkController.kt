import com.lambda.client.plugin.api.Plugin

internal object NGlinkController: Plugin() {

    override fun onLoad() {
        // Load any modules, commands, or HUD elements here

    }

    override fun onUnload() {
        // Here you can unregister threads etc...
    }
}