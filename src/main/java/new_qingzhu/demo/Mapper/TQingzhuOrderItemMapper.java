package new_qingzhu.demo.Mapper;

import new_qingzhu.demo.Pojo.TQingzhuOrderItem;
import new_qingzhu.demo.Pojo.TQingzhuOrderItemExample;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface TQingzhuOrderItemMapper {
    long countByExample(TQingzhuOrderItemExample example);

    int deleteByExample(TQingzhuOrderItemExample example);

    int deleteByPrimaryKey(Long orderItemId);

    int insert(TQingzhuOrderItem record);

    int insertSelective(TQingzhuOrderItem record);

    List<TQingzhuOrderItem> selectByExample(TQingzhuOrderItemExample example);

    TQingzhuOrderItem selectByPrimaryKey(Long orderItemId);

    int updateByExampleSelective(@Param("record") TQingzhuOrderItem record, @Param("example") TQingzhuOrderItemExample example);

    int updateByExample(@Param("record") TQingzhuOrderItem record, @Param("example") TQingzhuOrderItemExample example);

    int updateByPrimaryKeySelective(TQingzhuOrderItem record);

    int updateByPrimaryKey(TQingzhuOrderItem record);
}