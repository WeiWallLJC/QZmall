package new_qingzhu.demo.Service;


import com.github.pagehelper.PageInfo;
import new_qingzhu.demo.Pojo.TQingzhuGoodsInfo;

import java.util.List;
import java.util.Map;

public interface GoodsService {
    /**
     * 后台分页
     *
     * @param page,limit
     * @return
     */
    PageInfo getGoodsPage(int page,int limit);

    /**
     * 添加商品
     *
     * @param goods
     * @return
     */
    String saveGoods(TQingzhuGoodsInfo goods);

    /**
     * 批量新增商品数据
     *
     * @param newBeeMallGoodsList
     * @return
     */
    void batchSaveGoods(List<TQingzhuGoodsInfo> newBeeMallGoodsList);

    /**
     * 修改商品信息
     *
     * @param goods
     * @return
     */
    String updateGoods(TQingzhuGoodsInfo goods);

    /**
     * 获取商品详情
     *
     * @param id
     * @return
     */
    TQingzhuGoodsInfo getGoodsById(Long id);

    /**
     * 批量修改销售状态(上架下架)
     *
     * @param ids
     * @return
     */
    Boolean batchUpdateSellStatus(Long[] ids, int sellStatus);

    /**
     * 商品搜索
     *
     * @param params,page,limit
     * @return
     */
    PageInfo searchGoods(Map<String, Object> params,int page, int limit);
}
