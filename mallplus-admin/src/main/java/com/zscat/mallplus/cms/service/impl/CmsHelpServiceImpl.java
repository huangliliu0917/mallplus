package com.zscat.mallplus.cms.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zscat.mallplus.cms.entity.CmsHelp;
import com.zscat.mallplus.cms.entity.CmsHelpCategory;
import com.zscat.mallplus.cms.mapper.CmsHelpCategoryMapper;
import com.zscat.mallplus.cms.mapper.CmsHelpMapper;
import com.zscat.mallplus.cms.service.ICmsHelpService;
import com.zscat.mallplus.water.entity.WtFilterElementType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 帮助表 服务实现类
 * </p>
 *
 * @author zscat
 * @since 2019-04-17
 */
@Service
public class CmsHelpServiceImpl extends ServiceImpl<CmsHelpMapper, CmsHelp> implements ICmsHelpService {

    @Resource
    private CmsHelpMapper helpMapper;
    @Resource
    private CmsHelpCategoryMapper helpCategoryMapper;

    @Override
    @Transactional
    public boolean saves(CmsHelp entity) {
        entity.setCreateTime(new Date());
        helpMapper.insert(entity);
        CmsHelpCategory category = helpCategoryMapper.selectById(entity.getCategoryId());
        category.setHelpCount(category.getHelpCount() + 1);
        helpCategoryMapper.updateById(category);
        return true;
    }

    @Override
    public IPage<CmsHelp> pageList(IPage<CmsHelp> page, CmsHelp entity){
        return page.setRecords(helpMapper.pageList(entity));
    }
}
