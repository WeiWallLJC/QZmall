package new_qingzhu.demo.Controller.Admin;


import new_qingzhu.demo.Common.CategoryLevelEnum;
import new_qingzhu.demo.Common.Constants;
import new_qingzhu.demo.Common.ServiceResultEnum;
import new_qingzhu.demo.Pojo.TQingzhuGoodsCategory;
import new_qingzhu.demo.Pojo.TQingzhuGoodsInfo;
import new_qingzhu.demo.Service.CategoryService;
import new_qingzhu.demo.Service.GoodsService;
import new_qingzhu.demo.Util.Result;
import new_qingzhu.demo.Util.ResultGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;


@Controller
@RequestMapping("/admin")
public class GoodsController {

    private CategoryService CategoryService;
    private new_qingzhu.demo.Service.GoodsService GoodsService;
    @Autowired
    public GoodsController(CategoryService categoryService, GoodsService goodsService){
        this.CategoryService=categoryService;
        this.GoodsService=goodsService;
    }


    @GetMapping("/goods")
    public String goodsPage(HttpServletRequest request) {
        request.setAttribute("path", "newbee_mall_goods");
        return "admin/goods";
    }

    @GetMapping("/goods/edit")
    public String edit(HttpServletRequest request) {
        request.setAttribute("path", "edit");
        //查询所有的一级分类
        List<TQingzhuGoodsCategory> firstLevelCategories = CategoryService.selectByLevelAndParentIdsAndNumber(Collections.singletonList(0L), CategoryLevelEnum.LEVEL_ONE.getLevel());
        if (!CollectionUtils.isEmpty(firstLevelCategories)) {
            //查询一级分类列表中第一个实体的所有二级分类
            List<TQingzhuGoodsCategory> secondLevelCategories = CategoryService.selectByLevelAndParentIdsAndNumber(Collections.singletonList(firstLevelCategories.get(0).getCategoryId()), CategoryLevelEnum.LEVEL_TWO.getLevel());
            if (!CollectionUtils.isEmpty(secondLevelCategories)) {
                //查询二级分类列表中第一个实体的所有三级分类
                List<TQingzhuGoodsCategory> thirdLevelCategories = CategoryService.selectByLevelAndParentIdsAndNumber(Collections.singletonList(secondLevelCategories.get(0).getCategoryId()), CategoryLevelEnum.LEVEL_THREE.getLevel());
                request.setAttribute("firstLevelCategories", firstLevelCategories);
                request.setAttribute("secondLevelCategories", secondLevelCategories);
                request.setAttribute("thirdLevelCategories", thirdLevelCategories);
                request.setAttribute("path", "goods-edit");
                return "admin/goods_edit";
            }
        }
        return "error/error_5xx";
    }

    @GetMapping("/goods/edit/{goodsId}")
    public String edit(HttpServletRequest request, @PathVariable("goodsId") Long goodsId) {
        request.setAttribute("path", "edit");
        TQingzhuGoodsInfo Goods = GoodsService.getGoodsById(goodsId);
        if (Goods == null) {
            return "error/error_400";
        }
        if (Goods.getGoodsCategoryId() > 0) {
            if (Goods.getGoodsCategoryId() != null || Goods.getGoodsCategoryId() > 0) {
                //有分类字段则查询相关分类数据返回给前端以供分类的三级联动显示
                TQingzhuGoodsCategory currentGoodsCategory = CategoryService.getGoodsCategoryById(Goods.getGoodsCategoryId());
                //商品表中存储的分类id字段为三级分类的id，不为三级分类则是错误数据
                if (currentGoodsCategory != null && currentGoodsCategory.getCategoryLevel() == CategoryLevelEnum.LEVEL_THREE.getLevel()) {
                    //查询所有的一级分类
                    List<TQingzhuGoodsCategory> firstLevelCategories = CategoryService.selectByLevelAndParentIdsAndNumber(Collections.singletonList(0L), CategoryLevelEnum.LEVEL_ONE.getLevel());
                    //根据parentId查询当前parentId下所有的三级分类
                    List<TQingzhuGoodsCategory> thirdLevelCategories = CategoryService.selectByLevelAndParentIdsAndNumber(Collections.singletonList(currentGoodsCategory.getParentId()), CategoryLevelEnum.LEVEL_THREE.getLevel());
                    //查询当前三级分类的父级二级分类
                    TQingzhuGoodsCategory secondCategory = CategoryService.getGoodsCategoryById(currentGoodsCategory.getParentId());
                    if (secondCategory != null) {
                        //根据parentId查询当前parentId下所有的二级分类
                        List<TQingzhuGoodsCategory> secondLevelCategories = CategoryService.selectByLevelAndParentIdsAndNumber(Collections.singletonList(secondCategory.getParentId()), CategoryLevelEnum.LEVEL_TWO.getLevel());
                        //查询当前二级分类的父级一级分类
                        TQingzhuGoodsCategory firestCategory = CategoryService.getGoodsCategoryById(secondCategory.getParentId());
                        if (firestCategory != null) {
                            //所有分类数据都得到之后放到request对象中供前端读取
                            request.setAttribute("firstLevelCategories", firstLevelCategories);
                            request.setAttribute("secondLevelCategories", secondLevelCategories);
                            request.setAttribute("thirdLevelCategories", thirdLevelCategories);
                            request.setAttribute("firstLevelCategoryId", firestCategory.getCategoryId());
                            request.setAttribute("secondLevelCategoryId", secondCategory.getCategoryId());
                            request.setAttribute("thirdLevelCategoryId", currentGoodsCategory.getCategoryId());
                        }
                    }
                }
            }
        }
        if (Goods.getGoodsCategoryId() == 0) {
            //查询所有的一级分类
            List<TQingzhuGoodsCategory> firstLevelCategories = CategoryService.selectByLevelAndParentIdsAndNumber(Collections.singletonList(0L), CategoryLevelEnum.LEVEL_ONE.getLevel());
            if (!CollectionUtils.isEmpty(firstLevelCategories)) {
                //查询一级分类列表中第一个实体的所有二级分类
                List<TQingzhuGoodsCategory> secondLevelCategories = CategoryService.selectByLevelAndParentIdsAndNumber(Collections.singletonList(firstLevelCategories.get(0).getCategoryId()), CategoryLevelEnum.LEVEL_TWO.getLevel());
                if (!CollectionUtils.isEmpty(secondLevelCategories)) {
                    //查询二级分类列表中第一个实体的所有三级分类
                    List<TQingzhuGoodsCategory> thirdLevelCategories = CategoryService.selectByLevelAndParentIdsAndNumber(Collections.singletonList(secondLevelCategories.get(0).getCategoryId()), CategoryLevelEnum.LEVEL_THREE.getLevel());
                    request.setAttribute("firstLevelCategories", firstLevelCategories);
                    request.setAttribute("secondLevelCategories", secondLevelCategories);
                    request.setAttribute("thirdLevelCategories", thirdLevelCategories);
                }
            }
        }
        request.setAttribute("goods", Goods);
        request.setAttribute("path", "goods-edit");
        return "admin/goods_edit";
    }

    /**
     * 列表
     * 从地址栏获取到分页参数limit,page
     */
    @RequestMapping(value = "/goods/list", method = RequestMethod.GET)
    @ResponseBody
    public Result list(@RequestParam Map<String, Object> params) {
        if (StringUtils.isEmpty(params.get("page")) || StringUtils.isEmpty(params.get("limit"))) {
            return ResultGenerator.genFailResult("参数异常！");
        }

        return ResultGenerator.genSuccessResult(GoodsService.getGoodsPage(Integer.parseInt((String) params.get("page")),Integer.parseInt((String) params.get("limit"))));
    }

    /**
     * 添加
     */
    @RequestMapping(value = "/goods/save", method = RequestMethod.POST)
    @ResponseBody
    public Result save(@RequestBody TQingzhuGoodsInfo Goods) {
        if (StringUtils.isEmpty(Goods.getGoodsName())
                || StringUtils.isEmpty(Goods.getGoodsIntro())
                || StringUtils.isEmpty(Goods.getTag())
                || Objects.isNull(Goods.getOriginalPrice())
                || Objects.isNull(Goods.getGoodsCategoryId())
                || Objects.isNull(Goods.getSellingPrice())
                || Objects.isNull(Goods.getStockNum())
                || Objects.isNull(Goods.getGoodsSellStatus())
                || StringUtils.isEmpty(Goods.getGoodsCoverImg())
                || StringUtils.isEmpty(Goods.getGoodsDetailContent())) {
            return ResultGenerator.genFailResult("参数异常！");
        }
        String result = GoodsService.saveGoods(Goods);
        if (ServiceResultEnum.SUCCESS.getResult().equals(result)) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult(result);
        }
    }


    /**
     * 修改
     */
    @RequestMapping(value = "/goods/update", method = RequestMethod.POST)
    @ResponseBody
    public Result update(@RequestBody TQingzhuGoodsInfo Goods) {
        if (Objects.isNull(Goods.getGoodsId())
                || StringUtils.isEmpty(Goods.getGoodsName())
                || StringUtils.isEmpty(Goods.getGoodsIntro())
                || StringUtils.isEmpty(Goods.getTag())
                || Objects.isNull(Goods.getOriginalPrice())
                || Objects.isNull(Goods.getSellingPrice())
                || Objects.isNull(Goods.getGoodsCategoryId())
                || Objects.isNull(Goods.getStockNum())
                || Objects.isNull(Goods.getGoodsSellStatus())
                || StringUtils.isEmpty(Goods.getGoodsCoverImg())
                || StringUtils.isEmpty(Goods.getGoodsDetailContent())) {
            return ResultGenerator.genFailResult("参数异常！");
        }
        String result = GoodsService.updateGoods(Goods);
        if (ServiceResultEnum.SUCCESS.getResult().equals(result)) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult(result);
        }
    }

    /**
     * 详情
     */
    @GetMapping("/goods/info/{id}")
    @ResponseBody
    public Result info(@PathVariable("id") Long id) {
        TQingzhuGoodsInfo goods = GoodsService.getGoodsById(id);
        if (goods == null) {
            return ResultGenerator.genFailResult(ServiceResultEnum.DATA_NOT_EXIST.getResult());
        }
        return ResultGenerator.genSuccessResult(goods);
    }

    /**
     * 批量修改销售状态
     */
    @RequestMapping(value = "/goods/status/{sellStatus}", method = RequestMethod.PUT)
    @ResponseBody
    public Result delete(@RequestBody Long[] ids, @PathVariable("sellStatus") int sellStatus) {
        if (ids.length < 1) {
            return ResultGenerator.genFailResult("参数异常！");
        }
        if (sellStatus != Constants.SELL_STATUS_UP && sellStatus != Constants.SELL_STATUS_DOWN) {
            return ResultGenerator.genFailResult("状态异常！");
        }
        if (GoodsService.batchUpdateSellStatus(ids, sellStatus)) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult("修改失败");
        }
    }

}