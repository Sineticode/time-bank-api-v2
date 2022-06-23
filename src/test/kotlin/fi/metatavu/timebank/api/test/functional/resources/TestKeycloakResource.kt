package fi.metatavu.timebank.api.test.functional.resources

import dasniko.testcontainers.keycloak.KeycloakContainer
import io.quarkus.test.common.DevServicesContext
import io.quarkus.test.common.QuarkusTestResourceLifecycleManager

class TestKeycloakResource: QuarkusTestResourceLifecycleManager, DevServicesContext.ContextAware{

    private var containerNetworkId: String? = null

    override fun setIntegrationTestContext(context: DevServicesContext?) {
        containerNetworkId = context?.containerNetworkId().toString()
    }

    override fun start(): MutableMap<String, String> {
        keycloak.start()

        if (containerNetworkId.isNullOrEmpty()) keycloak::withNetworkMode

        var authServerUrl = keycloak.authServerUrl

        if (containerNetworkId.isNullOrEmpty()) {
            authServerUrl = fixAuthServerUrl(authServerUrl)
        }

        val config: MutableMap<String, String> = HashMap()
        config["quarkus.oidc.auth-server-url"] = "$authServerUrl/realms/timebank"
        config["quarkus.oidc.client-id"] = "test"
        config["quarkus.oidc.credentials.secret"] = "401d7df0-085a-405e-abdc-f2e231179178"

        return config
    }

    override fun stop() {
        keycloak.stop()
    }

    private fun fixAuthServerUrl(authServerUrl: String): String {
        val hostPort = "${keycloak.host}:${keycloak.getMappedPort(keycloak.httpPort)}"
        val networkHostPort = "${keycloak.currentContainerInfo.config.hostName}:${keycloak.httpPort}"

        return authServerUrl.replace(hostPort, networkHostPort)
    }

    companion object {
        val keycloak: KeycloakContainer = KeycloakContainer()
            .withRealmImportFile("kc.json")
    }
}