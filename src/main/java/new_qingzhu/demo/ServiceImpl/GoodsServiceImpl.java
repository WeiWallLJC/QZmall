package new_qingzhu.demo.ServiceImpl;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import new_qingzhu.demo.Common.ServiceResultEnum;
import new_qingzhu.demo.Mapper.TQingzhuGoodsInfoMapper;
import new_qingzhu.demo.Pojo.TQingzhuGoodsInfo;
import new_qingzhu.demo.Pojo.TQingzhuGoodsInfoExample;
import new_qingzhu.demo.Service.GoodsService;
import new_qingzhu.demo.Util.BeanUtil;
import new_qingzhu.demo.VO.SearchGoodsVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;

@Service
public class GoodsServiceImpl implements GoodsService {
    private TQingzhuGoodsInfoMapper goodsMapper;
    @Autowired
    public GoodsServiceImpl(TQingzhuGoodsInfoMapper goodsMapper){
        this.goodsMapper=goodsMapper;
    }

    @Override
    public PageInfo getGoodsPage(int page,int limit) {
        TQingzhuGoodsInfoExample example =new TQingzhuGoodsInfoExample();
        example.createCriteria().andGoodsIdIsNotNull();
        PageHelper.startPage(page,limit);
        List<TQingzhuGoodsInfo> goodsList = goodsMapper.selectByExample(example);
        return new PageInfo<>(goodsList);
    }

    @Override
    public String saveGoods(TQingzhuGoodsInfo goods) {
        if (goodsMapper.insertSelective(goods) > 0) {
            return ServiceResultEnum.SUCCESS.getResult();
        }
        return ServiceResultEnum.DB_ERROR.getResult();
    }

    @Override
    public void batchSaveGoods(List<TQingzhuGoodsInfo> GoodsList) {
        if (!CollectionUtils.isEmpty(GoodsList)) {
            for (TQingzhuGoodsInfo tQingzhuGoodsInfo : GoodsList) {
                goodsMapper.insertSelective(tQingzhuGoodsInfo);
            }

        }
    }

    @Override
    public String updateGoods(TQingzhuGoodsInfo goods) {
        TQingzhuGoodsInfo temp = goodsMapper.selectByPrimaryKey(goods.getGoodsId());
        if (temp == null) {
            return ServiceResultEnum.DATA_NOT_EXIST.getResult();
        }
        goods.setUpdateTime(new Date());
        if (goodsMapper.updateByPrimaryKeySelective(goods) > 0) {
            return ServiceResultEnum.SUCCESS.getResult();
        }
        return ServiceResultEnum.DB_ERROR.getResult();
    }

    @Override
    public TQingzhuGoodsInfo getGoodsById(Long id) {
        return goodsMapper.selectByPrimaryKey(id);
    }
    
    @Override
    public Boolean batchUpdateSellStatus(Long[] ids, int sellStatus) {

        TQingzhuGoodsInfo goodsInfo=new TQingzhuGoodsInfo();
        goodsInfo.setGoodsSellStatus((byte) sellStatus);
        goodsInfo.setUpdateTime(new Date());
        TQingzhuGoodsInfoExample example=new TQingzhuGoodsInfoExample();
        example.createCriteria().andGoodsIdIn(Arrays.asList(ids));
        return goodsMapper.updateByExampleSelective(goodsInfo,example) > 0;
    }

    @Override
    public PageInfo searchGoods(Map<String, Object> params,int page, int limit) {

        for (String mp : params.keySet()) {
            if(params.get(mp)!=null)
            System.out.println("key = " + mp + ", value = " + params.get(mp).toString());
        }
        TQingzhuGoodsInfoExample example=new TQingzhuGoodsInfoExample();
        TQingzhuGoodsInfoExample.Criteria criteria=example.createCriteria();
        TQingzhuGoodsInfoExample.Criteria criteria2=example.createCriteria();
        criteria.andGoodsIdIsNotNull();
        if (params.containsKey("keyword") && params.get("keyword")!=null&&params.get("keyword").toString().length()!=0){
            criteria.andGoodsNameLike("%"+params.get("keyword").toString()+"%");
            criteria2.andGoodsIntroLike("%"+params.get("keyword").toString()+"%");
        }
        if (params.containsKey("goodsCategoryId") && params.get("goodsCategoryId")!=null&&params.get("goodsCategoryId").toString().length()!=0){
            criteria.andGoodsCategoryIdEqualTo(Long.valueOf(params.get("goodsCategoryId").toString()) );
        }
        if (params.containsKey("orderBy") && params.get("orderBy")!=null&&params.get("orderBy").toString().length()!=0){
            if(params.get("orderBy").equals("new")){
                example.setOrderByClause("goods_id DESC");
            }
            else if(params.get("orderBy").equals("price")){
                example.setOrderByClause("selling_price DESC");
            }
        }
        else {
            example.setOrderByClause("stock_num DESC");
        }
        example.or(criteria2);
        PageHelper.startPage(page, limit);
        List<TQingzhuGoodsInfo> goodsList = goodsMapper.selectByExample(example);
        List<SearchGoodsVO> SearchGoodsVOS = new ArrayList<>();
        if (!CollectionUtils.isEmpty(goodsList)) {
            SearchGoodsVOS = BeanUtil.copyList(goodsList, SearchGoodsVO.class);
            for (SearchGoodsVO SearchGoodsVO : SearchGoodsVOS) {
                String goodsName = SearchGoodsVO.getGoodsName();
                String goodsIntro = SearchGoodsVO.getGoodsIntro();
                // 字符串过长导致文字超出的问题
                if (goodsName.length() > 28) {
                    goodsName = goodsName.substring(0, 28) + "...";
                    SearchGoodsVO.setGoodsName(goodsName);
                }
                if (goodsIntro.length() > 30) {
                    goodsIntro = goodsIntro.substring(0, 30) + "...";
                    SearchGoodsVO.setGoodsIntro(goodsIntro);
                }
            }
        }
        PageInfo<SearchGoodsVO> pageInfo=new PageInfo<>(SearchGoodsVOS);
        pageInfo.setPageNum(page);
        pageInfo.setPageSize(limit);
        return pageInfo;
    }
}
