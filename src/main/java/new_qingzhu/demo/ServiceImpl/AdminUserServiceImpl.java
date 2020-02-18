package new_qingzhu.demo.ServiceImpl;


import new_qingzhu.demo.Mapper.TQingzhuAdminUserMapper;
import new_qingzhu.demo.Pojo.TQingzhuAdminUser;
import new_qingzhu.demo.Pojo.TQingzhuAdminUserExample;
import new_qingzhu.demo.Service.AdminUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class AdminUserServiceImpl implements AdminUserService {

    private PasswordEncoder passwordEncoder;
    private TQingzhuAdminUserMapper adminUserMapper;
    @Autowired
    public AdminUserServiceImpl(TQingzhuAdminUserMapper adminUserMapper,PasswordEncoder passwordEncoder){
        this.adminUserMapper=adminUserMapper;
        this.passwordEncoder=passwordEncoder;
    }

    @Override
    public TQingzhuAdminUser login(String userName, String password) {
        TQingzhuAdminUserExample example=new TQingzhuAdminUserExample();
        example.createCriteria().andLoginUserNameEqualTo(userName);
        TQingzhuAdminUser user=adminUserMapper.selectByExample(example).get(0);
        if (passwordEncoder.matches(password,user.getLoginPassword())){
            return user;
        }
        return null;
    }

    @Override
    public TQingzhuAdminUser getUserDetailById(Integer loginUserId) {
        return adminUserMapper.selectByPrimaryKey(loginUserId);
    }

    @Override
    public Boolean updatePassword(Integer loginUserId, String originalPassword, String newPassword) {
        TQingzhuAdminUser adminUser = adminUserMapper.selectByPrimaryKey(loginUserId);
        //当前用户非空才可以进行更改
        if (adminUser != null) {
            String newPasswordMd5 = passwordEncoder.encode(newPassword);
            //比较原密码是否正确
            if (passwordEncoder.matches(originalPassword,adminUser.getLoginPassword())) {
                //设置新密码并修改
                adminUser.setLoginPassword(newPasswordMd5);
                //修改成功则返回true
                return adminUserMapper.updateByPrimaryKeySelective(adminUser) > 0;
            }
        }
        return false;
    }

    @Override
    public Boolean updateName(Integer loginUserId, String loginUserName, String nickName) {
        TQingzhuAdminUser adminUser = adminUserMapper.selectByPrimaryKey(loginUserId);
        //当前用户非空才可以进行更改
        if (adminUser != null) {
            //设置新名称并修改
            adminUser.setLoginUserName(loginUserName);
            adminUser.setNickName(nickName);
            //修改成功则返回true
            return adminUserMapper.updateByPrimaryKeySelective(adminUser) > 0;
        }
        return false;
    }
}
