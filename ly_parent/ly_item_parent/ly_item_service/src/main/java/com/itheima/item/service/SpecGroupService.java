package com.itheima.item.service;

import com.itheima.common.exception.LyException;
import com.itheima.common.exception.enms.ResponseCode;
import com.itheima.common.utils.BeanHelper;
import com.itheima.item.dto.SpecGroupDTO;
import com.itheima.item.mapper.SpecGroupMapper;
import com.itheima.item.pojo.SpecGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Service
public class SpecGroupService {

    @Autowired
    private SpecGroupMapper specGroupMapper;


    /**
     * sql: select * from tb_spec_group where cid = ?
     * @param categoryId
     * @return
     */
    public List<SpecGroupDTO> queryByCategoryId(Long categoryId) {
        SpecGroup specGroup = new SpecGroup();
        specGroup.setCid(categoryId);
        List<SpecGroup> specGroupList = specGroupMapper.select(specGroup);
        if (CollectionUtils.isEmpty(specGroupList)) {
            throw new LyException(ResponseCode.SPEC_NOT_FOUND);
        }
        return BeanHelper.copyWithCollection(specGroupList, SpecGroupDTO.class);
    }
}
