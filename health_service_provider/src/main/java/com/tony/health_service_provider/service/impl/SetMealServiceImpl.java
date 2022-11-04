package com.tony.health_service_provider.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tony.health_common.constant.RedisConstant;
import com.tony.health_common.entity.PageResult;
import com.tony.health_common.entity.QueryPageBean;
import com.tony.health_common.pojo.CheckGroup;
import com.tony.health_common.pojo.CheckItem;
import com.tony.health_common.pojo.Setmeal;
import com.tony.health_interface.service.SetMealService;
import com.tony.health_service_provider.domin.SetMealRelationParam;
import com.tony.health_service_provider.mapper.CheckGroupMapper;
import com.tony.health_service_provider.mapper.CheckItemMapper;
import com.tony.health_service_provider.mapper.SetMealMapper;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@DubboService(interfaceClass = SetMealService.class)
@Transactional
public class SetMealServiceImpl implements SetMealService {
    @Resource
    private SetMealMapper setMealMapper;
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Resource
    private CheckGroupMapper checkGroupMapper;
    @Resource
    private CheckItemMapper checkItemMapper;
    @Resource
    private FreeMarkerConfigurer configurer;
    @Value("${spring.freemarker.out-put-path}")
    private String outPutPath;

    /**
     * 新增
     */
    @Override
    public void add(Setmeal setmeal, Integer[] checkGroupIds) {
        //新增套餐数据
        setMealMapper.insert(setmeal);
        //插入套餐与检查组关系
        if (ObjectUtil.isNotEmpty(checkGroupIds)) {
            for (Integer checkGroupId : checkGroupIds) {
                setMealMapper.setSetMealAndCheckGroup(new SetMealRelationParam(setmeal.getId(), checkGroupId));
            }
        }
        //每次上传都将fileName放入redis的 set setmealPicDbResource 中
        stringRedisTemplate.opsForSet().add(RedisConstant.SETMEAL_PIC_DB_RESOURCE, setmeal.getImg());

        //当添加套餐后生成静态页面(套餐列表、套餐详情)
        generateMobileStaticHtml();
    }

    /**
     * 分页查询
     */
    @Override
    public PageResult findPage(QueryPageBean queryPageBean) {
        LambdaQueryWrapper<Setmeal> wrapper = new LambdaQueryWrapper<>();
        if (ObjectUtil.isNotEmpty(queryPageBean.getQueryString())) {
            wrapper.like(Setmeal::getCode, queryPageBean.getQueryString());
            wrapper.or().like(Setmeal::getName, queryPageBean.getQueryString());
            wrapper.or().like(Setmeal::getHelpCode, queryPageBean.getQueryString());
        }
        Page<Setmeal> page = setMealMapper.selectPage(
                new Page<>(queryPageBean.getCurrentPage(), queryPageBean.getPageSize()), wrapper);
        return new PageResult(page.getTotal(), page.getRecords());
    }


    /**
     * 编辑
     */
    @Override
    public void edit(Setmeal setmeal, Integer[] checkGroupIds) {
        setMealMapper.updateById(setmeal);
        setMealMapper.deleteCheckGroupBySetMealId(setmeal.getId());
        if (ObjectUtil.isNotEmpty(checkGroupIds)) {
            for (Integer checkGroupId : checkGroupIds) {
                setMealMapper.setSetMealAndCheckGroup(new SetMealRelationParam(setmeal.getId(), checkGroupId));
            }
        }
    }


    /**
     * 删除
     */
    @Override
    public void deleteById(Integer id) {
        setMealMapper.deleteCheckGroupBySetMealId(id);
        setMealMapper.deleteById(id);
    }

    /**
     * 获取所有套餐
     */
    @Override
    public List<Setmeal> getAllSetmeal() {
        return setMealMapper.selectList(null);
    }

    /**
     * 获取指定Id套餐详情
     */
    @Override
    public Setmeal findById(Integer id) {
        Setmeal setmeal = setMealMapper.selectById(id);
        if (ObjectUtil.isNotEmpty(setmeal)) {
            //查询检查组
            List<CheckGroup> checkGroups = setMealMapper.selectCheckGroupBySetMealId(id);

            if (ObjectUtil.isNotEmpty(checkGroups)) {
                //查询检查组对应的各检查项
                checkGroups.forEach(p -> {
                    List<Integer> checkItemIds = checkGroupMapper.findCheckItemIdByCheckGroupId(p.getId());
                    List<CheckItem> checkItems = checkItemMapper.selectList(
                            new LambdaQueryWrapper<CheckItem>().in(CheckItem::getId, checkItemIds));
                    if (ObjectUtil.isNotEmpty(checkItems)) {
                        p.setCheckItems(checkItems);
                    }
                });
                setmeal.setCheckGroups(checkGroups);
            }
        }
        return setmeal;
    }

    /**
     * 通用方法：生成静态页面
     */
    public void generateHtml(String templateName, String htmlPageName, Map map) {
        Configuration configuration = configurer.getConfiguration();
        Writer out = null;
        try {
            Template template = configuration.getTemplate(templateName);
            out = new FileWriter(new File(outPutPath + "/" + htmlPageName));
            template.process(map, out);
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 生成套餐列表页面
     */
    public void generateMobileSetmealListHtml(List<Setmeal> list) {
        Map map = new HashMap();
        map.put("setmealList", list);

        generateHtml("mobile_setmeal.ftl", "m_setmeal.html", map);
    }

    /**
     * 生成套餐详情页面
     */
    public void generateMobileSetmealDetailHtml(List<Setmeal> list) {
        for (Setmeal setmeal : list) {
            Map map = new HashMap();
            map.put("setmeal", findById(setmeal.getId()));
            generateHtml("mobile_setmeal_detail.ftl", "setmeal_detail_" + setmeal.getId() + ".html", map);
        }
    }

    /**
     * 生成当前方法所需的页面
     */
    public void generateMobileStaticHtml() {
        List<Setmeal> list = getAllSetmeal();

        generateMobileSetmealListHtml(list);

        generateMobileSetmealDetailHtml(list);
    }

    /**
     * 获取套餐数量
     */
    @Override
    public List<Map<String, Object>> findSetmealCount() {
        return setMealMapper.findSetmealCount();
    }
}
