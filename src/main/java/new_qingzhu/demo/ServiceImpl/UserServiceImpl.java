package new_qingzhu.demo.ServiceImpl;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import new_qingzhu.demo.Common.Constants;
import new_qingzhu.demo.Common.ServiceResultEnum;
import new_qingzhu.demo.Mapper.TQingzhuUserMapper;
import new_qingzhu.demo.Pojo.TQingzhuUser;
import new_qingzhu.demo.Pojo.TQingzhuUserExample;
import new_qingzhu.demo.Service.UserService;
import new_qingzhu.demo.Util.BeanUtil;
import new_qingzhu.demo.VO.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private PasswordEncoder passwordEncoder;
    private TQingzhuUserMapper UserMapper;
    @Autowired
    public  UserServiceImpl(TQingzhuUserMapper userMapper,PasswordEncoder passwordEncoder){
        this.UserMapper=userMapper;
        this.passwordEncoder=passwordEncoder;
    }


    @Override
    public PageInfo getUsersPage(int page,int limit) {
        TQingzhuUserExample example=new TQingzhuUserExample();
        example.createCriteria().andUserIdIsNotNull();
        PageHelper.startPage(page, limit);
        List<TQingzhuUser> Users = UserMapper.selectByExample(example);
        PageInfo<TQingzhuUser> pageInfo=new PageInfo<>(Users);
        pageInfo.setPageNum(page);
        pageInfo.setPageSize(limit);

        return pageInfo;
    }

    @Override
    public String register(String loginName, String password) {
        TQingzhuUserExample example=new TQingzhuUserExample();
        example.createCriteria().andLoginNameEqualTo(loginName);
         List<TQingzhuUser> User=UserMapper.selectByExample(example);
        if (User.size()!=0) {
            return ServiceResultEnum.SAME_LOGIN_NAME_EXIST.getResult();
        }
        TQingzhuUser registerUser = new TQingzhuUser();
        registerUser.setLoginName(loginName);
        registerUser.setNickName(loginName);
        String passwordEn = passwordEncoder.encode(password);
        registerUser.setPasswordMd5(passwordEn);
        if (UserMapper.insertSelective(registerUser) > 0) {
            return ServiceResultEnum.SUCCESS.getResult();
        }
        return ServiceResultEnum.DB_ERROR.getResult();
    }

    @Override
    public String login(String loginName, String passwordMD5, HttpSession httpSession) {
        TQingzhuUser user=null;
        TQingzhuUserExample example=new TQingzhuUserExample();
        example.createCriteria().andLoginNameEqualTo(loginName);
        if(passwordEncoder.matches(passwordMD5,UserMapper.selectByExample(example).get(0).getPasswordMd5())){
             user = UserMapper.selectByExample(example).get(0);
        }
        if (user != null && httpSession != null) {
            if (user.getLockedFlag() == 1) {
                return ServiceResultEnum.LOGIN_USER_LOCKED.getResult();
            }
            //昵称太长 影响页面展示
            if (user.getNickName() != null && user.getNickName().length() > 7) {
                String tempNickName = user.getNickName().substring(0, 7) + "..";
                user.setNickName(tempNickName);
            }
            UserVO UserVO = new UserVO();
            BeanUtil.copyProperties(user, UserVO);
            //设置购物车中的数量
            httpSession.setAttribute(Constants.MALL_USER_SESSION_KEY, UserVO);
            return ServiceResultEnum.SUCCESS.getResult();
        }
        return ServiceResultEnum.LOGIN_ERROR.getResult();
    }

    @Override
    public UserVO updateUserInfo(TQingzhuUser User, HttpSession httpSession) {
        TQingzhuUser user = UserMapper.selectByPrimaryKey(User.getUserId());
        if (user != null) {
            user.setNickName(User.getNickName());
            user.setAddress(User.getAddress());
            user.setIntroduceSign(User.getIntroduceSign());
            if (UserMapper.updateByPrimaryKeySelective(user) > 0) {
                UserVO UserVO = new UserVO();
                user = UserMapper.selectByPrimaryKey(User.getUserId());
                BeanUtil.copyProperties(user, UserVO);
                httpSession.setAttribute(Constants.MALL_USER_SESSION_KEY, UserVO);
                return UserVO;
            }
        }
        return null;
    }

    @Override
    public Boolean lockUsers(Long[] ids, int lockStatus) {
        if (ids.length < 1) {
            return false;
        }
        TQingzhuUserExample example=new TQingzhuUserExample();
        example.createCriteria().andUserIdIn(Arrays.asList(ids));
        TQingzhuUser user=new TQingzhuUser();
        user.setLockedFlag((byte) lockStatus);

        return UserMapper.updateByExampleSelective(user,example) > 0;
    }
}
