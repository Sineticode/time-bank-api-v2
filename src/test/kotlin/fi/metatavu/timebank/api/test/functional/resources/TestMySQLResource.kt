package fi.metatavu.timebank.api.test.functional.resources

import io.quarkus.test.common.QuarkusTestResourceLifecycleManager
import org.testcontainers.containers.MySQLContainer
import javax.enterprise.context.ApplicationScoped

internal class SpecifiedMySQLContainer(image: String): MySQLContainer<SpecifiedMySQLContainer>(image)

/**
 * Quarkus test resource for providing MySQL database
 *
 * @author Jari Nykänen
 */
@ApplicationScoped
class TestMySQLResource: QuarkusTestResourceLifecycleManager {

    private var isDbRunning = false

    private val db: MySQLContainer<*> = SpecifiedMySQLContainer("mysql:latest")
        .withDatabaseName(DATABASE)
        .withUsername(USERNAME)
        .withPassword(PASSWORD)
        .withCommand(
            "--lower_case_table_names=1"
        )

    override fun start(): Map<String, String> {
        db.start()
        isDbRunning = true
        val config: MutableMap<String, String> = HashMap()
        config["quarkus.datasource.username"] = USERNAME
        config["quarkus.datasource.password"] = PASSWORD
        config["quarkus.datasource.jdbc.url"] = db.jdbcUrl
        config["quarkus.datasource.reactive.url"] = db.jdbcUrl.replace("jdbc:", "")
        return config
    }

    fun truncateDatabase() {
        db.execInContainer("TRUNCATE TABLE timeentry")
    }

    override fun stop() {
        db.stop()
        isDbRunning = false
    }

    companion object {
        const val DATABASE = "timebank-test"
        const val USERNAME = "root"
        const val PASSWORD = "root"
    }
}