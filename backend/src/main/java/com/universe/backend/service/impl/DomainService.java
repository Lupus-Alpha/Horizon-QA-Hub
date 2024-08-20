package com.universe.backend.service.impl;

import com.universe.backend.common.exception.DuplicateException;
import com.universe.backend.database.domain.Domain;
import com.universe.backend.database.mapper.DomainMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
public class DomainService {
    @Resource
    private DomainMapper domainMapper;

    public void saveDomain(Domain domain) {
        Domain old = domainMapper.getDomainByName(domain.getEnvironmentId(), domain.getDomainKey());
        if(old != null && !Objects.equals(old.getId(), domain.getId())){
            throw new DuplicateException("当前环境已有重复的域名标识");
        }
        if(domain.getId() == null || domain.getId().equals("")){
            //新增域名
            domain.setId(UUID.randomUUID().toString());
            domain.setCreateTime(System.currentTimeMillis());
            domain.setUpdateTime(System.currentTimeMillis());
            domain.setCreateUser(domain.getUpdateUser());
        }else{
            // 更新域名
            domain.setUpdateTime(System.currentTimeMillis());
        }
        domainMapper.saveDomain(domain);
    }

    public void deleteDomain(String id) {
        domainMapper.deleteDomain(id);
    }

    public List<Domain> getDomainList(String environmentId){
        return domainMapper.getDomainList(environmentId);
    }

}
