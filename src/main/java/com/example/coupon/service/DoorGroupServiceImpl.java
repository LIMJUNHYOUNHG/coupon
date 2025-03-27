package com.example.coupon.service;

package com.biostar.module.doorgroup.service;

import com.biostar.module.common.domain.ParentIdObject;
import com.biostar.module.doorgroup.dao.DoorGroupRepository;
import com.biostar.module.doorgroup.domain.DoorGroup;
import com.biostar.module.doorgroup.domain.DoorGroupList;
import com.biostar.module.doorgroup.domain.DoorGroupSearchCriteria;
import com.biostar.module.nestedset.service.NestedSetService;
import com.biostar.module.setting.service.SettingService;
import com.biostar.server.util.IConstant;
import com.biostar.server.util.SqlUtil;
import javafx.util.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service("DoorGroupService")
@ComponentScan(basePackages = {
        "com.biostar.module.door.dao",
        "com.biostar.module.doorgroup.dao"
})
public class DoorGroupServiceImpl implements DoorGroupService {

    private static Logger logger = LoggerFactory.getLogger(DoorGroupServiceImpl.class);

    @PersistenceContext
    private EntityManager em;

    @Autowired
    DoorGroupRepository doorGroupRepository;

    @Autowired
    SettingService settingService;
    @Autowired
    NestedSetService nestedSetService;

    final int INDEX_ID = 0;
    final int INDEX_NAME = INDEX_ID + 1;
    final int INDEX_DEPTH = INDEX_NAME + 1;
    final int INDEX_PARENT_DOOR_GROUP_ID = INDEX_DEPTH + 1;
    final int INDEX_PARENT_DOOR_GROUP_NAME = INDEX_PARENT_DOOR_GROUP_ID + 1;
    final int INDEX_DELETED = INDEX_PARENT_DOOR_GROUP_NAME + 1;


    private DoorGroupList getList(DoorGroupSearchCriteria criteria, Boolean includeAllParent) {
        DoorGroupList list = new DoorGroupList();

        String nativeSql = "SELECT " +
                "    drgr.drgruid as doorGroupId, " +
                "    drgr.nm as doorGroupName, " +
                "    drgr.dep as depth, " +
                "    drgr.parentdrgruid as parentDoorGroupId , " +
                "    pdrgr.nm as parentDoorGroupName , " +
                "    drgr.del as deleted ";

        String sqlSuffix = " FROM " +
                "    T_DRGR as drgr " +
                "LEFT OUTER JOIN T_DRGR as pdrgr on drgr.parentdrgruid=pdrgr.drgruid " +
                " WHERE 1=1";

        String countNativeSql = "SELECT count(*) ";
        List<Pair<String, String>> bindParams = new ArrayList<>();

        if (criteria == null || !criteria.getIncludeDeleted()) {
            sqlSuffix += " AND (drgr.del is null OR  drgr.del = 'N') ";
        }
        if (criteria == null || criteria.getDepth() != null) {
            sqlSuffix += " AND drgr.dep = " + criteria.getDepth();
        }
        if (criteria != null && criteria.getSearchText() != null) {
            sqlSuffix += " AND (drgr.nm like Concat('%',:searchTxt,'%') " +
                    " or pdrgr.nm like Concat('%',:searchTxt,'%') " +
                    ")";
            bindParams.add(new Pair<>("searchTxt", criteria.getSearchText().trim()));
        }

        if (criteria != null && criteria.getGroupIdList().size() > 0) {
            sqlSuffix += " AND drgr.drgruid in " +
                    " ( " +
                    SqlUtil.buildNestedGroupSelectSqlIncludingSelf(IConstant.NESTED_GROUP_TABLE_NAME_DOOR_GROUP, criteria.getGroupIdList(), false, includeAllParent) +
                    " ) ";
        }

        String orderByClause = "";
        if (criteria.getOrderBy() != null) {
            String[] temp = criteria.getOrderBy().split(":");
            String jsonProperyName = temp[0];
            String descendingValue = temp[1];
            String dbColumnName = null;
            if (IConstant.JSON_DOOR_GROUP_ID.equals(jsonProperyName)) {
                dbColumnName = "doorGroupId";
            } else if (IConstant.JSON_DOOR_GROUP_NAME.equals(jsonProperyName)) {
                dbColumnName = "doorGroupName";
            } else if (IConstant.JSON_DOOR_GROUP_DEPTH.equals(jsonProperyName)) {
                dbColumnName = "depth";
            }
            if (dbColumnName != null) {
                if (IConstant.BOOLEAN_TRUE.equals(descendingValue)) {
                    orderByClause += " ORDER BY " + dbColumnName + " desc";
                } else {
                    orderByClause += " ORDER BY " + dbColumnName + " asc";
                }
            }
        } else {
            orderByClause += " ORDER BY doorGroupName asc";
        }
        countNativeSql += sqlSuffix;

        nativeSql += sqlSuffix + orderByClause;

        try {
            Query countQuery = em.createNativeQuery(countNativeSql);
            Query q = em.createNativeQuery(nativeSql);
            bindParams.forEach(v -> {
                q.setParameter(v.getKey(), v.getValue());
                countQuery.setParameter(v.getKey(), v.getValue());
            });
            String offsetLimitClause = SqlUtil.setLimitAndOffsetAndReturnSqlClause(em, criteria, q);
            Object count = countQuery.getSingleResult();
            list.getDoorGroupCollection().setTotal(new Long(count.toString()));

            List<Object[]> objectList = q.getResultList();

            list.getResponse().setCode(IConstant.RESPONSE_CODE_SUCCESSFUL);
            for (Object[] oneRecord : objectList) {
                DoorGroup oneDoorGroup = new DoorGroup();
                if (oneRecord[INDEX_ID] != null) {
                    oneDoorGroup.setId(Long.valueOf(oneRecord[INDEX_ID].toString()));
                }
                if (oneRecord[INDEX_NAME] != null) {
                    oneDoorGroup.setName(String.valueOf(oneRecord[INDEX_NAME].toString()));
                }
                if (oneRecord[INDEX_DEPTH] != null) {
                    oneDoorGroup.setDepth(Integer.valueOf(oneRecord[INDEX_DEPTH].toString()));
                }
                if (oneRecord[INDEX_PARENT_DOOR_GROUP_ID] != null) {
                    oneDoorGroup.setParentGroup(new ParentIdObject());
                    oneDoorGroup.getParentGroup().setId(Long.valueOf(oneRecord[INDEX_PARENT_DOOR_GROUP_ID].toString()));
                }
                if ("Y".equals(oneRecord[INDEX_DELETED])) {
                    oneDoorGroup.setDeleted(true);
                } else {
                    oneDoorGroup.setDeleted(false);
                }
                list.getDoorGroupCollection().getRows().add(oneDoorGroup);
            }

            return list;
        } catch (Exception e) {
            logger.error("failed to process sql::" + nativeSql+ " exct:", e);
            throw e;
        }
    }

    public DoorGroupList getListOnlyPermission(DoorGroupSearchCriteria criteria) {
        return getList(criteria, false);
    }

    public DoorGroupList getList(DoorGroupSearchCriteria criteria) {
        return getList(criteria, true);
    }

    @Transactional
    public void remove(Long id) throws IllegalAccessException, IOException, InstantiationException {
        try {
            String selectAllGroupIdString = SqlUtil.buildNestedGroupSelectSqlIncludingSelf(IConstant.NESTED_GROUP_TABLE_NAME_DOOR_GROUP, id, false, false);
            Query selectQuery = em.createNativeQuery(selectAllGroupIdString);
            List<Long> groupIdList = new ArrayList<Long>();
            List<Object> objectList = selectQuery.getResultList();
            for (Object oneRecord : objectList) {
                if (oneRecord instanceof Integer) {
                    groupIdList.add(((Integer) oneRecord).longValue());
                } else if (oneRecord instanceof Long) {
                    groupIdList.add((Long) oneRecord);
                }
            }

            for (Long currentId : groupIdList) {
                Optional<com.biostar.module.doorgroup.dao.DoorGroup> currentGroup = doorGroupRepository.findById(currentId);
                currentGroup.get().setDeleted(IConstant.INDICATOR_YES);
                currentGroup.get().setDeletedDate(new Date());
                doorGroupRepository.save(currentGroup.get());
            }

            // do not remove values because if only one item is selected and removed it works like all groups
//      settingService.removeGroupIdListFromPermission(IConstant.PERMISSION_FILTER_TYPE_DOOR_GROUP, groupIdList);

            nestedSetService.updateDoorGroup();
        } catch (Exception e) {
            logger.error("Failed to remove door group with id " + id);
            throw e;
        }
    }
}
