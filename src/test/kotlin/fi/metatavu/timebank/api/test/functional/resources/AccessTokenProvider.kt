package fi.metatavu.timebank.api.test.functional.resources

import io.quarkus.test.keycloak.client.KeycloakTestClient

/**
 *  Class for providing access tokens
 */
class AccessTokenProvider {

    /**
     * Gets a user's access token
     *
     * @param userName user
     * @return user's access token
     */
     fun getAccessToken(userName: String): String {
        val keycloakClient = KeycloakTestClient()
        return keycloakClient.getAccessToken(userName)
    }
}