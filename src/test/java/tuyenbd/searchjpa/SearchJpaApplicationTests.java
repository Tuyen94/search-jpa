package tuyenbd.searchjpa;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import tuyenbd.searchjpa.common.query.SearchRequest;
import tuyenbd.searchjpa.domain.product.repository.ProductRepository;

import java.util.List;
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
		String search = "STRING:name=like=uyen,STRING:quantity>200+1,DATE:createTime>2023-01-13T13:52:30.374419";
		String[] filterArray = search.split(",");
		for (String filter : filterArray) {
			Pattern pattern = Pattern.compile("(\\w+?)(:)(\\w+?)(<|>|=|>=|<=|!=|~|!~|=like=)(.*)");
			Matcher matcher = pattern.matcher(filter);
			matcher.find();
			System.out.printf("%s %s %s %s%n", matcher.group(1), matcher.group(3), matcher.group(4), matcher.group(5));
		}

		System.out.println(SearchRequest.buildFilters(search));
		String sort = "a=DESC,b=ASC";
		System.out.println(SearchRequest.buildSorts(sort));
	}

}
