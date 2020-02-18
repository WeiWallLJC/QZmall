package new_qingzhu.demo.Service;


import com.github.pagehelper.PageInfo;
import new_qingzhu.demo.Pojo.TQingzhuGoodsCategory;
import new_qingzhu.demo.VO.IndexCategoryVO;
import new_qingzhu.demo.VO.SearchPageCategoryVO;

import java.util.List;
public interface CategoryService {
    /**
     * 后台分页
     *
     * @param page,limit
     * @return
     */
     PageInfo<TQingzhuGoodsCategory> getCategorisPage(int page, int limit);

    String saveCategory(TQingzhuGoodsCategory goodsCategory);

    String updateGoodsCategory(TQingzhuGoodsCategory goodsCategory);

    TQingzhuGoodsCategory getGoodsCategoryById(Long id);

    Boolean deleteBatch(Integer[] ids);

    /**
     * 返回分类数据(首页调用)
     *
     * @return
     */
    List<IndexCategoryVO> getCategoriesForIndex();

    /**
     * 返回分类数据(搜索页调用)
     *
     * @param categoryId
     * @return
     */
    SearchPageCategoryVO getCategoriesForSearch(Long categoryId);

    /**
     * 根据parentId和level获取分类列表
     *
     * @param parentIds
     * @param categoryLevel
     * @return
     */
    List<TQingzhuGoodsCategory> selectByLevelAndParentIdsAndNumber(List<Long> parentIds, int categoryLevel);
}
