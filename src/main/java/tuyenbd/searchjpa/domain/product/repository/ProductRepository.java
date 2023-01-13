package tuyenbd.searchjpa.domain.product.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tuyenbd.searchjpa.domain.product.entity.ProductEntity;

public interface ProductRepository extends JpaRepository<ProductEntity, Long> {
}
