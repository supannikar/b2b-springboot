package no.api.service;

import no.api.dao.BasketProductDao;
import no.api.model.BasketProductModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("basketProductService")
public class BasketProductService {

    @Autowired
    private BasketProductDao basketProductDao;

    public BasketProductModel save(BasketProductModel model){
        return basketProductDao.save(model);
    }
    public BasketProductModel findById(Long id){
        return basketProductDao.findById(id);
    }

    public BasketProductModel findProductByProductId(Long productId){
        return basketProductDao.findProductByProductId(productId);
    }

    public List<BasketProductModel> listAll(){
        return basketProductDao.listAll();
    }

    public List<BasketProductModel> listProductByBasketId(Long basketId){
        return basketProductDao.listProductByBasketId(basketId);
    }

//    public List<BasketProductModel> groupByName(Long id){
//        return basketProductDao.groupByName(id);
//    }
//
//    public List<CISGroupModel> queryCISGroup(CISGroupQuery cisGroupQuery){
//        return basketProductDao.queryCISGroup(cisGroupQuery);
//    }
//
//    public int countCISGroup(CISGroupQuery cisGroupQuery){
//        return basketProductDao.countCISGroup(cisGroupQuery);
//    }

    public void delete(Integer id){
        basketProductDao.delete(id);
    }
}
