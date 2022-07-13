package fi.metatavu.timebank.api.keycloak

import org.eclipse.microprofile.config.inject.ConfigProperty
import org.keycloak.admin.client.Keycloak
import org.keycloak.admin.client.KeycloakBuilder
import org.keycloak.admin.client.resource.UsersResource
import javax.enterprise.context.ApplicationScoped

/**
 * Class for Keycloak controller
 */
@ApplicationScoped
class KeycloakController {

    @ConfigProperty(name = "quarkus.oidc.auth-server-url")
    private lateinit var authServerUrl: String

    @ConfigProperty(name = "keycloak.admin.username")
    private lateinit var adminUsername: String

    @ConfigProperty(name = "keycloak.admin.password")
    private lateinit var adminPassword: String

    @ConfigProperty(name = "keycloak.client.id")
    private lateinit var clientId: String

    @ConfigProperty(name = "keycloak.client.secret")
    private lateinit var clientSecret: String

    @ConfigProperty(name = "keycloak.realm")
    private lateinit var realm: String

    /**
     * Gets minimumBillableRate attribute for Person
     * If not set will return default value of 50 (%)
     *
     * @return Int minimumBillableRate
     */
    fun getUsersMinimumBillableRate(username: String): Int {
        val user = getUserResources()?.search(username)

        if (user.isNullOrEmpty()) return 50

        return try {
            user.first().attributes["minimumBillableRate"]!!.first().toInt()
        } catch (e: NullPointerException) {
            50
        }
    }

    /**
     * Gets Keycloak UsersResource
     * e.g. list of Keycloak Users
     *
     * @return UsersResource
     */
    private fun getUserResources(): UsersResource? {
        val keycloakClient = getKeycloakClient()
        val foundRealm = keycloakClient.realm(realm) ?: return null

        return foundRealm.users()
    }

    /**
     * Builds a Keycloak Admin Client
     *
     * @return Keycloak client
     */
    private fun getKeycloakClient(): Keycloak {
        return KeycloakBuilder.builder()
            .serverUrl(authServerUrl.substringBeforeLast("/").substringBeforeLast("/"))
            .realm(realm)
            .clientId(clientId)
            .clientSecret(clientSecret)
            .grantType("client_credentials")
            .username(adminUsername)
            .password(adminPassword)
            .build()
    }
}