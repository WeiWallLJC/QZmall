package new_qingzhu.demo.Service;


import new_qingzhu.demo.Pojo.TQingzhuShoppingCartItem;
import new_qingzhu.demo.VO.ShoppingCartItemVO;

import java.util.List;

public interface ShoppingCartService {

    /**
     * 保存商品至购物车中
     *
     * @param ShoppingCartItem
     * @return
     */
    String saveCartItem(TQingzhuShoppingCartItem ShoppingCartItem);

    /**
     * 修改购物车中的属性
     *
     * @param newBeeMallShoppingCartItem
     * @return
     */
    String updateCartItem(TQingzhuShoppingCartItem newBeeMallShoppingCartItem);

    /**
     * 获取购物项详情
     *
     * @param ShoppingCartItemId
     * @return
     */
    TQingzhuShoppingCartItem getCartItemById(Long ShoppingCartItemId);

    /**
     * 删除购物车中的商品
     *
     * @param ShoppingCartItemId
     * @return
     */
    Boolean deleteById(Long ShoppingCartItemId);

    /**
     * 获取我的购物车中的列表数据
     *
     * @param UserId
     * @return
     */
    List<ShoppingCartItemVO> getMyShoppingCartItems(Long UserId);
}
