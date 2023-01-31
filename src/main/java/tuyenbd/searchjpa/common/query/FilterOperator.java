package tuyenbd.searchjpa.common.query;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;

@Slf4j
public enum FilterOperator {

    EQUAL("=", false) {
        @Override
        public <Y extends Comparable<? super Y>> Predicate build(CriteriaBuilder cb, Predicate predicate,
                                                                 Expression<Y> key, Y value) {
            return cb.and(cb.equal(key, value), predicate);
        }
    },

    NOT_EQUAL("!=", false) {
        @Override
        public <Y extends Comparable<? super Y>> Predicate build(CriteriaBuilder cb, Predicate predicate,
                                                                 Expression<Y> key, Y value) {
            return cb.and(cb.notEqual(key, value), predicate);
        }
    },

    LIKE("=like=", false) {
        @Override
        public <Y extends Comparable<? super Y>> Predicate build(CriteriaBuilder cb, Predicate predicate,
                                                                 Expression<Y> key, Y value) {
            return cb.and(cb.like(cb.upper((Expression<String>) key), "%" + ((String) value).toUpperCase() + "%"), predicate);
        }
    },

    GREATER_THAN(">", false) {
        @Override
        public <Y extends Comparable<? super Y>> Predicate build(CriteriaBuilder cb, Predicate predicate,
                                                                 Expression<Y> key, Y value) {
            return cb.and(cb.greaterThan(key, value), predicate);
        }
    },

    GREATER_THAN_EQUAL(">=", false) {
        @Override
        public <Y extends Comparable<? super Y>> Predicate build(CriteriaBuilder cb, Predicate predicate,
                                                                 Expression<Y> key, Y value) {
            return cb.and(cb.greaterThanOrEqualTo(key, value), predicate);
        }
    },

    LESS_THAN("<", false) {
        @Override
        public <Y extends Comparable<? super Y>> Predicate build(CriteriaBuilder cb, Predicate predicate,
                                                                 Expression<Y> key, Y value) {
            return cb.and(cb.lessThan(key, value), predicate);
        }
    },

    LESS_THAN_EQUAL("<=", false) {
        @Override
        public <Y extends Comparable<? super Y>> Predicate build(CriteriaBuilder cb, Predicate predicate,
                                                                 Expression<Y> key, Y value) {
            return cb.and(cb.lessThanOrEqualTo(key, value), predicate);
        }
    },

    IN("~", true) {
        @Override
        public <Y extends Comparable<? super Y>> Predicate buildMultiValue(CriteriaBuilder cb, Predicate predicate,
                                                                           Expression<Y> key, List<Y> values) {
            CriteriaBuilder.In<Y> inClause = cb.in(key);
            values.forEach(inClause::value);
            return cb.and(inClause, predicate);
        }
    },

    NOT_IN("!~", true) {
        @Override
        public <Y extends Comparable<? super Y>> Predicate buildMultiValue(CriteriaBuilder cb, Predicate predicate,
                                                                           Expression<Y> key, List<Y> values) {
            CriteriaBuilder.In<Y> inClause = cb.in(key);
            values.forEach(inClause::value);
            return cb.and(inClause, predicate).not();
        }
    },

    BETWEEN("=bw=", true) {
        @Override
        public <Y extends Comparable<? super Y>> Predicate buildMultiValue(CriteriaBuilder cb, Predicate predicate,
                                                                           Expression<Y> key, List<Y> values) {
            var fromValue = values.get(0);
            var toValue = values.get(1);
            return cb.and(cb.and(cb.greaterThanOrEqualTo(key, fromValue), cb.lessThanOrEqualTo(key, toValue)), predicate);
        }
    };

    public String operator;
    public boolean isMultiValue;

    FilterOperator(String operator, boolean isMultiValue) {
        this.operator = operator;
        this.isMultiValue = isMultiValue;
    }

    public static FilterOperator from(String operator) {
        return Arrays.stream(values())
                .filter(filterOperator -> filterOperator.operator.equals(operator))
                .findFirst()
                .orElseThrow();
    }

    public <Y extends Comparable<? super Y>> Predicate build(CriteriaBuilder cb, Predicate predicate,
                                                             Expression<Y> key, Y value) {
        throw new RuntimeException("Not support build predicate");
    }

    public <Y extends Comparable<? super Y>> Predicate buildMultiValue(CriteriaBuilder cb, Predicate predicate,
                                                                       Expression<Y> key, List<Y> values) {
        throw new RuntimeException("Not support buildMultiValue predicate");
    }
}
