package com.universe.backend.database.mapper;

import com.universe.backend.database.domain.Domain;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DomainMapper {
    void saveDomain(Domain domain);

    void deleteDomain(String id);

    Domain getDomainByName(@Param("environmentId")String environmentId,@Param("domainKey")String  domainKey);

    List<Domain> getDomainList(String environmentId);
}
