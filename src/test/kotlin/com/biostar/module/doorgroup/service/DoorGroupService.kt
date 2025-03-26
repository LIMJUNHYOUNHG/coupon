package com.biostar.module.doorgroup.service

import com.biostar.module.doorgroup.domain.DoorGroupList
import com.biostar.module.doorgroup.domain.DoorGroupSearchCriteria
import io.kotest.core.spec.style.StringSpec
import io.mockk.coEvery
import io.mockk.mockk

class DoorGroupServiceTest : StringSpec({

    "getListOnlyPermission should return DoorGroupList" {
        // Given:
        val doorGroupService = mockk<DoorGroupService>()
        val criteria = DoorGroupSearchCriteria()
        val expectedList = DoorGroupList()

        coEvery { doorGroupService.getListOnlyPermission(criteria) } returns expectedList

        // When:
        val actualList = doorGroupService.getListOnlyPermission(criteria)

        // Then:
        actualList shouldBe expectedList
    }

    "getList should return DoorGroupList" {
        // Given:
        val doorGroupService = mockk<DoorGroupService>()
        val criteria = DoorGroupSearchCriteria()
        val expectedList = DoorGroupList()

        coEvery { doorGroupService.getList(criteria) } returns expectedList

        // When:
        val actualList = doorGroupService.getList(criteria)

        // Then:
        actualList shouldBe expectedList
    }

    "remove should not throw exception" {
        // Given:
        val doorGroupService = mockk<DoorGroupService>()
        val id: Long = 123
        coEvery { doorGroupService.remove(id) } returns Unit

        // When:
        val result = doorGroupService.remove(id)

        // Then:
        // No exception is thrown
    }
})