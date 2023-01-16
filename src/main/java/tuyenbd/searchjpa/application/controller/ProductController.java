package tuyenbd.searchjpa.application.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import tuyenbd.searchjpa.domain.product.entity.ProductEntity;
import tuyenbd.searchjpa.domain.product.repository.ProductRepository;
import tuyenbd.searchjpa.domain.product.service.ProductService;

@Controller
@RequiredArgsConstructor
public class ProductController {

    private final ProductRepository productRepository;
    private final ProductService productService;

    @RequestMapping(method = RequestMethod.GET, value = "/products")
    @ResponseBody
    public Page<ProductEntity> search(
            @RequestParam(value = "filter") String filter,
            @RequestParam(value = "sort") String sort,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "100") int size) {
        return productService.find(filter, sort, page, size);
    }
}