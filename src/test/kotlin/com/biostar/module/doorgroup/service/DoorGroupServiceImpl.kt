package com.biostar.module.doorgroup.service

import com.biostar.module.doorgroup.dao.DoorGroupRepository
import com.biostar.module.doorgroup.domain.DoorGroup
import com.biostar.module.doorgroup.domain.DoorGroupList
import com.biostar.module.doorgroup.domain.DoorGroupSearchCriteria
import com.biostar.module.nestedset.service.NestedSetService
import com.biostar.module.setting.service.SettingService
import com.biostar.server.util.IConstant
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.*
import io.mockk.junit5.MockKExtension
import javafx.util.Pair
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.context.annotation.ComponentScan
import org.springframework.test.util.ReflectionTestUtils
import java.util.*
import javax.persistence.EntityManager
import javax.persistence.Query

@ExtendWith(MockKExtension::class)
class DoorGroupServiceImplTest : StringSpec({

    val em = mockk<EntityManager>(relaxed = true)
    val doorGroupRepository = mockk<DoorGroupRepository>(relaxed = true)
    val settingService = mockk<SettingService>(relaxed = true)
    val nestedSetService = mockk<NestedSetService>(relaxed = true)

    val doorGroupServiceImpl = DoorGroupServiceImpl().apply {
        ReflectionTestUtils.setField(this, "em", em)
        ReflectionTestUtils.setField(this, "doorGroupRepository", doorGroupRepository)
        ReflectionTestUtils.setField(this, "settingService", settingService)
        ReflectionTestUtils.setField(this, "nestedSetService", nestedSetService)
    }

    "getListOnlyPermission calls getList with includeAllParent = false" {
        // Given:
        val criteria = DoorGroupSearchCriteria()
        val expectedList = DoorGroupList()
        val includeAllParent = false

        // Mock private method call by setting up verify block after invoking target method.
        val capturedCriteria = slot<DoorGroupSearchCriteria>()
        val capturedIncludeAllParent = slot<Boolean>()

        // When:
        doorGroupServiceImpl.getListOnlyPermission(criteria)

        // Then:
        verify {
            ReflectionTestUtils.invokeMethod<DoorGroupList>(doorGroupServiceImpl, "getList", criteria, includeAllParent)
        }
    }

    "getList calls getList with includeAllParent = true" {
        // Given:
        val criteria = DoorGroupSearchCriteria()
        val expectedList = DoorGroupList()
        val includeAllParent = true

        // Mock private method call by setting up verify block after invoking target method.
        val capturedCriteria = slot<DoorGroupSearchCriteria>()
        val capturedIncludeAllParent = slot<Boolean>()

        // When:
        doorGroupServiceImpl.getList(criteria)

        // Then:
        verify {
            ReflectionTestUtils.invokeMethod<DoorGroupList>(doorGroupServiceImpl, "getList", criteria, includeAllParent)
        }
    }

    "remove deletes a door group" {
        // Given:
        val id: Long = 123
        val selectQuery = mockk<Query>()
        val updateQuery = mockk<Query>()
        val groupIdList = mutableListOf<Long>(id)
        val doorGroupDAO = com.biostar.module.doorgroup.dao.DoorGroup()
        doorGroupDAO.setDeleted("N")

        every { em.createNativeQuery(any()) } returns selectQuery andThen updateQuery
        every { selectQuery.resultList } returns listOf(id)
        every { doorGroupRepository.findById(id) } returns Optional.of(doorGroupDAO)
        every { doorGroupRepository.save(any()) } returns doorGroupDAO

        // When:
        doorGroupServiceImpl.remove(id)

        // Then:
        verify {
            doorGroupRepository.findById(id)
            doorGroupRepository.save(doorGroupDAO)
            nestedSetService.updateDoorGroup()
        }

        doorGroupDAO.Deleted shouldBe IConstant.INDICATOR_YES
    }

})