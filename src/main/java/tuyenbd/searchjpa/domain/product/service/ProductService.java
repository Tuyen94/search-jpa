package tuyenbd.searchjpa.domain.product.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import tuyenbd.searchjpa.common.query.SearchRequest;
import tuyenbd.searchjpa.common.query.SearchSpecification;
import tuyenbd.searchjpa.domain.product.entity.ProductEntity;
import tuyenbd.searchjpa.domain.product.repository.ProductRepository;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public Page<ProductEntity> find(String filter, String sort, int page, int size) {
        SearchRequest searchRequest = SearchRequest.from(filter, sort, page, size);
        SearchSpecification<ProductEntity> searchSpecification = new SearchSpecification<>(searchRequest);
        Pageable pageable = searchRequest.getPageable();
        return productRepository.findAll(searchSpecification, pageable);
    }
}
