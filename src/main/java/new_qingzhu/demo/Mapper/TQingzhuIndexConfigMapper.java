package new_qingzhu.demo.Mapper;

import new_qingzhu.demo.Pojo.TQingzhuIndexConfig;
import new_qingzhu.demo.Pojo.TQingzhuIndexConfigExample;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface TQingzhuIndexConfigMapper {
    long countByExample(TQingzhuIndexConfigExample example);

    int deleteByExample(TQingzhuIndexConfigExample example);

    int deleteByPrimaryKey(Long configId);

    int insert(TQingzhuIndexConfig record);

    int insertSelective(TQingzhuIndexConfig record);

    List<TQingzhuIndexConfig> selectByExample(TQingzhuIndexConfigExample example);

    TQingzhuIndexConfig selectByPrimaryKey(Long configId);

    int updateByExampleSelective(@Param("record") TQingzhuIndexConfig record, @Param("example") TQingzhuIndexConfigExample example);

    int updateByExample(@Param("record") TQingzhuIndexConfig record, @Param("example") TQingzhuIndexConfigExample example);

    int updateByPrimaryKeySelective(TQingzhuIndexConfig record);

    int updateByPrimaryKey(TQingzhuIndexConfig record);

}