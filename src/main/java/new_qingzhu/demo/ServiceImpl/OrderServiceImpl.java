package new_qingzhu.demo.ServiceImpl;

import new_qingzhu.demo.Common.Exception;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import new_qingzhu.demo.Common.OrderStatusEnum;
import new_qingzhu.demo.Common.PayStatusEnum;
import new_qingzhu.demo.Common.PayTypeEnum;
import new_qingzhu.demo.Common.ServiceResultEnum;
import new_qingzhu.demo.Mapper.TQingzhuGoodsInfoMapper;
import new_qingzhu.demo.Mapper.TQingzhuOrderItemMapper;
import new_qingzhu.demo.Mapper.TQingzhuOrderMapper;
import new_qingzhu.demo.Mapper.TQingzhuShoppingCartItemMapper;
import new_qingzhu.demo.Pojo.*;
import new_qingzhu.demo.Service.OrderService;
import new_qingzhu.demo.Util.BeanUtil;
import new_qingzhu.demo.Util.NumberUtil;
import new_qingzhu.demo.VO.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

@Service
public class OrderServiceImpl implements OrderService {

    private TQingzhuGoodsInfoMapper GoodsMapper;
    private TQingzhuOrderMapper OrderMapper;
    private TQingzhuOrderItemMapper OrderItemMapper;
    private TQingzhuShoppingCartItemMapper ShoppingCartItemMapper;
    @Autowired
    public OrderServiceImpl(TQingzhuShoppingCartItemMapper tQingzhuShoppingCartItemMapper,TQingzhuOrderMapper tQingzhuOrderMapper,TQingzhuOrderItemMapper tQingzhuOrderItemMapper,TQingzhuGoodsInfoMapper tQingzhuGoodsInfoMapper){
        this.GoodsMapper=tQingzhuGoodsInfoMapper;
        this.OrderItemMapper=tQingzhuOrderItemMapper;
        this.ShoppingCartItemMapper=tQingzhuShoppingCartItemMapper;
        this.OrderMapper=tQingzhuOrderMapper;

    }


    @Override
    public PageInfo getOrdersPage(int page,int limit) {
        TQingzhuOrderExample example=new TQingzhuOrderExample();
        example.createCriteria().andOrderIdIsNotNull();
        PageHelper.startPage(page, limit);
        List<TQingzhuOrder> Orders = OrderMapper.selectByExample(example);
        PageInfo<TQingzhuOrder> pageInfo =new PageInfo<>(Orders);
        pageInfo.setPageSize(limit);
        pageInfo.setPageNum(page);
        return pageInfo;
    }

    @Override
    @Transactional
    public String updateOrderInfo(TQingzhuOrder Order) {
        TQingzhuOrder temp = OrderMapper.selectByPrimaryKey(Order.getOrderId());
        //不为空且orderStatus>=0且状态为出库之前可以修改部分信息
        if (temp != null && temp.getOrderStatus() >= 0 && temp.getOrderStatus() < 3) {
            temp.setTotalPrice(Order.getTotalPrice());
            temp.setUserAddress(Order.getUserAddress());
            temp.setUpdateTime(new Date());
            if (OrderMapper.updateByPrimaryKeySelective(temp) > 0) {
                return ServiceResultEnum.SUCCESS.getResult();
            }
            return ServiceResultEnum.DB_ERROR.getResult();
        }
        return ServiceResultEnum.DATA_NOT_EXIST.getResult();
    }

    @Override
    @Transactional
    public String checkDone(Long[] ids) {
        //查询所有的订单 判断状态 修改状态和更新时间
        TQingzhuOrderExample example=new TQingzhuOrderExample();
        example.createCriteria().andOrderIdIn(Arrays.asList(ids));
        List<TQingzhuOrder> orders = OrderMapper.selectByExample(example);
        StringBuilder errorOrderNos = new StringBuilder();
        if (!CollectionUtils.isEmpty(orders)) {
            for (TQingzhuOrder Order : orders) {
                if (Order.getIsDeleted() == 1) {
                    errorOrderNos.append(Order.getOrderNo()).append(" ");
                    continue;
                }
                if (Order.getOrderStatus() != 1) {
                    errorOrderNos.append(Order.getOrderNo()).append(" ");
                }
            }
            if (StringUtils.isEmpty(errorOrderNos.toString())) {
                //订单状态正常 可以执行配货完成操作 修改订单状态和更新时间
                if (OrderMapper.checkDone(Arrays.asList(ids)) > 0) {
                    return ServiceResultEnum.SUCCESS.getResult();
                } else {
                    return ServiceResultEnum.DB_ERROR.getResult();
                }
            } else {
                //订单此时不可执行出库操作
                if (errorOrderNos.length() > 0 && errorOrderNos.length() < 100) {
                    return errorOrderNos + "订单的状态不是支付成功无法执行出库操作";
                } else {
                    return "你选择了太多状态不是支付成功的订单，无法执行配货完成操作";
                }
            }
        }
        //未查询到数据 返回错误提示
        return ServiceResultEnum.DATA_NOT_EXIST.getResult();
    }

    @Override
    @Transactional
    public String checkOut(Long[] ids) {
        //查询所有的订单 判断状态 修改状态和更新时间
        TQingzhuOrderExample example=new TQingzhuOrderExample();
        example.createCriteria().andOrderIdIn(Arrays.asList(ids));
        List<TQingzhuOrder> orders = OrderMapper.selectByExample(example);
        StringBuilder errorOrderNos = new StringBuilder();
        if (!CollectionUtils.isEmpty(orders)) {
            for (TQingzhuOrder Order : orders) {
                if (Order.getIsDeleted() == 1) {
                    errorOrderNos.append(Order.getOrderNo()).append(" ");
                    continue;
                }
                if (Order.getOrderStatus() != 1 && Order.getOrderStatus() != 2) {
                    errorOrderNos.append(Order.getOrderNo()).append(" ");
                }
            }
            if (StringUtils.isEmpty(errorOrderNos.toString())) {
                //订单状态正常 可以执行出库操作 修改订单状态和更新时间
                if (OrderMapper.checkOut(Arrays.asList(ids)) > 0) {
                    return ServiceResultEnum.SUCCESS.getResult();
                } else {
                    return ServiceResultEnum.DB_ERROR.getResult();
                }
            } else {
                //订单此时不可执行出库操作
                if (errorOrderNos.length() > 0 && errorOrderNos.length() < 100) {
                    return errorOrderNos + "订单的状态不是支付成功或配货完成无法执行出库操作";
                } else {
                    return "你选择了太多状态不是支付成功或配货完成的订单，无法执行出库操作";
                }
            }
        }
        //未查询到数据 返回错误提示
        return ServiceResultEnum.DATA_NOT_EXIST.getResult();
    }

    @Override
    @Transactional
    public String closeOrder(Long[] ids) {
        //查询所有的订单 判断状态 修改状态和更新时间
        TQingzhuOrderExample example=new TQingzhuOrderExample();
        example.createCriteria().andOrderIdIn(Arrays.asList(ids));
        List<TQingzhuOrder> orders = OrderMapper.selectByExample(example);
        StringBuilder errorOrderNos = new StringBuilder();
        if (!CollectionUtils.isEmpty(orders)) {
            for (TQingzhuOrder Order : orders) {
                // isDeleted=1 一定为已关闭订单
                if (Order.getIsDeleted() == 1) {
                    errorOrderNos.append(Order.getOrderNo()).append(" ");
                    continue;
                }
                //已关闭或者已完成无法关闭订单
                if (Order.getOrderStatus() == 4 || Order.getOrderStatus() < 0) {
                    errorOrderNos.append(Order.getOrderNo()).append(" ");
                }
            }
            if (StringUtils.isEmpty(errorOrderNos.toString())) {
                //订单状态正常 可以执行关闭操作 修改订单状态和更新时间
                if (OrderMapper.closeOrder(Arrays.asList(ids), OrderStatusEnum.ORDER_CLOSED_BY_JUDGE.getOrderStatus()) > 0) {
                    return ServiceResultEnum.SUCCESS.getResult();
                } else {
                    return ServiceResultEnum.DB_ERROR.getResult();
                }
            } else {
                //订单此时不可执行关闭操作
                if (errorOrderNos.length() > 0 && errorOrderNos.length() < 100) {
                    return errorOrderNos + "订单不能执行关闭操作";
                } else {
                    return "你选择的订单不能执行关闭操作";
                }
            }
        }
        //未查询到数据 返回错误提示
        return ServiceResultEnum.DATA_NOT_EXIST.getResult();
    }

    @Override
    @Transactional
    public String saveOrder(UserVO user, List<ShoppingCartItemVO> myShoppingCartItems) {
        List<Long> itemIdList = myShoppingCartItems.stream().map(ShoppingCartItemVO::getCartItemId).collect(Collectors.toList());
        List<Long> goodsIds = myShoppingCartItems.stream().map(ShoppingCartItemVO::getGoodsId).collect(Collectors.toList());
        TQingzhuGoodsInfoExample example=new TQingzhuGoodsInfoExample();
        example.createCriteria().andGoodsIdIn(goodsIds);
        List<TQingzhuGoodsInfo> Goods = GoodsMapper.selectByExample(example);
        Map<Long, TQingzhuGoodsInfo> GoodsMap = Goods.stream().collect(Collectors.toMap(TQingzhuGoodsInfo::getGoodsId, Function.identity(), (entity1, entity2) -> entity1));
        //判断商品库存
        for (ShoppingCartItemVO shoppingCartItemVO : myShoppingCartItems) {
            //查出的商品中不存在购物车中的这条关联商品数据，直接返回错误提醒
            if (!GoodsMap.containsKey(shoppingCartItemVO.getGoodsId())) {
                Exception.fail(ServiceResultEnum.SHOPPING_ITEM_ERROR.getResult());
            }
            //存在数量大于库存的情况，直接返回错误提醒
            if (shoppingCartItemVO.getGoodsCount() > GoodsMap.get(shoppingCartItemVO.getGoodsId()).getStockNum()) {
                Exception.fail(ServiceResultEnum.SHOPPING_ITEM_COUNT_ERROR.getResult());
            }
        }
        //删除购物项
        if (!CollectionUtils.isEmpty(itemIdList) && !CollectionUtils.isEmpty(goodsIds) && !CollectionUtils.isEmpty(Goods)) {
            TQingzhuShoppingCartItemExample example1=new TQingzhuShoppingCartItemExample();
            example1.createCriteria().andCartItemIdIn(itemIdList);
            if (ShoppingCartItemMapper.deleteByExample(example1) > 0) {
                List<StockNumDTO> stockNumDTOS = BeanUtil.copyList(myShoppingCartItems, StockNumDTO.class);
                int updateStockNumResult = GoodsMapper.updateStockNum(stockNumDTOS);
                if (updateStockNumResult < 1) {
                    Exception.fail(ServiceResultEnum.SHOPPING_ITEM_COUNT_ERROR.getResult());
                }
                //生成订单号
                String orderNo = NumberUtil.genOrderNo();
                int priceTotal = 0;
                //保存订单
                TQingzhuOrder Order = new TQingzhuOrder();
                Order.setOrderNo(orderNo);
                Order.setUserId(user.getUserId());
                Order.setUserAddress(user.getAddress());
                //总价
                for (ShoppingCartItemVO ShoppingCartItemVO : myShoppingCartItems) {
                    priceTotal += ShoppingCartItemVO.getGoodsCount() * ShoppingCartItemVO.getSellingPrice();
                }
                if (priceTotal < 1) {
                    Exception.fail(ServiceResultEnum.ORDER_PRICE_ERROR.getResult());
                }
                Order.setTotalPrice(priceTotal);
                //todo 订单body字段，用来作为生成支付单描述信息，暂时未接入第三方支付接口，故该字段暂时设为空字符串
                String extraInfo = "";
                Order.setExtraInfo(extraInfo);
                //生成订单项并保存订单项纪录
                if (OrderMapper.insertSelective(Order) > 0) {
                    //生成所有的订单项快照，并保存至数据库
                    List<TQingzhuOrderItem> OrderItems = new ArrayList<>();
                    for (ShoppingCartItemVO ShoppingCartItemVO : myShoppingCartItems) {
                        TQingzhuOrderItem OrderItem = new TQingzhuOrderItem();
                        //使用BeanUtil工具类将newBeeMallShoppingCartItemVO中的属性复制到newBeeMallOrderItem对象中
                        BeanUtil.copyProperties(ShoppingCartItemVO, OrderItem);
                        //NewBeeMallOrderMapper文件insert()方法中使用了useGeneratedKeys因此orderId可以获取到
                        OrderItem.setOrderId(Order.getOrderId());
                        OrderItems.add(OrderItem);
                    }
                    //保存至数据库
                    int flag=0;
                    for(TQingzhuOrderItem orderItem:OrderItems){
                        if (OrderItemMapper.insertSelective(orderItem) > 0) {
                            //所有操作成功后，将订单号返回，以供Controller方法跳转到订单详情
                            flag++;

                        }
                    }
                    if(flag==OrderItems.size()){
                        return orderNo;
                    }
                    Exception.fail(ServiceResultEnum.ORDER_PRICE_ERROR.getResult());
                }
                Exception.fail(ServiceResultEnum.DB_ERROR.getResult());
            }
            Exception.fail(ServiceResultEnum.DB_ERROR.getResult());
        }
        Exception.fail(ServiceResultEnum.SHOPPING_ITEM_ERROR.getResult());
        return ServiceResultEnum.SHOPPING_ITEM_ERROR.getResult();
    }

    @Override
    public OrderDetailVO getOrderDetailByOrderNo(String orderNo, Long userId) {
        TQingzhuOrderExample example=new TQingzhuOrderExample();
        example.createCriteria().andOrderNoEqualTo(orderNo);
        TQingzhuOrder Order = OrderMapper.selectByExample(example).get(0);
        if (Order != null) {
            //todo 验证是否是当前userId下的订单，否则报错
            TQingzhuOrderItemExample example1=new TQingzhuOrderItemExample();
            example1.createCriteria().andOrderIdEqualTo(Order.getOrderId());
            List<TQingzhuOrderItem> orderItems = OrderItemMapper.selectByExample(example1);
            //获取订单项数据
            if (!CollectionUtils.isEmpty(orderItems)) {
                List<OrderItemVO> OrderItemVOS = BeanUtil.copyList(orderItems, OrderItemVO.class);
                OrderDetailVO OrderDetailVO = new OrderDetailVO();
                BeanUtil.copyProperties(Order, OrderDetailVO);
                OrderDetailVO.setOrderStatusString(OrderStatusEnum.getOrderStatusEnumByStatus(OrderDetailVO.getOrderStatus()).getName());
                OrderDetailVO.setPayTypeString(PayTypeEnum.getPayTypeEnumByType(OrderDetailVO.getPayType()).getName());
                OrderDetailVO.setNewBeeMallOrderItemVOS(OrderItemVOS);
                return OrderDetailVO;
            }
        }
        return null;
    }

    @Override
    public TQingzhuOrder getOrderByOrderNo(String orderNo) {
        TQingzhuOrderExample example=new TQingzhuOrderExample();
        example.createCriteria().andOrderNoEqualTo(orderNo);
        return OrderMapper.selectByExample(example).get(0);
    }

    @Override
    public PageInfo getMyOrders(Long userid,int page,int limit) {

        TQingzhuOrderExample example=new TQingzhuOrderExample();
        example.createCriteria().andUserIdEqualTo(userid);
        PageHelper.startPage(page, limit);
        List<TQingzhuOrder> Orders = OrderMapper.selectByExample(example);
        PageInfo<TQingzhuOrder> pageInfo=new PageInfo<>(Orders);
        List<OrderListVO> orderListVOS = new ArrayList<>();
        if ( pageInfo.getTotal()> 0) {
            //数据转换 将实体类转成vo
            orderListVOS = BeanUtil.copyList(Orders, OrderListVO.class);
            //设置订单状态中文显示值
            for (OrderListVO OrderListVO : orderListVOS) {
                OrderListVO.setOrderStatusString(OrderStatusEnum.getOrderStatusEnumByStatus(OrderListVO.getOrderStatus()).getName());
            }
            List<Long> orderIds = Orders.stream().map(TQingzhuOrder::getOrderId).collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(orderIds)) {
                TQingzhuOrderItemExample example1=new TQingzhuOrderItemExample();
                example1.createCriteria().andOrderIdIn(orderIds);
                List<TQingzhuOrderItem> orderItems = OrderItemMapper.selectByExample(example1);
                Map<Long, List<TQingzhuOrderItem>> itemByOrderIdMap = orderItems.stream().collect(groupingBy(TQingzhuOrderItem::getOrderId));
                for (OrderListVO OrderListVO : orderListVOS) {
                    //封装每个订单列表对象的订单项数据
                    if (itemByOrderIdMap.containsKey(OrderListVO.getOrderId())) {
                        List<TQingzhuOrderItem> orderItemListTemp = itemByOrderIdMap.get(OrderListVO.getOrderId());
                        //将OrderItem对象列表转换成OrderItemVO对象列表
                        List<OrderItemVO> OrderItemVOS = BeanUtil.copyList(orderItemListTemp, OrderItemVO.class);
                        OrderListVO.setNewBeeMallOrderItemVOS(OrderItemVOS);
                    }
                }
            }
        }

        PageInfo<OrderListVO> pageInfo1=new PageInfo<>(orderListVOS);
        pageInfo1.setPageSize(limit);
        pageInfo1.setPageNum(page);
        return pageInfo1;
    }

    @Override
    public String cancelOrder(String orderNo, Long userId) {
        TQingzhuOrderExample example=new TQingzhuOrderExample();
        example.createCriteria().andOrderNoEqualTo(orderNo);
        TQingzhuOrder Order = OrderMapper.selectByExample(example).get(0);
        if (Order != null) {
            //todo 验证是否是当前userId下的订单，否则报错
            //todo 订单状态判断
            if (OrderMapper.closeOrder(Collections.singletonList(Order.getOrderId()), OrderStatusEnum.ORDER_CLOSED_BY_MALLUSER.getOrderStatus()) > 0) {
                return ServiceResultEnum.SUCCESS.getResult();
            } else {
                return ServiceResultEnum.DB_ERROR.getResult();
            }
        }
        return ServiceResultEnum.ORDER_NOT_EXIST_ERROR.getResult();
    }

    @Override
    public String finishOrder(String orderNo, Long userId) {
        TQingzhuOrderExample example=new TQingzhuOrderExample();
        example.createCriteria().andOrderNoEqualTo(orderNo);
        TQingzhuOrder Order = OrderMapper.selectByExample(example).get(0);
        if (Order != null) {
            //todo 验证是否是当前userId下的订单，否则报错
            //todo 订单状态判断
            Order.setOrderStatus((byte) OrderStatusEnum.ORDER_SUCCESS.getOrderStatus());
            Order.setUpdateTime(new Date());
            if (OrderMapper.updateByPrimaryKeySelective(Order) > 0) {
                return ServiceResultEnum.SUCCESS.getResult();
            } else {
                return ServiceResultEnum.DB_ERROR.getResult();
            }
        }
        return ServiceResultEnum.ORDER_NOT_EXIST_ERROR.getResult();
    }

    @Override
    public String paySuccess(String orderNo, int payType) {
        TQingzhuOrderExample example=new TQingzhuOrderExample();
        example.createCriteria().andOrderNoEqualTo(orderNo);
        TQingzhuOrder Order = OrderMapper.selectByExample(example).get(0);
        if (Order != null) {
            //todo 订单状态判断 非待支付状态下不进行修改操作
            Order.setOrderStatus((byte) OrderStatusEnum.OREDER_PAID.getOrderStatus());
            Order.setPayType((byte) payType);
            Order.setPayStatus((byte) PayStatusEnum.PAY_SUCCESS.getPayStatus());
            Order.setPayTime(new Date());
            Order.setUpdateTime(new Date());
            if (OrderMapper.updateByPrimaryKeySelective(Order) > 0) {
                return ServiceResultEnum.SUCCESS.getResult();
            } else {
                return ServiceResultEnum.DB_ERROR.getResult();
            }
        }
        return ServiceResultEnum.ORDER_NOT_EXIST_ERROR.getResult();
    }

    @Override
    public List<OrderItemVO> getOrderItems(Long id) {
        TQingzhuOrder Order = OrderMapper.selectByPrimaryKey(id);
        if (Order != null) {
            TQingzhuOrderItemExample example=new TQingzhuOrderItemExample();
            example.createCriteria().andOrderIdEqualTo(Order.getOrderId());
            List<TQingzhuOrderItem> orderItems = OrderItemMapper.selectByExample(example);
            //获取订单项数据
            if (!CollectionUtils.isEmpty(orderItems)) {
                return BeanUtil.copyList(orderItems, OrderItemVO.class);
            }
        }
        return null;
    }
    @Override
    public List<OrderListVO> getAllOrderList(){
            TQingzhuOrderExample example=new TQingzhuOrderExample();
            example.createCriteria().andOrderIdIsNotNull();
            List<TQingzhuOrder> orders = OrderMapper.selectByExample(example);
            //获取订单项数据
            if (!CollectionUtils.isEmpty(orders)) {
                return BeanUtil.copyList(orders, OrderListVO.class);
            }
        return null;
    }
}