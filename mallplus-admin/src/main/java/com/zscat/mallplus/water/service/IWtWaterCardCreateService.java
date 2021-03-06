package com.zscat.mallplus.water.service;

import com.zscat.mallplus.utils.ValidatorUtils;
import com.zscat.mallplus.water.entity.WtWaterCardCreate;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zscat.mallplus.water.entity.WtWaterCardExcel;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author lyn
 * @date 2020-05-29
 */

public interface IWtWaterCardCreateService extends IService<WtWaterCardCreate> {

    boolean saveAll(WtWaterCardCreate entity, String key);
    //卡号是否重复
    boolean checkNum(Long str, Long end);
    //制卡信息下载
    List<WtWaterCardExcel> getExport(String delFlag,Long id);
    //关联公众号key
    String getAcidKey(Integer acid);
    //经销商和制卡的公众号是否一致
    boolean checkDealerId(WtWaterCardCreate entity);
}
