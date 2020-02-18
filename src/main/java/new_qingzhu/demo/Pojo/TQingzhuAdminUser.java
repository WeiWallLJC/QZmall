package new_qingzhu.demo.Pojo;

import java.util.Date;

public class TQingzhuAdminUser {
    private Integer adminUserId;

    private String loginUserName;

    private String loginPassword;

    private String nickName;

    private Byte locked;

    private Date createTime;

    private Date updateTime;

    public TQingzhuAdminUser(Integer adminUserId, String loginUserName, String loginPassword, String nickName, Byte locked, Date createTime, Date updateTime) {
        this.adminUserId = adminUserId;
        this.loginUserName = loginUserName;
        this.loginPassword = loginPassword;
        this.nickName = nickName;
        this.locked = locked;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    public TQingzhuAdminUser() {
        super();
    }

    public Integer getAdminUserId() {
        return adminUserId;
    }

    public void setAdminUserId(Integer adminUserId) {
        this.adminUserId = adminUserId;
    }

    public String getLoginUserName() {
        return loginUserName;
    }

    public void setLoginUserName(String loginUserName) {
        this.loginUserName = loginUserName == null ? null : loginUserName.trim();
    }

    public String getLoginPassword() {
        return loginPassword;
    }

    public void setLoginPassword(String loginPassword) {
        this.loginPassword = loginPassword == null ? null : loginPassword.trim();
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName == null ? null : nickName.trim();
    }

    public Byte getLocked() {
        return locked;
    }

    public void setLocked(Byte locked) {
        this.locked = locked;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}