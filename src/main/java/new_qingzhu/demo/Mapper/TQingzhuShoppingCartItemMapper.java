package new_qingzhu.demo.Mapper;

import new_qingzhu.demo.Pojo.TQingzhuShoppingCartItem;
import new_qingzhu.demo.Pojo.TQingzhuShoppingCartItemExample;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface TQingzhuShoppingCartItemMapper {
    long countByExample(TQingzhuShoppingCartItemExample example);

    int deleteByExample(TQingzhuShoppingCartItemExample example);

    int deleteByPrimaryKey(Long cartItemId);

    int insert(TQingzhuShoppingCartItem record);

    int insertSelective(TQingzhuShoppingCartItem record);

    List<TQingzhuShoppingCartItem> selectByExample(TQingzhuShoppingCartItemExample example);

    TQingzhuShoppingCartItem selectByPrimaryKey(Long cartItemId);

    int updateByExampleSelective(@Param("record") TQingzhuShoppingCartItem record, @Param("example") TQingzhuShoppingCartItemExample example);

    int updateByExample(@Param("record") TQingzhuShoppingCartItem record, @Param("example") TQingzhuShoppingCartItemExample example);

    int updateByPrimaryKeySelective(TQingzhuShoppingCartItem record);

    int updateByPrimaryKey(TQingzhuShoppingCartItem record);
}