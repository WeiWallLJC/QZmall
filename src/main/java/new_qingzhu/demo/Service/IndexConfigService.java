package new_qingzhu.demo.Service;

import com.github.pagehelper.PageInfo;
import new_qingzhu.demo.Pojo.TQingzhuIndexConfig;
import new_qingzhu.demo.VO.IndexConfigGoodsVO;

import java.util.List;

public interface IndexConfigService {
    /**
     * 后台分页
     *
     * @param limit,page
     * @return
     */
    PageInfo getConfigsPage(int page,int limit);

    String saveIndexConfig(TQingzhuIndexConfig indexConfig);

    String updateIndexConfig(TQingzhuIndexConfig indexConfig);

    TQingzhuIndexConfig getIndexConfigById(Long id);

    /**
     * 返回固定数量的首页配置商品对象(首页调用)
     *
     * @param number
     * @return
     */
    List<IndexConfigGoodsVO> getConfigGoodsesForIndex(int configType, int number);

    Boolean deleteBatch(Long[] ids);
}
