package com.macro.mall.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.macro.mall.common.utils.UuidUtil;
import com.macro.mall.mapper.SysDictMapper;
import com.macro.mall.model.SysDict;
import com.macro.mall.model.SysDictExample;
import com.macro.mall.service.SysDictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author Louis
 * @description
 * @create 2020-07-09 10:50
 */
@Service
public class SysDictServiceImpl implements SysDictService {

    @Autowired
    private SysDictMapper sysDictDao;

    @Override
    public boolean insert(SysDict record) {
        record.setDictId(UuidUtil.getUuid());
        record.setCreateDate(new Date());
        return sysDictDao.insert(record);
    }

    @Override
    public SysDict queryDict(String groupCode, String dictKey) {
        SysDictExample sysDictExample = new SysDictExample();
        SysDictExample.Criteria criteria = sysDictExample.createCriteria();
        criteria.andGroupCodeEqualTo(groupCode);
        criteria.andDictKeyEqualTo(dictKey);
        List<SysDict> sysDicts = sysDictDao.selectByExample(sysDictExample);
        if (CollectionUtil.isNotEmpty(sysDicts)) {
            return sysDicts.get(0);
        }
        return new SysDict();
    }
}
