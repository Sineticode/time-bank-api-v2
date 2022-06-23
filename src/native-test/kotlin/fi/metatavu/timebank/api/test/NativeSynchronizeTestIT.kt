package fi.metatavu.timebank.api.test

import fi.metatavu.timebank.api.test.functional.resources.TestKeycloakResource
import fi.metatavu.timebank.api.test.functional.resources.TestMySQLResource
import fi.metatavu.timebank.api.test.functional.resources.TestWiremockResource
import io.quarkus.test.junit.QuarkusIntegrationTest
import io.quarkus.test.common.QuarkusTestResource
import fi.metatavu.timebank.api.test.functional.tests.SynchronizeTest

@QuarkusIntegrationTest
@QuarkusTestResource.List(
    QuarkusTestResource(TestMySQLResource::class),
    QuarkusTestResource(TestWiremockResource::class),
    QuarkusTestResource(TestKeycloakResource::class)
)
class NativeSynchronizeTestIT : SynchronizeTest() {

}