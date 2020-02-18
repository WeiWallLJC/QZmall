package new_qingzhu.demo.ServiceImpl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import new_qingzhu.demo.Common.ServiceResultEnum;
import new_qingzhu.demo.Mapper.TQingzhuGoodsInfoMapper;
import new_qingzhu.demo.Mapper.TQingzhuIndexConfigMapper;
import new_qingzhu.demo.Pojo.TQingzhuGoodsInfo;
import new_qingzhu.demo.Pojo.TQingzhuGoodsInfoExample;
import new_qingzhu.demo.Pojo.TQingzhuIndexConfig;
import new_qingzhu.demo.Pojo.TQingzhuIndexConfigExample;
import new_qingzhu.demo.Service.IndexConfigService;
import new_qingzhu.demo.Util.BeanUtil;
import new_qingzhu.demo.VO.IndexConfigGoodsVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class IndexConfigServiceImpl implements IndexConfigService {

    private TQingzhuIndexConfigMapper indexConfigMapper;
    private TQingzhuGoodsInfoMapper goodsMapper;
    @Autowired
    public IndexConfigServiceImpl(TQingzhuGoodsInfoMapper goodsMapper,TQingzhuIndexConfigMapper indexConfigMapper){
        this.goodsMapper=goodsMapper;
        this.indexConfigMapper=indexConfigMapper;
    }

    @Override
    public PageInfo getConfigsPage(int page,int limit) {
        TQingzhuIndexConfigExample example =new TQingzhuIndexConfigExample();
        example.createCriteria().andConfigIdIsNotNull();
        PageHelper.startPage(page, limit);
        List<TQingzhuIndexConfig> indexConfigs = indexConfigMapper.selectByExample(example);
        PageInfo pageInfo=new PageInfo<>(indexConfigs);
        pageInfo.setPageSize(limit);
        pageInfo.setPageNum(page);
        return pageInfo;
    }

    @Override
    public String saveIndexConfig(TQingzhuIndexConfig indexConfig) {
        //todo 判断是否存在该商品
        if (indexConfigMapper.insertSelective(indexConfig) > 0) {
            return ServiceResultEnum.SUCCESS.getResult();
        }
        return ServiceResultEnum.DB_ERROR.getResult();
    }

    @Override
    public String updateIndexConfig(TQingzhuIndexConfig indexConfig) {
        //todo 判断是否存在该商品
        TQingzhuIndexConfig temp = indexConfigMapper.selectByPrimaryKey(indexConfig.getConfigId());
        if (temp == null) {
            return ServiceResultEnum.DATA_NOT_EXIST.getResult();
        }
        if (indexConfigMapper.updateByPrimaryKeySelective(indexConfig) > 0) {
            return ServiceResultEnum.SUCCESS.getResult();
        }
        return ServiceResultEnum.DB_ERROR.getResult();
    }

    @Override
    public TQingzhuIndexConfig getIndexConfigById(Long id) {
        return null;
    }

    @Override
    public List<IndexConfigGoodsVO> getConfigGoodsesForIndex(int configType, int number) {
        List<IndexConfigGoodsVO> IndexConfigGoodsVOS = new ArrayList<>(number);
        TQingzhuIndexConfigExample example =new TQingzhuIndexConfigExample();
        example.createCriteria().andConfigTypeEqualTo((byte) configType);
        PageHelper.startPage(0,number);
        List<TQingzhuIndexConfig> indexConfigs = indexConfigMapper.selectByExample(example);
        if (!CollectionUtils.isEmpty(indexConfigs)) {
            //取出所有的goodsId
            List<Long> goodsIds = indexConfigs.stream().map(TQingzhuIndexConfig::getGoodsId).collect(Collectors.toList());
            TQingzhuGoodsInfoExample example1=new TQingzhuGoodsInfoExample();
            example1.createCriteria().andGoodsIdIn(goodsIds);
            List<TQingzhuGoodsInfo> Goods = goodsMapper.selectByExample(example1);
            IndexConfigGoodsVOS = BeanUtil.copyList(Goods, IndexConfigGoodsVO.class);
            for (IndexConfigGoodsVO IndexConfigGoodsVO : IndexConfigGoodsVOS) {
                String goodsName = IndexConfigGoodsVO.getGoodsName();
                String goodsIntro = IndexConfigGoodsVO.getGoodsIntro();
                // 字符串过长导致文字超出的问题
                if (goodsName.length() > 30) {
                    goodsName = goodsName.substring(0, 30) + "...";
                    IndexConfigGoodsVO.setGoodsName(goodsName);
                }
                if (goodsIntro.length() > 22) {
                    goodsIntro = goodsIntro.substring(0, 22) + "...";
                    IndexConfigGoodsVO.setGoodsIntro(goodsIntro);
                }
            }
        }
        return IndexConfigGoodsVOS;
    }

    @Override
    public Boolean deleteBatch(Long[] ids) {
        if (ids.length < 1) {
            return false;
        }
        //删除数据
        TQingzhuIndexConfigExample example=new TQingzhuIndexConfigExample();
        example.createCriteria().andConfigIdIn(Arrays.asList(ids));
        return indexConfigMapper.deleteByExample(example) > 0;
    }
}
