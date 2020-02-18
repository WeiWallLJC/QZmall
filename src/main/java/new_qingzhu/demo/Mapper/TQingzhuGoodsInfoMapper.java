package new_qingzhu.demo.Mapper;

import new_qingzhu.demo.Pojo.StockNumDTO;
import new_qingzhu.demo.Pojo.TQingzhuGoodsInfo;
import new_qingzhu.demo.Pojo.TQingzhuGoodsInfoExample;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface TQingzhuGoodsInfoMapper {
    long countByExample(TQingzhuGoodsInfoExample example);

    int deleteByExample(TQingzhuGoodsInfoExample example);

    int deleteByPrimaryKey(Long goodsId);

    int insert(TQingzhuGoodsInfo record);

    int insertSelective(TQingzhuGoodsInfo record);

    List<TQingzhuGoodsInfo> selectByExampleWithBLOBs(TQingzhuGoodsInfoExample example);

    List<TQingzhuGoodsInfo> selectByExample(TQingzhuGoodsInfoExample example);

    TQingzhuGoodsInfo selectByPrimaryKey(Long goodsId);

    int updateByExampleSelective(@Param("record") TQingzhuGoodsInfo record, @Param("example") TQingzhuGoodsInfoExample example);

    int updateByExampleWithBLOBs(@Param("record") TQingzhuGoodsInfo record, @Param("example") TQingzhuGoodsInfoExample example);

    int updateByExample(@Param("record") TQingzhuGoodsInfo record, @Param("example") TQingzhuGoodsInfoExample example);

    int updateByPrimaryKeySelective(TQingzhuGoodsInfo record);

    int updateByPrimaryKeyWithBLOBs(TQingzhuGoodsInfo record);

    int updateByPrimaryKey(TQingzhuGoodsInfo record);

    int updateStockNum(@Param("stockNumDTOS")List<StockNumDTO> stockNumDTOS);
}