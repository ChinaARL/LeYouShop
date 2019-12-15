package com.itheima.item.service;

import com.itheima.common.exception.LyException;
import com.itheima.common.exception.enms.ResponseCode;
import com.itheima.common.utils.BeanHelper;
import com.itheima.item.dto.SpecParamDTO;
import com.itheima.item.mapper.SpecParamMapper;
import com.itheima.item.pojo.SpecParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Service
public class SpecParamService {
    @Autowired
    private SpecParamMapper specParamMapper;

    /**
     * sql: select * from tb_spec_param where cid = ? and group_id = ?  将来条件一般一个 要求：两个参数必须有一个有值
     * @param groupId
     * @param searching
     * @return
     */
    public List<SpecParamDTO> querySpecParamByGroupId(Long groupId, Long categoryId, Boolean searching) {
        if(groupId == null && categoryId ==null){
            throw new LyException(ResponseCode.ERROR);
        }
        SpecParam specParam = new SpecParam();
        specParam.setCid(categoryId);
        specParam.setGroupId(groupId);
        specParam.setSearching(searching);
        List<SpecParam> specParamList = specParamMapper.select(specParam);
        if(CollectionUtils.isEmpty(specParamList)){
            throw new LyException(ResponseCode.SPEC_NOT_FOUND);
        }
        return BeanHelper.copyWithCollection(specParamList, SpecParamDTO.class);
    }
}
