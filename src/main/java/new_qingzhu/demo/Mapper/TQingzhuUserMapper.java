package new_qingzhu.demo.Mapper;

import new_qingzhu.demo.Pojo.TQingzhuUser;
import new_qingzhu.demo.Pojo.TQingzhuUserExample;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface TQingzhuUserMapper {
    long countByExample(TQingzhuUserExample example);

    int deleteByExample(TQingzhuUserExample example);

    int deleteByPrimaryKey(Long userId);

    int insert(TQingzhuUser record);

    int insertSelective(TQingzhuUser record);

    List<TQingzhuUser> selectByExample(TQingzhuUserExample example);

    TQingzhuUser selectByPrimaryKey(Long userId);

    int updateByExampleSelective(@Param("record") TQingzhuUser record, @Param("example") TQingzhuUserExample example);

    int updateByExample(@Param("record") TQingzhuUser record, @Param("example") TQingzhuUserExample example);

    int updateByPrimaryKeySelective(TQingzhuUser record);

    int updateByPrimaryKey(TQingzhuUser record);
}