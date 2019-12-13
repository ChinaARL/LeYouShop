package com.itheima.item.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.itheima.common.exception.LyException;
import com.itheima.common.exception.enms.ResponseCode;
import com.itheima.common.utils.BeanHelper;
import com.itheima.common.vo.PageResult;
import com.itheima.item.dto.BrandDTO;
import com.itheima.item.mapper.BrandMapper;
import com.itheima.item.pojo.Brand;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @author 高策
 * @version V1.0
 * @Package com.itheima.item.service
 * @date 2019/12/7 20:37
 * @Copyright © 2018-2019 黑马程序员（顺义）校区
 */
@Service
public class BrandService {

    @Autowired
    private BrandMapper brandMapper;


    //sql: select * from tb_brand where name like %米% limit 0, 5 order by name acs
    public PageResult<BrandDTO> queryByPage(String key, Integer page, Integer size, String sortBy, Boolean desc) {
        //使用PageHelper分页
        PageHelper.startPage(page, size);
        //selectByExample动态设置查询条件
        Example example = new Example(Brand.class);
        //设置where部分
        Example.Criteria criteria = example.createCriteria();
        if (StringUtils.isNotBlank(key)) {
            criteria.andLike("name", "%" + key + "%");
        }
        if (StringUtils.isNotBlank(sortBy)) {
            //设置oder by nameDESC/ASC setOrderByClause:设置执行sql语句
            example.setOrderByClause(sortBy + (desc ? " desc" : " asc"));
        }
        List<Brand> brandsList = brandMapper.selectByExample(example);

        //分页查询
        PageInfo<Brand> pageInfo = new PageInfo<>(brandsList);

        //将查询到POJO对象 转为 DTO 对象
        List<BrandDTO> brandDTOList = BeanHelper.copyWithCollection(pageInfo.getList(), BrandDTO.class);

        return new PageResult<>(pageInfo.getTotal(), pageInfo.getPages(), brandDTOList);

    }

    /**
     * 1、向tb_brand表中新增品牌数据  2、想tb_category_brand（分类品牌中间表）新增若干条记录
     *
     * @param brandDTO
     * @param categoryIds
     * @return
     */

    @Transactional
    public void saveBrand(BrandDTO brandDTO, List<Long> categoryIds) {
        Brand brand = BeanHelper.copyProperties(brandDTO, Brand.class);
        //insertSelectiv 如果提交参数为null,会使用数据库默认值  创建时间 更新时间
//        1、向tb_brand表中新增品牌数据
        int count = brandMapper.insertSelective(brand);
        if (count != 1) {
            throw new LyException(ResponseCode.INSERT_OPERATION_FAIL);
        }
        //2、想tb_category_brand（分类品牌中间表）新增若干条记录
        if (!CollectionUtils.isEmpty(categoryIds)) {
            count = brandMapper.insertCategoryBrands(brand.getId(), categoryIds);
            if (count != categoryIds.size()) {
                throw new LyException(ResponseCode.INSERT_OPERATION_FAIL);
            }
        }
    }

    public BrandDTO queryByBrandId(Long brandId) {
        Brand brand = brandMapper.selectByPrimaryKey(brandId);
        if (brand == null) {
            throw new LyException(ResponseCode.BRAND_NOT_FOUND);
        }
        return BeanHelper.copyProperties(brand, BrandDTO.class);
    }

    public List<BrandDTO> queryByCategoryId(Long categoryId) {
        List<Brand> brandList = brandMapper.queryByCategoryId(categoryId);
        if (CollectionUtils.isEmpty(brandList)) {
            throw new LyException(ResponseCode.BRAND_NOT_FOUND);
        }
        return BeanHelper.copyWithCollection(brandList, BrandDTO.class);
    }
}
