package tuyenbd.searchjpa.common.query;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class SearchRequest implements Serializable {

    private static final long serialVersionUID = 8514625832019794838L;

    private List<FilterRequest> filters;

    private List<SortRequest> sorts;

    private Integer page;

    private Integer size;

    public List<FilterRequest> getFilters() {
        if (Objects.isNull(this.filters)) return new ArrayList<>();
        return this.filters;
    }

    public List<SortRequest> getSorts() {
        if (Objects.isNull(this.sorts)) return new ArrayList<>();
        return this.sorts;
    }

    public static SearchRequest from(String filterString, String sortString, int page, int size) {
        List<FilterRequest> filters = buildFilters(filterString);
        List<SortRequest> sorts = buildSorts(sortString);
        return SearchRequest.builder()
                .filters(filters)
                .sorts(sorts)
                .page(page)
                .size(size)
                .build();
    }

    public static List<FilterRequest> buildFilters(String filterString) {
        String[] filterArray = filterString.split(",");
        List<FilterRequest> filters = new ArrayList<>();
        for (String filter : filterArray) {
            Pattern pattern = Pattern.compile("(\\w+?)(<|>|=|>=|<=|!=|~|!~|=like=)(.*)");
            Matcher matcher = pattern.matcher(filter);
            matcher.find();
            var filterRequest = FilterRequest.builder()
                    .key(matcher.group(1))
                    .operator(FilterOperator.from(matcher.group(2)))
                    .value(matcher.group(3))
                    .fieldType(FieldType.STRING)
                    .build();
            filters.add(filterRequest);
        }
        return filters;
    }

    public static List<SortRequest> buildSorts(String sortString) {
        Pattern pattern = Pattern.compile("(\\w+?)(=)(\\w+?),");
        Matcher matcher = pattern.matcher(sortString + ",");
        List<SortRequest> sorts = new ArrayList<>();
        while (matcher.find()) {
            var sort = SortRequest.builder()
                    .key(matcher.group(1))
                    .direction(SortDirection.valueOf(matcher.group(3)))
                    .build();
            sorts.add(sort);
        }
        return sorts;
    }

    public Pageable getPageable() {
        return PageRequest.of(Objects.requireNonNullElse(page, 0), Objects.requireNonNullElse(size, 100));
    }
}
