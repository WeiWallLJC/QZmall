package new_qingzhu.demo.ServiceImpl;


import com.github.pagehelper.PageHelper;
import new_qingzhu.demo.Common.Constants;
import new_qingzhu.demo.Common.ServiceResultEnum;
import new_qingzhu.demo.Mapper.TQingzhuGoodsInfoMapper;
import new_qingzhu.demo.Mapper.TQingzhuShoppingCartItemMapper;
import new_qingzhu.demo.Pojo.TQingzhuGoodsInfo;
import new_qingzhu.demo.Pojo.TQingzhuGoodsInfoExample;
import new_qingzhu.demo.Pojo.TQingzhuShoppingCartItem;
import new_qingzhu.demo.Pojo.TQingzhuShoppingCartItemExample;
import new_qingzhu.demo.Service.ShoppingCartService;
import new_qingzhu.demo.Util.BeanUtil;
import new_qingzhu.demo.VO.ShoppingCartItemVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {
    
    private TQingzhuShoppingCartItemMapper ShoppingCartItemMapper;
    private TQingzhuGoodsInfoMapper GoodsMapper;
    @Autowired
    public ShoppingCartServiceImpl(TQingzhuGoodsInfoMapper goodsMapper,TQingzhuShoppingCartItemMapper shoppingCartItemMapper){
        this.GoodsMapper=goodsMapper;
        this.ShoppingCartItemMapper=shoppingCartItemMapper;
    }

    //todo 修改session中购物项数量

    @Override
    public String saveCartItem(TQingzhuShoppingCartItem ShoppingCartItem) {
        TQingzhuShoppingCartItemExample example=new TQingzhuShoppingCartItemExample();
        example.createCriteria().andUserIdEqualTo(ShoppingCartItem.getUserId()).andGoodsIdEqualTo(ShoppingCartItem.getGoodsId());
        TQingzhuShoppingCartItem temp=null;
        if(ShoppingCartItemMapper.selectByExample(example).size()!=0){
            temp = ShoppingCartItemMapper.selectByExample(example).get(0);
        }
        if (temp != null) {
            //已存在则修改该记录
            //todo count = tempCount + 1
            temp.setGoodsCount(ShoppingCartItem.getGoodsCount());
            return updateCartItem(temp);
        }
        TQingzhuGoodsInfo Goods = GoodsMapper.selectByPrimaryKey(ShoppingCartItem.getGoodsId());
        //商品为空
        if (Goods == null) {
            return ServiceResultEnum.GOODS_NOT_EXIST.getResult();
        }
        TQingzhuShoppingCartItemExample example1= new TQingzhuShoppingCartItemExample();
        example1.createCriteria().andUserIdEqualTo(ShoppingCartItem.getUserId());
        int totalItem=0;
        if(ShoppingCartItemMapper.selectByExample(example1).size()!=0){
             totalItem= ShoppingCartItemMapper.selectByExample(example1).get(0).getGoodsCount();
        }

        //超出最大数量
        if (totalItem > Constants.SHOPPING_CART_ITEM_LIMIT_NUMBER) {
            return ServiceResultEnum.SHOPPING_CART_ITEM_LIMIT_NUMBER_ERROR.getResult();
        }
        //保存记录
        if (ShoppingCartItemMapper.insertSelective(ShoppingCartItem) > 0) {
            return ServiceResultEnum.SUCCESS.getResult();
        }
        return ServiceResultEnum.DB_ERROR.getResult();
    }

    @Override
    public String updateCartItem(TQingzhuShoppingCartItem ShoppingCartItem) {
        TQingzhuShoppingCartItem ShoppingCartItemUpdate = ShoppingCartItemMapper.selectByPrimaryKey(ShoppingCartItem.getCartItemId());
        if (ShoppingCartItemUpdate == null) {
            return ServiceResultEnum.DATA_NOT_EXIST.getResult();
        }
        //超出最大数量
        if (ShoppingCartItem.getGoodsCount() > Constants.SHOPPING_CART_ITEM_LIMIT_NUMBER) {
            return ServiceResultEnum.SHOPPING_CART_ITEM_LIMIT_NUMBER_ERROR.getResult();
        }
        //todo 数量相同不会进行修改
        //todo userId不同不能修改
        ShoppingCartItemUpdate.setGoodsCount(ShoppingCartItem.getGoodsCount());
        ShoppingCartItemUpdate.setUpdateTime(new Date());
        //保存记录
        if (ShoppingCartItemMapper.updateByPrimaryKeySelective(ShoppingCartItemUpdate) > 0) {
            return ServiceResultEnum.SUCCESS.getResult();
        }
        return ServiceResultEnum.DB_ERROR.getResult();
    }

    @Override
    public TQingzhuShoppingCartItem getCartItemById(Long ShoppingCartItemId) {
        return ShoppingCartItemMapper.selectByPrimaryKey(ShoppingCartItemId);
    }

    @Override
    public Boolean deleteById(Long ShoppingCartItemId) {
        //todo userId不同不能删除
        return ShoppingCartItemMapper.deleteByPrimaryKey(ShoppingCartItemId) > 0;
    }

    @Override
    public List<ShoppingCartItemVO> getMyShoppingCartItems(Long UserId) {
        List<ShoppingCartItemVO> ShoppingCartItemVOS = new ArrayList<>();
        TQingzhuShoppingCartItemExample example =new TQingzhuShoppingCartItemExample();
        example.createCriteria().andUserIdEqualTo(UserId);
        PageHelper.startPage(0,Constants.SHOPPING_CART_ITEM_TOTAL_NUMBER);
        List<TQingzhuShoppingCartItem> ShoppingCartItems = ShoppingCartItemMapper.selectByExample(example);
        if (!CollectionUtils.isEmpty(ShoppingCartItems)) {
            //查询商品信息并做数据转换
            List<Long> Shop = ShoppingCartItems.stream().map(TQingzhuShoppingCartItem::getGoodsId).collect(Collectors.toList());
            TQingzhuGoodsInfoExample example1=new TQingzhuGoodsInfoExample();
            example1.createCriteria().andGoodsIdIn(Shop);
            List<TQingzhuGoodsInfo> Goods = GoodsMapper.selectByExample(example1);
            Map<Long, TQingzhuGoodsInfo> GoodsMap = new HashMap<>();
            if (!CollectionUtils.isEmpty(Goods)) {
                GoodsMap = Goods.stream().collect(Collectors.toMap(TQingzhuGoodsInfo::getGoodsId, Function.identity(), (entity1, entity2) -> entity1));
            }
            for (TQingzhuShoppingCartItem ShoppingCartItem : ShoppingCartItems) {
                ShoppingCartItemVO ShoppingCartItemVO = new ShoppingCartItemVO();
                BeanUtil.copyProperties(ShoppingCartItem, ShoppingCartItemVO);
                if (GoodsMap.containsKey(ShoppingCartItem.getGoodsId())) {
                    TQingzhuGoodsInfo GoodsTemp = GoodsMap.get(ShoppingCartItem.getGoodsId());
                    ShoppingCartItemVO.setGoodsCoverImg(GoodsTemp.getGoodsCoverImg());
                    String goodsName = GoodsTemp.getGoodsName();
                    // 字符串过长导致文字超出的问题
                    if (goodsName.length() > 28) {
                        goodsName = goodsName.substring(0, 28) + "...";
                    }
                    ShoppingCartItemVO.setGoodsName(goodsName);
                    ShoppingCartItemVO.setSellingPrice(GoodsTemp.getSellingPrice());
                    ShoppingCartItemVOS.add(ShoppingCartItemVO);
                }
            }
        }
        return ShoppingCartItemVOS;
    }
}
