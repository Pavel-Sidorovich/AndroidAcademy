import java.io.FileInputStream
import java.util.Properties
import org.gradle.api.Project

private const val API_KEY = "API_KEY"
class Secrets(private val rootProject: Project) {

    val apiKey: String = apiKeysProperties().getProperty(API_KEY)

    private fun apiKeysProperties(): Properties {
        val filename = "./api_key.properties"
        val file = rootProject.file(filename)
        if (!file.exists()) {
            throw Error("You need to prepare a file called $filename in the project root directory.\n" +
                    "and contain the API key.")
        }
        val properties = Properties()
        properties.load(FileInputStream(file))
        return properties
    }
}