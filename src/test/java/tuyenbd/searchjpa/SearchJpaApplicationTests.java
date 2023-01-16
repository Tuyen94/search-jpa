package tuyenbd.searchjpa;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import tuyenbd.searchjpa.common.query.SearchRequest;
import tuyenbd.searchjpa.domain.product.repository.ProductRepository;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

//@SpringBootTest
class SearchJpaApplicationTests {

	@Autowired
	ProductRepository productRepository;

	@Test
	void contextLoads() {
		System.out.println(productRepository.findAll());
	}

	@Test
	void test() {
		String search = "a=3,a!=4,a=4";
		Pattern pattern = Pattern.compile("(\\w+?)(<|>|=|>=|<=|!=|~|!~|=like=)(\\w+?),");
		Matcher matcher = pattern.matcher(search + ",");
		System.out.println(matcher);
		while (matcher.find()) {
			System.out.println(matcher.group(1) + matcher.group(2) + matcher.group(3));
		}
		System.out.println(SearchRequest.buildFilters(search));
		String sort = "a=DESC,b=ASC";
		System.out.println(SearchRequest.buildSorts(sort));
	}

}
