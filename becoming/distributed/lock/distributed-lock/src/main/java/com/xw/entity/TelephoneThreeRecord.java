package com.xw.entity;

//import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @author liuxiaowei
 * @description 手机三要素校验记录表
 * @date 2023/7/13
 */
@Data
//@TableName("b_telephone_three_record")
public class TelephoneThreeRecord implements Serializable {
    private Long id;
    private String name;
    private String mobile;
    private String idNo;
    private String createTime;
}
