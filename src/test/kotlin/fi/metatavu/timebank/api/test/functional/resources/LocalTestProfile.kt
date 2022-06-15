package fi.metatavu.timebank.api.test.functional.resources

import io.quarkus.test.junit.QuarkusTestProfile

/**
 * Local test profile
 */
class LocalTestProfile: QuarkusTestProfile {
    
    override fun getConfigOverrides(): Map<String, String> {
        return mapOf(
            "quarkus.keycloak.devservices.realm-name" to "timebank",
            "quarkus.keycloak.devservices.enabled" to "true",
            "quarkus.keycloak.devservices.realm-path" to "kc.json",
            "forecast.base.url" to ""
        )
    }
}