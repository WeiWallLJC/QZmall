package new_qingzhu.demo.Controller.Qingzhu;


import new_qingzhu.demo.Common.Constants;
import new_qingzhu.demo.Common.ServiceResultEnum;
import new_qingzhu.demo.Pojo.TQingzhuShoppingCartItem;
import new_qingzhu.demo.Service.ShoppingCartService;
import new_qingzhu.demo.Util.Result;
import new_qingzhu.demo.Util.ResultGenerator;
import new_qingzhu.demo.VO.ShoppingCartItemVO;
import new_qingzhu.demo.VO.UserVO;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class ShoppingCartController {

    @Resource
    private ShoppingCartService ShoppingCartService;

    @GetMapping("/shop-cart")
    public String cartListPage(HttpServletRequest request,
                               HttpSession httpSession) {
        UserVO user = (UserVO) httpSession.getAttribute(Constants.MALL_USER_SESSION_KEY);
        int itemsTotal = 0;
        int priceTotal = 0;
        List<ShoppingCartItemVO> myShoppingCartItems = ShoppingCartService.getMyShoppingCartItems(user.getUserId());
        if (!CollectionUtils.isEmpty(myShoppingCartItems)) {
            //订单项总数
            itemsTotal = myShoppingCartItems.stream().mapToInt(ShoppingCartItemVO::getGoodsCount).sum();
            if (itemsTotal < 1) {
                return "error/error_5xx";
            }
            //总价
            for (ShoppingCartItemVO ShoppingCartItemVO : myShoppingCartItems) {
                priceTotal += ShoppingCartItemVO.getGoodsCount() * ShoppingCartItemVO.getSellingPrice();
            }
            if (priceTotal < 1) {
                return "error/error_5xx";
            }
        }
        request.setAttribute("itemsTotal", itemsTotal);
        request.setAttribute("priceTotal", priceTotal);
        request.setAttribute("myShoppingCartItems", myShoppingCartItems);
        return "mall/cart";
    }

    @PostMapping("/shop-cart")
    @ResponseBody
    public Result saveShoppingCartItem(@RequestBody TQingzhuShoppingCartItem TQingzhuShoppingCartItem,
                                       HttpSession httpSession) {
        UserVO user = (UserVO) httpSession.getAttribute(Constants.MALL_USER_SESSION_KEY);
        TQingzhuShoppingCartItem.setUserId(user.getUserId());
        //todo 判断数量
        String saveResult = ShoppingCartService.saveCartItem(TQingzhuShoppingCartItem);
        //添加成功
        if (ServiceResultEnum.SUCCESS.getResult().equals(saveResult)) {
            return ResultGenerator.genSuccessResult();
        }
        //添加失败
        return ResultGenerator.genFailResult(saveResult);
    }

    @PutMapping("/shop-cart")
    @ResponseBody
    public Result updateShoppingCartItem(@RequestBody TQingzhuShoppingCartItem TQingzhuShoppingCartItem,
                                                   HttpSession httpSession) {
        UserVO user = (UserVO) httpSession.getAttribute(Constants.MALL_USER_SESSION_KEY);
        TQingzhuShoppingCartItem.setUserId(user.getUserId());
        //todo 判断数量
        String saveResult = ShoppingCartService.updateCartItem(TQingzhuShoppingCartItem);
        //修改成功
        if (ServiceResultEnum.SUCCESS.getResult().equals(saveResult)) {
            return ResultGenerator.genSuccessResult();
        }
        //修改失败
        return ResultGenerator.genFailResult(saveResult);
    }

    @DeleteMapping("/shop-cart/{ShoppingCartItemId}")
    @ResponseBody
    public Result updateShoppingCartItem(@PathVariable("ShoppingCartItemId") Long ShoppingCartItemId,
                                                   HttpSession httpSession) {
        UserVO user = (UserVO) httpSession.getAttribute(Constants.MALL_USER_SESSION_KEY);
        Boolean deleteResult = ShoppingCartService.deleteById(ShoppingCartItemId);
        //删除成功
        if (deleteResult) {
            return ResultGenerator.genSuccessResult();
        }
        //删除失败
        return ResultGenerator.genFailResult(ServiceResultEnum.OPERATE_ERROR.getResult());
    }

    @GetMapping("/shop-cart/settle")
    public String settlePage(HttpServletRequest request,
                             HttpSession httpSession) {
        int priceTotal = 0;
        UserVO user = (UserVO) httpSession.getAttribute(Constants.MALL_USER_SESSION_KEY);
        List<ShoppingCartItemVO> myShoppingCartItems = ShoppingCartService.getMyShoppingCartItems(user.getUserId());
        if (CollectionUtils.isEmpty(myShoppingCartItems)) {
            //无数据则不跳转至结算页
            return "/shop-cart";
        } else {
            //总价
            for (ShoppingCartItemVO ShoppingCartItemVO : myShoppingCartItems) {
                priceTotal += ShoppingCartItemVO.getGoodsCount() * ShoppingCartItemVO.getSellingPrice();
            }
            if (priceTotal < 1) {
                return "error/error_5xx";
            }
        }
        request.setAttribute("priceTotal", priceTotal);
        request.setAttribute("myShoppingCartItems", myShoppingCartItems);
        return "mall/order-settle";
    }
}
