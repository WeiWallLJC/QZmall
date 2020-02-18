package new_qingzhu.demo.Service;

import com.github.pagehelper.PageInfo;
import new_qingzhu.demo.Pojo.TQingzhuUser;
import new_qingzhu.demo.VO.UserVO;

import javax.servlet.http.HttpSession;

public interface UserService {
    /**
     * 后台分页
     *
     * @param page,limit
     * @return
     */
    PageInfo getUsersPage(int page,int limit);

    /**
     * 用户注册
     *
     * @param loginName
     * @param password
     * @return
     */
    String register(String loginName, String password);

    /**
     * 登录
     *
     * @param loginName
     * @param password
     * @param httpSession
     * @return
     */
    String login(String loginName, String password, HttpSession httpSession);

    /**
     * 用户信息修改并返回最新的用户信息
     *
     * @param User
     * @return
     */
    UserVO updateUserInfo(TQingzhuUser User, HttpSession httpSession);

    /**
     * 用户禁用与解除禁用(0-未锁定 1-已锁定)
     *
     * @param ids
     * @param lockStatus
     * @return
     */
    Boolean lockUsers( Long[] ids, int lockStatus);
}
