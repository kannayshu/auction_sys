package com.auction_sys.service.impl;


import com.auction_sys.common.ServerResponse;
import com.auction_sys.common.constant.CommonConst;
import com.auction_sys.common.constant.ProductConst;
import com.auction_sys.common.vaild.ParamVaild;
import com.auction_sys.common.vaild.Vaild;
import com.auction_sys.dao.ProductMapper;
import com.auction_sys.pojo.Product;
import com.auction_sys.pojo.ProductOrder;
import com.auction_sys.pojo.User;
import com.auction_sys.service.ProductService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.List;

/**
 * Created By liuda on 2018/5/30
 */
@Transactional
@Service()
public class ProductServiceImpl implements ProductService {

    private static final Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);

    ParamVaild paramVaild = ParamVaild.getInstance();
    @Autowired
    ProductMapper productMapper;

    final static BigDecimal ZERO = new BigDecimal(0);
    final static int THIRTY_MINUTE = 30*60*1000;
    public ServerResponse addProduct(Product product, User user){
        boolean bool = paramVaild.insertable(product);
        if (!bool)
            return ServerResponse.createByError(CommonConst.CommonResponStateConst.ILLEGAL_PARAMETETS);
        product.setSellerId(user.getUserId());
        product.setIsDeleted((byte)0);
        product.setCurrentPrice(product.getStartingPrice());
        product.setStatus(ProductConst.ProductStatusConst.DRAFT_STATUS);
        int id = 0;
        try{
            id = productMapper.insert(product);
        }catch (Exception e){
            e.printStackTrace();
            logger.debug(e.getMessage(),e);
            return ServerResponse.createByError(CommonConst.CommonResponStateConst.ILLEGAL_PARAMETETS);
        }
        if(id>0)
            return ServerResponse.createBySuccess(CommonConst.CommonResponStateConst.OPRATION_SUCCESS);
        return ServerResponse.createByError(CommonConst.CommonResponStateConst.UNKONW_ERROR);
    }



    public ServerResponse updateProduct(Product product, User user){
        boolean bool = ParamVaild.getInstance().updateable(product);
        if(!bool)
            return  ServerResponse.createByError(CommonConst.CommonResponStateConst.ILLEGAL_PARAMETETS);
        Product temp = productMapper.sellectSellerIdAndStatusByPrimaryKey(product.getProductId());
        //只有拍品处于两个状态其中一个才是可编辑更新状态
        byte vaildStatus = (ProductConst.ProductStatusConst.DRAFT_STATUS|ProductConst.ProductStatusConst.DEPOSIT_PAYED);
        if (temp.getSellerId()!=user.getUserId()||(temp.getStatus()|vaildStatus)!=vaildStatus)
            return  ServerResponse.createByError(CommonConst.CommonResponStateConst.ILLEGAL_OPERATION);
        //如果更新了起始价格 重新更新当前价格
        product.setCurrentPrice(product.getStartingPrice());
        int id = productMapper.updateByPrimaryKeySelective(product);
        if(id>0)
            return ServerResponse.createBySuccess(CommonConst.CommonResponStateConst.OPRATION_SUCCESS);
        return ServerResponse.createByError(CommonConst.CommonResponStateConst.UNKONW_ERROR);
    }

    public ServerResponse selectProduct(long pid){

        Product product = productMapper.selectByPrimaryKey(pid);
        if(product!=null)
            return ServerResponse.createBySuccess(CommonConst.CommonResponStateConst.OPRATION_SUCCESS,product);
        return ServerResponse.createByError(ProductConst.ProductResponStateConst.PRODUCT_INEXISTENCE);
    }

    public ServerResponse selectProductSample(long pid){

        Product product = productMapper.selectSimpleResultByPrimaryKey(pid);
        if(product!=null)
            return ServerResponse.createBySuccess(CommonConst.CommonResponStateConst.OPRATION_SUCCESS,product);
        return ServerResponse.createByError(ProductConst.ProductResponStateConst.PRODUCT_INEXISTENCE);
    }

    public ServerResponse selectProductList(int pageNum,int pageSize){
        PageHelper.startPage(pageNum,pageSize);
        List<Product> productList = productMapper.listResult();
        PageInfo pageInfo = new PageInfo(productList);
        if(productList!=null)
            return ServerResponse.createBySuccess(CommonConst.CommonResponStateConst.OPRATION_SUCCESS,pageInfo);
        return ServerResponse.createByError(CommonConst.CommonResponStateConst.UNKONW_ERROR);
    }

    public ServerResponse selectProductListByCategory(int pageNum,int pageSize,int categoryId){
        PageHelper.startPage(pageNum,pageSize);
        List<Product> productList = productMapper.listResultByCategory(categoryId);
        PageInfo pageInfo = new PageInfo(productList);
        if(productList!=null)
            return ServerResponse.createBySuccess(CommonConst.CommonResponStateConst.OPRATION_SUCCESS,pageInfo);
        return ServerResponse.createByError(CommonConst.CommonResponStateConst.UNKONW_ERROR);
    }

    public ServerResponse listResultBySellerId(int pageNum,int pageSize,Long sellerId){
        PageHelper.startPage(pageNum,pageSize);
        List<Product> productList = productMapper.listResultBySellerId(sellerId);
        PageInfo pageInfo = new PageInfo(productList);
        if(productList!=null)
            return ServerResponse.createBySuccess(CommonConst.CommonResponStateConst.OPRATION_SUCCESS,pageInfo);
        return ServerResponse.createByError(CommonConst.CommonResponStateConst.UNKONW_ERROR);
    }
    public ServerResponse searchProductSelective(String content,Integer categoryId,int pageNum,int pageSize){
        try {
            content = new String(("%"+content+"%").getBytes(),"utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return ServerResponse.createByError(CommonConst.CommonResponStateConst.UNKONW_ERROR);
        }
        Product product = new Product();
        product.setName(content);
        product.setSubTitle(content);
        if(categoryId!=-1)
            product.setCategoryId(categoryId);
        PageHelper.startPage(pageNum,pageSize);
        List<Product> productList = productMapper.searchSelective(product);
        PageInfo pageInfo = new PageInfo(productList);
        if(productList!=null)
            return ServerResponse.createBySuccess(CommonConst.CommonResponStateConst.OPRATION_SUCCESS,pageInfo);
        return ServerResponse.createByError(CommonConst.CommonResponStateConst.UNKONW_ERROR);
    }

    public ServerResponse putawayProduct(User user,Long productId){
        int num = productMapper.updateStatusByPrimaryKeyAndUserId(productId,user.getUserId(),ProductConst.ProductStatusConst.BIDDING
                , ProductConst.ProductStatusConst.DEPOSIT_PAYED);
        if (num>0)
            return ServerResponse.createBySuccess(CommonConst.CommonResponStateConst.OPRATION_SUCCESS);
        return ServerResponse.createByErrorIllegalOperation();
    }

    public boolean insertable(Product product){

        if(!statusIsVaild(product.getStatus()))
            return false;
        if (product.getGmtDeadline()==null&& product.getStatus()!=ProductConst.ProductStatusConst.DRAFT_STATUS)
            return false;

        return product.getCategoryId()!=null&&!(product.getCurrentPrice().compareTo(ZERO)<0)&&!(product.getReservePrice().compareTo(ZERO)<0)&&
                !(product.getScale().compareTo(ZERO)<0)&&!(product.getStartingPrice().compareTo(ZERO)<0)&& !StringUtils.isBlank(product.getDetail())&&
                !StringUtils.isBlank(product.getMainImage())&&!StringUtils.isBlank(product.getName())&&!StringUtils.isBlank(product.getSubImage())&&
                product.getCashDeposit()!=null&&!(product.getCashDeposit().compareTo(ZERO)<0);
    }


    public boolean statusIsVaild(byte status){
        if(status== ProductConst.ProductStatusConst.DRAFT_STATUS||status == ProductConst.ProductStatusConst.BIDDING)
            return true;
        return false;

    }
    public boolean updateable(Product product){

        if(product.getStatus()!=null&&!statusIsVaild(product.getStatus()))
            return false;
        if (product.getGmtDeadline()==null&& product.getStatus()!=ProductConst.ProductStatusConst.DRAFT_STATUS)
            return false;
        return false;
//        return product.getId()!=null&&product.getCurrentPrice()==null?true:!(product.getCurrentPrice().compareTo(ZERO)<0) &&
////                product.getReservePrice()==null?true:!(product.getReservePrice().compareTo(ZERO)<0)&&
////                product.getScale()==null?true: !(product.getScale().compareTo(ZERO)<0)&&
////                product.getCashDeposit()==null?true: !(product.getCashDeposit().compareTo(ZERO)<0)&&
////                product.getStartingPrice()==null?true:!(product.getStartingPrice().compareTo(ZERO)<0)&&
////                product.getDetail()==null?true:!StringUtils.isBlank(product.getDetail())&&
////                product.getMainImage()==null?true: !StringUtils.isBlank(product.getMainImage())&&
////                product.getName()==null?true:!StringUtils.isBlank(product.getName())&&
////                product.getSubImage()==null?true:!StringUtils.isBlank(product.getSubImage())&&
////                product.getSubTitle()==null?true: !StringUtils.isBlank(product.getSubTitle());
    }
}
