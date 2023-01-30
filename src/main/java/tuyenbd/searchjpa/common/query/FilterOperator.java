package tuyenbd.searchjpa.common.query;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Slf4j
public enum FilterOperator {

    EQUAL("==") {
        public <T> Predicate build(Root<T> root, CriteriaBuilder cb, FilterRequest request, Predicate predicate) {
            Object value = request.getFieldType().parse(request.getValue());
            Expression<?> key = root.get(request.getKey());
            return cb.and(cb.equal(key, value), predicate);
        }
    },

    NOT_EQUAL("!=") {
        public <T> Predicate build(Root<T> root, CriteriaBuilder cb, FilterRequest request, Predicate predicate) {
            Object value = request.getFieldType().parse(request.getValue());
            Expression<?> key = root.get(request.getKey());
            return cb.and(cb.notEqual(key, value), predicate);
        }
    },

    LIKE("=like=") {
        public <T> Predicate build(Root<T> root, CriteriaBuilder cb, FilterRequest request, Predicate predicate) {
            Expression<String> key = root.get(request.getKey());
            return cb.and(cb.like(cb.upper(key), "%" + request.getValue().toUpperCase() + "%"), predicate);
        }
    },

    GREATER_THAN(">") {
        public <T> Predicate build(Root<T> root, CriteriaBuilder cb, FilterRequest request, Predicate predicate) {
            return cb.and(cb.greaterThan(root.get(request.getKey()), request.getValue()), predicate);
        }
    },

    GREATER_THAN_EQUAL(">=") {
        public <T> Predicate build(Root<T> root, CriteriaBuilder cb, FilterRequest request, Predicate predicate) {
            return cb.and(cb.greaterThanOrEqualTo(root.get(request.getKey()), request.getValue()), predicate);
        }
    },

    LESS_THAN("<") {
        public <T> Predicate build(Root<T> root, CriteriaBuilder cb, FilterRequest request, Predicate predicate) {
            return cb.and(cb.lessThan(root.get(request.getKey()), request.getValue()), predicate);
        }
    },

    LESS_THAN_EQUAL("<=") {
        public <T> Predicate build(Root<T> root, CriteriaBuilder cb, FilterRequest request, Predicate predicate) {
            return cb.and(cb.lessThanOrEqualTo(root.get(request.getKey()), request.getValue()), predicate);
        }
    },

    IN("~") {
        public <T> Predicate build(Root<T> root, CriteriaBuilder cb, FilterRequest request, Predicate predicate) {
            List<String> values = request.getValues();
            CriteriaBuilder.In<Object> inClause = cb.in(root.get(request.getKey()));
            for (String value : values) {
                inClause.value(request.getFieldType().parse(value));
            }
            return cb.and(inClause, predicate);
        }
    },

    NOT_IN("!~") {
        public <T> Predicate build(Root<T> root, CriteriaBuilder cb, FilterRequest request, Predicate predicate) {
            List<String> values = request.getValues();
            CriteriaBuilder.In<Object> inClause = cb.in(root.get(request.getKey()));
            for (String value : values) {
                inClause.value(request.getFieldType().parse(value));
            }
            return cb.and(inClause, predicate).not();
        }
    },

    BETWEEN("=bw=") {
        public <T> Predicate build(Root<T> root, CriteriaBuilder cb, FilterRequest request, Predicate predicate) {
            Object value = request.getFieldType().parse(request.getValues().get(0));
            Object valueTo = request.getFieldType().parse(request.getValues().get(1));

            switch (request.getFieldType()) {
                case DATE -> {
                    LocalDate startDate = (LocalDate) value;
                    LocalDate endDate = (LocalDate) valueTo;
                    Expression<LocalDate> key = root.get(request.getKey());
                    return cb.and(cb.and(cb.greaterThanOrEqualTo(key, startDate), cb.lessThanOrEqualTo(key, endDate)), predicate);
                }
                case DATETIME -> {
                    LocalDateTime startDate = (LocalDateTime) value;
                    LocalDateTime endDate = (LocalDateTime) valueTo;
                    Expression<LocalDateTime> key = root.get(request.getKey());
                    return cb.and(cb.and(cb.greaterThanOrEqualTo(key, startDate), cb.lessThanOrEqualTo(key, endDate)), predicate);
                }
                case LONG, DOUBLE, INTEGER -> {
                    Number start = (Number) value;
                    Number end = (Number) valueTo;
                    Expression<Number> key = root.get(request.getKey());
                    return cb.and(cb.and(cb.ge(key, start), cb.le(key, end)), predicate);
                }
                default -> throw new RuntimeException("Not support between for field type " + request.getFieldType());
            }
        }
    };

    public String operator;

    FilterOperator(String operator) {
        this.operator = operator;
    }

    public static FilterOperator from(String operator) {
        return Arrays.stream(values())
                .filter(filterOperator -> filterOperator.operator.equals(operator))
                .findFirst()
                .orElseThrow();
    }

    public abstract <T> Predicate build(Root<T> root, CriteriaBuilder cb, FilterRequest request, Predicate predicate);
}
