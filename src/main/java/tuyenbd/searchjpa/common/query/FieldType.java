package tuyenbd.searchjpa.common.query;

import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
public enum FieldType {

    BOOLEAN {
        public Object parse(String value) {
            return Boolean.valueOf(value);
        }
    },

    CHAR {
        public Object parse(String value) {
            return value.charAt(0);
        }
    },

    DATE {
        public Object parse(String value) {
            return LocalDateTime.parse(value, DateTimeFormatter.ISO_LOCAL_DATE);
        }
    },

    DATETIME {
        public Object parse(String value) {
            return LocalDateTime.parse(value, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        }
    },

    DOUBLE {
        public Object parse(String value) {
            return Double.valueOf(value);
        }
    },

    INTEGER {
        public Object parse(String value) {
            return Integer.valueOf(value);
        }
    },

    LONG {
        public Object parse(String value) {
            return Long.valueOf(value);
        }
    },

    STRING {
        public Object parse(String value) {
            return value;
        }
    };

    public abstract Object parse(String value);

}
