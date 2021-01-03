import java.io.File
import java.io.FileInputStream
import java.util.Properties

object Secrets {
    private const val API_KEY = "API_KEY_MOVIE_DB"

    val apiKey: String = apiKeysProperties().getProperty(API_KEY)

    private fun apiKeysProperties(): Properties {
        val filename = "api_key.properties"
        val file = File(filename)
        if (!file.exists()) {
            throw Error("You need to prepare a file called $filename in the project root directory.\n" +
                    "and contain the API key.")
        }
        val properties = Properties()
        properties.load(FileInputStream(file))
        return properties
    }
}