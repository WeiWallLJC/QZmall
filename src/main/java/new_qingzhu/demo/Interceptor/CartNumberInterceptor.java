package new_qingzhu.demo.Interceptor;


import new_qingzhu.demo.Common.Constants;
import new_qingzhu.demo.Mapper.TQingzhuShoppingCartItemMapper;
import new_qingzhu.demo.Pojo.TQingzhuShoppingCartItemExample;
import new_qingzhu.demo.VO.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@Component
public class CartNumberInterceptor implements HandlerInterceptor {

    private TQingzhuShoppingCartItemMapper ShoppingCartItemMapper;

    @Autowired
    public CartNumberInterceptor(TQingzhuShoppingCartItemMapper shoppingCartItemMapper){
        this.ShoppingCartItemMapper=shoppingCartItemMapper;
    }

    @Override//在一个请求返回Controller层方法方法执行前执行这个方法判断登陆情况
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) {
        //购物车中的数量会更改，但是在这些接口中并没有对session中的数据做修改，这里统一处理一下
        if (null != request.getSession() && null != request.getSession().getAttribute(Constants.MALL_USER_SESSION_KEY)) {
            //如果当前为登陆状态，就查询数据库并设置购物车中的数量值
            UserVO UserVO = (UserVO) request.getSession().getAttribute(Constants.MALL_USER_SESSION_KEY);
            //设置购物车中的数量
            TQingzhuShoppingCartItemExample example=new TQingzhuShoppingCartItemExample();
            example.createCriteria().andUserIdEqualTo(UserVO.getUserId());
            if(ShoppingCartItemMapper.selectByExample(example).size()!=0){
                UserVO.setShopCartItemCount(ShoppingCartItemMapper.selectByExample(example).get(0).getGoodsCount());
            }
            request.getSession().setAttribute(Constants.MALL_USER_SESSION_KEY, UserVO);
        }
        return true;
    }

    @Override//控制器调用之后，在解析视图之前，用此方法对请求域中的模型和视图做进一步的修改
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) {
    }

    @Override//整个请求完成之后，即视图渲染之后，可以用来资源清理或者记录日志信息
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) {

    }
}
