package new_qingzhu.demo.Pojo;

import java.util.Date;

public class TQingzhuIndexConfig {
    private Long configId;

    private String configName;

    private Byte configType;

    private Long goodsId;

    private String redirectUrl;

    private Integer configRank;

    private Byte isDeleted;

    private Date createTime;

    private Integer createUser;

    private Date updateTime;

    private Integer updateUser;

    public TQingzhuIndexConfig(Long configId, String configName, Byte configType, Long goodsId, String redirectUrl, Integer configRank, Byte isDeleted, Date createTime, Integer createUser, Date updateTime, Integer updateUser) {
        this.configId = configId;
        this.configName = configName;
        this.configType = configType;
        this.goodsId = goodsId;
        this.redirectUrl = redirectUrl;
        this.configRank = configRank;
        this.isDeleted = isDeleted;
        this.createTime = createTime;
        this.createUser = createUser;
        this.updateTime = updateTime;
        this.updateUser = updateUser;
    }

    public TQingzhuIndexConfig() {
        super();
    }

    public Long getConfigId() {
        return configId;
    }

    public void setConfigId(Long configId) {
        this.configId = configId;
    }

    public String getConfigName() {
        return configName;
    }

    public void setConfigName(String configName) {
        this.configName = configName == null ? null : configName.trim();
    }

    public Byte getConfigType() {
        return configType;
    }

    public void setConfigType(Byte configType) {
        this.configType = configType;
    }

    public Long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl == null ? null : redirectUrl.trim();
    }

    public Integer getConfigRank() {
        return configRank;
    }

    public void setConfigRank(Integer configRank) {
        this.configRank = configRank;
    }

    public Byte getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Byte isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getCreateUser() {
        return createUser;
    }

    public void setCreateUser(Integer createUser) {
        this.createUser = createUser;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(Integer updateUser) {
        this.updateUser = updateUser;
    }
}