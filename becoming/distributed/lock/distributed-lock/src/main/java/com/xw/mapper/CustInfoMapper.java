package com.xw.mapper;

import com.xw.entity.TelCheckRequest;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CustInfoMapper {
    void addTelephoneThreeRecord(TelCheckRequest request);
}
