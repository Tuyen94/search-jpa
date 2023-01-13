package tuyenbd.searchjpa.application.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import tuyenbd.searchjpa.domain.product.entity.ProductEntity;
import tuyenbd.searchjpa.domain.product.repository.ProductRepository;

import java.util.List;

@Controller
public class ProductController {

    @Autowired
    private ProductRepository repo;

    @RequestMapping(method = RequestMethod.GET, value = "/products")
    @ResponseBody
    public List<ProductEntity> search(@RequestParam(value = "search") String search) {

        return null;
    }
}