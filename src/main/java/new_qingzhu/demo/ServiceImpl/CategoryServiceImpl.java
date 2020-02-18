package new_qingzhu.demo.ServiceImpl;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import new_qingzhu.demo.Common.CategoryLevelEnum;
import new_qingzhu.demo.Common.Constants;
import new_qingzhu.demo.Common.ServiceResultEnum;
import new_qingzhu.demo.Mapper.TQingzhuGoodsCategoryMapper;
import new_qingzhu.demo.Pojo.TQingzhuGoodsCategory;
import new_qingzhu.demo.Pojo.TQingzhuGoodsCategoryExample;
import new_qingzhu.demo.Service.CategoryService;
import new_qingzhu.demo.Util.BeanUtil;
import new_qingzhu.demo.VO.IndexCategoryVO;
import new_qingzhu.demo.VO.SearchPageCategoryVO;
import new_qingzhu.demo.VO.SecondLevelCategoryVO;
import new_qingzhu.demo.VO.ThirdLevelCategoryVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

@Service
public class CategoryServiceImpl implements CategoryService {
    private TQingzhuGoodsCategoryMapper goodsCategoryMapper;
    @Autowired
    public CategoryServiceImpl (TQingzhuGoodsCategoryMapper goodsCategoryMapper){
        this.goodsCategoryMapper=goodsCategoryMapper;
    }
    @Override
    public PageInfo<TQingzhuGoodsCategory> getCategorisPage(int page, int limit) {

        PageHelper.startPage(page, limit);
        TQingzhuGoodsCategoryExample example=new TQingzhuGoodsCategoryExample();
        example.createCriteria().andCategoryIdIsNotNull();
        List<TQingzhuGoodsCategory> goodsCategories = goodsCategoryMapper.selectByExample(example);
        return new PageInfo<>(goodsCategories);
    }

    @Override
    public String saveCategory(TQingzhuGoodsCategory goodsCategory) {
        TQingzhuGoodsCategoryExample example=new TQingzhuGoodsCategoryExample();
        example.createCriteria().andCategoryLevelEqualTo(goodsCategory.getCategoryLevel()).andCategoryNameEqualTo(goodsCategory.getCategoryName());
        List<TQingzhuGoodsCategory> temp = goodsCategoryMapper.selectByExample(example);
        if (temp.get(0) != null) {
            return ServiceResultEnum.SAME_CATEGORY_EXIST.getResult();
        }
        if (goodsCategoryMapper.insertSelective(goodsCategory) > 0) {
            return ServiceResultEnum.SUCCESS.getResult();
        }
        return ServiceResultEnum.DB_ERROR.getResult();
    }

    @Override
    public String updateGoodsCategory(TQingzhuGoodsCategory goodsCategory) {
        TQingzhuGoodsCategory temp = goodsCategoryMapper.selectByPrimaryKey(goodsCategory.getCategoryId());
        if (temp == null) {
            return ServiceResultEnum.DATA_NOT_EXIST.getResult();
        }
        TQingzhuGoodsCategoryExample example=new TQingzhuGoodsCategoryExample();
        example.createCriteria().andCategoryLevelEqualTo(goodsCategory.getCategoryLevel()).andCategoryNameEqualTo(goodsCategory.getCategoryName());

        List<TQingzhuGoodsCategory> temp2 = goodsCategoryMapper.selectByExample(example);
        if (temp2.get(0) != null && !temp2.get(0).getCategoryId().equals(goodsCategory.getCategoryId())) {
            //同名且不同id 不能继续修改
            return ServiceResultEnum.SAME_CATEGORY_EXIST.getResult();
        }
        goodsCategory.setUpdateTime(new Date());
        if (goodsCategoryMapper.updateByPrimaryKeySelective(goodsCategory) > 0) {
            return ServiceResultEnum.SUCCESS.getResult();
        }
        return ServiceResultEnum.DB_ERROR.getResult();
    }

    @Override
    public TQingzhuGoodsCategory getGoodsCategoryById(Long id) {
        return goodsCategoryMapper.selectByPrimaryKey(id);
    }

    @Override
    public Boolean deleteBatch(Integer[] ids) {
        if (ids.length < 1) {
            return false;
        }
        List ids1= Arrays.asList(ids);
        TQingzhuGoodsCategoryExample example=new TQingzhuGoodsCategoryExample();
        example.createCriteria().andCategoryIdIn(ids1);
    //删除分类数据
        return goodsCategoryMapper.deleteByExample(example)> 0;
    }

    @Override
    public List<IndexCategoryVO> getCategoriesForIndex() {
        List<IndexCategoryVO> IndexCategoryVOS = new ArrayList<>();
        //获取一级分类的固定数量的数据
        TQingzhuGoodsCategoryExample example= new TQingzhuGoodsCategoryExample();
        example.createCriteria().andParentIdIn(Collections.singletonList(0L)).andCategoryLevelEqualTo((byte) CategoryLevelEnum.LEVEL_ONE.getLevel());
        PageHelper.startPage(0,Constants.INDEX_CATEGORY_NUMBER);
        List<TQingzhuGoodsCategory> firstLevelCategories = goodsCategoryMapper.selectByExample(example);
        if (!CollectionUtils.isEmpty(firstLevelCategories)) {
            List<Long> firstLevelCategoryIds = firstLevelCategories.stream().map(TQingzhuGoodsCategory::getCategoryId).collect(Collectors.toList());
            //获取二级分类的数据
            example.clear();
            example.createCriteria().andParentIdIn(firstLevelCategoryIds).andCategoryLevelEqualTo((byte) CategoryLevelEnum.LEVEL_TWO.getLevel());
            List<TQingzhuGoodsCategory> secondLevelCategories = goodsCategoryMapper.selectByExample(example);
            if (!CollectionUtils.isEmpty(secondLevelCategories)) {
                List<Long> secondLevelCategoryIds = secondLevelCategories.stream().map(TQingzhuGoodsCategory::getCategoryId).collect(Collectors.toList());
                //获取三级分类的数据
                example.clear();
                example.createCriteria().andParentIdIn(secondLevelCategoryIds).andCategoryLevelEqualTo((byte) CategoryLevelEnum.LEVEL_THREE.getLevel());
                List<TQingzhuGoodsCategory> thirdLevelCategories = goodsCategoryMapper.selectByExample(example);
                if (!CollectionUtils.isEmpty(thirdLevelCategories)) {
                    //根据 parentId 将 thirdLevelCategories 分组
                    Map<Long, List<TQingzhuGoodsCategory>> thirdLevelCategoryMap = thirdLevelCategories.stream().collect(groupingBy(TQingzhuGoodsCategory::getParentId));
                    List<SecondLevelCategoryVO> secondLevelCategoryVOS = new ArrayList<>();
                    //处理二级分类
                    for (TQingzhuGoodsCategory secondLevelCategory : secondLevelCategories) {
                        SecondLevelCategoryVO secondLevelCategoryVO = new SecondLevelCategoryVO();
                        BeanUtil.copyProperties(secondLevelCategory, secondLevelCategoryVO);
                        //如果该二级分类下有数据则放入 secondLevelCategoryVOS 对象中
                        if (thirdLevelCategoryMap.containsKey(secondLevelCategory.getCategoryId())) {
                            //根据二级分类的id取出thirdLevelCategoryMap分组中的三级分类list
                            List<TQingzhuGoodsCategory> tempGoodsCategories = thirdLevelCategoryMap.get(secondLevelCategory.getCategoryId());
                            secondLevelCategoryVO.setThirdLevelCategoryVOS((BeanUtil.copyList(tempGoodsCategories, ThirdLevelCategoryVO.class)));
                            secondLevelCategoryVOS.add(secondLevelCategoryVO);
                        }
                    }
                    //处理一级分类
                    if (!CollectionUtils.isEmpty(secondLevelCategoryVOS)) {
                        //根据 parentId 将 thirdLevelCategories 分组
                        Map<Long, List<SecondLevelCategoryVO>> secondLevelCategoryVOMap = secondLevelCategoryVOS.stream().collect(groupingBy(SecondLevelCategoryVO::getParentId));
                        for (TQingzhuGoodsCategory firstCategory : firstLevelCategories) {
                            IndexCategoryVO newBeeMallIndexCategoryVO = new IndexCategoryVO();
                            BeanUtil.copyProperties(firstCategory, newBeeMallIndexCategoryVO);
                            //如果该一级分类下有数据则放入 IndexCategoryVOS 对象中
                            if (secondLevelCategoryVOMap.containsKey(firstCategory.getCategoryId())) {
                                //根据一级分类的id取出secondLevelCategoryVOMap分组中的二级级分类list
                                List<SecondLevelCategoryVO> tempGoodsCategories = secondLevelCategoryVOMap.get(firstCategory.getCategoryId());
                                newBeeMallIndexCategoryVO.setSecondLevelCategoryVOS(tempGoodsCategories);
                                IndexCategoryVOS.add(newBeeMallIndexCategoryVO);
                            }
                        }
                    }
                }
            }
            return IndexCategoryVOS;
        } else {
            return null;
        }
    }

    @Override
    public SearchPageCategoryVO getCategoriesForSearch(Long categoryId) {
        SearchPageCategoryVO searchPageCategoryVO = new SearchPageCategoryVO();
        TQingzhuGoodsCategory thirdLevelGoodsCategory = goodsCategoryMapper.selectByPrimaryKey(categoryId);
        if (thirdLevelGoodsCategory != null && thirdLevelGoodsCategory.getCategoryLevel() == CategoryLevelEnum.LEVEL_THREE.getLevel()) {
            //获取当前三级分类的二级分类
            TQingzhuGoodsCategory secondLevelGoodsCategory = goodsCategoryMapper.selectByPrimaryKey(thirdLevelGoodsCategory.getParentId());
            if (secondLevelGoodsCategory != null && secondLevelGoodsCategory.getCategoryLevel() == CategoryLevelEnum.LEVEL_TWO.getLevel()) {
                //获取当前二级分类下的三级分类List
                TQingzhuGoodsCategoryExample example=new TQingzhuGoodsCategoryExample();
                example.createCriteria().andParentIdIn(Collections.singletonList(secondLevelGoodsCategory.getCategoryId())).andCategoryLevelEqualTo((byte) CategoryLevelEnum.LEVEL_THREE.getLevel());
                PageHelper.startPage(0,Constants.SEARCH_CATEGORY_NUMBER);
                List<TQingzhuGoodsCategory> thirdLevelCategories = goodsCategoryMapper.selectByExample(example);
                searchPageCategoryVO.setCurrentCategoryName(thirdLevelGoodsCategory.getCategoryName());
                searchPageCategoryVO.setSecondLevelCategoryName(secondLevelGoodsCategory.getCategoryName());
                searchPageCategoryVO.setThirdLevelCategoryList(thirdLevelCategories);
                return searchPageCategoryVO;
            }
        }
        return null;
    }

    @Override
    public List<TQingzhuGoodsCategory> selectByLevelAndParentIdsAndNumber(List<Long> parentIds, int categoryLevel) {
        TQingzhuGoodsCategoryExample example=new TQingzhuGoodsCategoryExample();
        example.createCriteria().andParentIdIn(parentIds).andCategoryLevelEqualTo((byte) categoryLevel);
        return goodsCategoryMapper.selectByExample(example);
    }
}
