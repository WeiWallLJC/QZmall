package new_qingzhu.demo.Mapper;

import new_qingzhu.demo.Pojo.TQingzhuGoodsCategory;
import new_qingzhu.demo.Pojo.TQingzhuGoodsCategoryExample;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface TQingzhuGoodsCategoryMapper {

    long countByExample(TQingzhuGoodsCategoryExample example);

    int deleteByExample(TQingzhuGoodsCategoryExample example);

    int deleteByPrimaryKey(Long categoryId);

    int insert(TQingzhuGoodsCategory record);

    int insertSelective(TQingzhuGoodsCategory record);

    List<TQingzhuGoodsCategory> selectByExample(TQingzhuGoodsCategoryExample example);

    TQingzhuGoodsCategory selectByPrimaryKey(Long categoryId);

    int updateByExampleSelective(@Param("record") TQingzhuGoodsCategory record, @Param("example") TQingzhuGoodsCategoryExample example);

    int updateByExample(@Param("record") TQingzhuGoodsCategory record, @Param("example") TQingzhuGoodsCategoryExample example);

    int updateByPrimaryKeySelective(TQingzhuGoodsCategory record);

    int updateByPrimaryKey(TQingzhuGoodsCategory record);
}