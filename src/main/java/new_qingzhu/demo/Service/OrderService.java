package new_qingzhu.demo.Service;

import com.github.pagehelper.PageInfo;
import new_qingzhu.demo.Pojo.TQingzhuOrder;
import new_qingzhu.demo.VO.*;

import java.util.List;

public interface OrderService {
    /**
     * 后台分页
     *
     * @param page,limit
     * @return
     */
    PageInfo getOrdersPage(int page,int limit);

    /**
     * 订单信息修改
     *
     * @param Order
     * @return
     */
    String updateOrderInfo( TQingzhuOrder Order);

    /**
     * 配货
     *
     * @param ids
     * @return
     */
    String checkDone(Long[] ids);

    /**
     * 出库
     *
     * @param ids
     * @return
     */
    String checkOut(Long[] ids);

    /**
     * 关闭订单
     *
     * @param ids
     * @return
     */
    String closeOrder(Long[] ids);

    /**
     * 保存订单
     *
     * @param user
     * @param myShoppingCartItems
     * @return
     */
    String saveOrder(UserVO user, List<ShoppingCartItemVO> myShoppingCartItems);

    /**
     * 获取订单详情
     *
     * @param orderNo
     * @param userId
     * @return
     */
    OrderDetailVO getOrderDetailByOrderNo(String orderNo, Long userId);

    /**
     * 获取订单详情
     *
     * @param orderNo
     * @return
     */
    TQingzhuOrder getOrderByOrderNo(String orderNo);

    /**
     * 我的订单列表
     *
     * @param page,limit
     * @return
     */
    PageInfo getMyOrders(Long userid,int page, int limit);

    /**
     * 手动取消订单
     *
     * @param orderNo
     * @param userId
     * @return
     */
    String cancelOrder(String orderNo, Long userId);

    /**
     * 确认收货
     *
     * @param orderNo
     * @param userId
     * @return
     */
    String finishOrder(String orderNo, Long userId);

    String paySuccess(String orderNo, int payType);

    List<OrderItemVO> getOrderItems(Long id);

    List<OrderListVO> getAllOrderList();
}
