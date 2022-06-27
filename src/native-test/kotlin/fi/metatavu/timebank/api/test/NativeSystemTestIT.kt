package fi.metatavu.timebank.api.test

import fi.metatavu.timebank.api.test.functional.resources.TestKeycloakResource
import fi.metatavu.timebank.api.test.functional.resources.TestMySQLResource
import fi.metatavu.timebank.api.test.functional.resources.TestWiremockResource
import fi.metatavu.timebank.api.test.functional.tests.SystemTest
import io.quarkus.test.common.QuarkusTestResource
import io.quarkus.test.junit.QuarkusIntegrationTest


@QuarkusIntegrationTest
@QuarkusTestResource.List(
    QuarkusTestResource(TestMySQLResource::class),
    QuarkusTestResource(TestWiremockResource::class),
    QuarkusTestResource(TestKeycloakResource::class)
)
class NativeSystemTestIT : SystemTest() {

}