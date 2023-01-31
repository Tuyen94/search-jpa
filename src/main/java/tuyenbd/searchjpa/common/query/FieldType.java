package tuyenbd.searchjpa.common.query;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.function.Function;

@Slf4j
public enum FieldType {

    BOOLEAN {
        @Override
        public <T> Predicate build(Root<T> root, CriteriaBuilder cb, FilterRequest request, Predicate predicate) {
            return build(root, cb, request, predicate, Boolean.class, Boolean::valueOf);
        }
    },

    CHAR {
        @Override
        public <T> Predicate build(Root<T> root, CriteriaBuilder cb, FilterRequest request, Predicate predicate) {
            return build(root, cb, request, predicate, Character.class, value -> value.charAt(0));
        }
    },

    DATE {
        @Override
        public <T> Predicate build(Root<T> root, CriteriaBuilder cb, FilterRequest request, Predicate predicate) {
            return build(root, cb, request, predicate, LocalDate.class,
                    value -> LocalDate.parse(request.getValue(), DateTimeFormatter.ISO_LOCAL_DATE));
        }
    },

    DATETIME {
        @Override
        public <T> Predicate build(Root<T> root, CriteriaBuilder cb, FilterRequest request, Predicate predicate) {
            return build(root, cb, request, predicate, LocalDateTime.class,
                    value -> LocalDateTime.parse(request.getValue(), DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        }
    },

    DOUBLE {
        @Override
        public <T> Predicate build(Root<T> root, CriteriaBuilder cb, FilterRequest request, Predicate predicate) {
            return build(root, cb, request, predicate, Double.class, Double::valueOf);
        }
    },

    INTEGER {
        @Override
        public <T> Predicate build(Root<T> root, CriteriaBuilder cb, FilterRequest request, Predicate predicate) {
            return build(root, cb, request, predicate, Integer.class, Integer::valueOf);
        }
    },

    LONG {
        @Override
        public <T> Predicate build(Root<T> root, CriteriaBuilder cb, FilterRequest request, Predicate predicate) {
            return build(root, cb, request, predicate, Long.class, Long::valueOf);
        }
    },

    STRING {
        @Override
        public <T> Predicate build(Root<T> root, CriteriaBuilder cb, FilterRequest request, Predicate predicate) {
            return build(root, cb, request, predicate, String.class, String::valueOf);
        }
    };

    public abstract <T> Predicate build(Root<T> root, CriteriaBuilder cb, FilterRequest request, Predicate predicate);

    public <T, Y extends Comparable<? super Y>> Predicate build(Root<T> root, CriteriaBuilder cb,
                                                                FilterRequest request, Predicate predicate,
                                                                Class<Y> fieldType,
                                                                Function<String, Y> mapper) {
        Expression<Y> key = root.get(request.getKey());
        if (request.getOperator().isMultiValue) {
            List<Y> values = request.getValues().stream().map(mapper).toList();
            return request.getOperator().buildMultiValue(cb, predicate, key, values);
        } else {
            Y value = mapper.apply(request.getValue());
            return request.getOperator().build(cb, predicate, key, value);
        }
    }

}
