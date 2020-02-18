package new_qingzhu.demo.Mapper;

import new_qingzhu.demo.Pojo.TQingzhuAdminUser;
import new_qingzhu.demo.Pojo.TQingzhuAdminUserExample;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface TQingzhuAdminUserMapper {


    long countByExample(TQingzhuAdminUserExample example);

    int deleteByExample(TQingzhuAdminUserExample example);

    int deleteByPrimaryKey(Integer adminUserId);

    int insert(TQingzhuAdminUser record);

    int insertSelective(TQingzhuAdminUser record);

    List<TQingzhuAdminUser> selectByExample(TQingzhuAdminUserExample example);

    TQingzhuAdminUser selectByPrimaryKey(Integer adminUserId);

    int updateByExampleSelective(@Param("record") TQingzhuAdminUser record, @Param("example") TQingzhuAdminUserExample example);

    int updateByExample(@Param("record") TQingzhuAdminUser record, @Param("example") TQingzhuAdminUserExample example);

    int updateByPrimaryKeySelective(TQingzhuAdminUser record);

    int updateByPrimaryKey(TQingzhuAdminUser record);
}