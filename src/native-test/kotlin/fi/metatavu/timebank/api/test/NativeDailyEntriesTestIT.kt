package fi.metatavu.timebank.api.test

import fi.metatavu.timebank.api.test.functional.resources.TestMockResource
import fi.metatavu.timebank.api.test.functional.resources.TestMySQLResource
import io.quarkus.test.junit.QuarkusIntegrationTest
import io.quarkus.test.common.QuarkusTestResource
import fi.metatavu.timebank.api.test.functional.tests.DailyEntriesTest

@QuarkusIntegrationTest
@QuarkusTestResource.List(
    QuarkusTestResource(TestMySQLResource::class),
    QuarkusTestResource(TestMockResource::class)
)
class NativeDailyEntriesTestIT : DailyEntriesTest() {

}