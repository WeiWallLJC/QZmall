package new_qingzhu.demo.Mapper;

import new_qingzhu.demo.Pojo.TQingzhuOrder;
import new_qingzhu.demo.Pojo.TQingzhuOrderExample;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface TQingzhuOrderMapper {
    long countByExample(TQingzhuOrderExample example);

    int deleteByExample(TQingzhuOrderExample example);

    int deleteByPrimaryKey(Long orderId);

    int insert(TQingzhuOrder record);

    int insertSelective(TQingzhuOrder record);

    List<TQingzhuOrder> selectByExample(TQingzhuOrderExample example);

    TQingzhuOrder selectByPrimaryKey(Long orderId);

    int updateByExampleSelective(@Param("record") TQingzhuOrder record, @Param("example") TQingzhuOrderExample example);

    int updateByExample(@Param("record") TQingzhuOrder record, @Param("example") TQingzhuOrderExample example);

    int updateByPrimaryKeySelective(TQingzhuOrder record);

    int updateByPrimaryKey(TQingzhuOrder record);

    int checkDone(@Param("orderIds") List<Long> asList);

    int checkOut(@Param("orderIds") List<Long> orderIds);

    int closeOrder(@Param("orderIds") List<Long> orderIds, @Param("orderStatus") int orderStatus);
}