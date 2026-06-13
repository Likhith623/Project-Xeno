package com.xenocrm.segment.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * SegmentQueryBuilder -- Safely builds parameterized SQL queries from JSON conditions.
 * Prevents SQL Injection by whitelisting fields and operators.
 */
public class SegmentQueryBuilder {

    private static final Set<String> ALLOWED_FIELDS = Set.of(
            "c.id", "c.email", "c.phone", "c.city", "c.state", "c.country", "c.gender",
            "cm.recency_days", "cm.frequency", "cm.monetary_total", "cm.rfm_score",
            "cm.total_orders_last_30d", "cm.avg_days_between_orders", "cm.clv_predicted"
    );

    private static final Set<String> ALLOWED_OPERATORS = Set.of(
            "=", "!=", ">", "<", ">=", "<=", "LIKE", "ILIKE"
    );

    public static class ParameterizedQuery {
        public String sql;
        public Object[] params;

        public ParameterizedQuery(String sql, Object[] params) {
            this.sql = sql;
            this.params = params;
        }
    }

    /**
     * Builds a safe parameterized query from filterJson.
     * Expects filterJson to contain a list of conditions under the key "conditions".
     */
    public static ParameterizedQuery buildQuery(Map<String, Object> filterJson) {
        if (filterJson == null || !filterJson.containsKey("conditions")) {
            // Fallback to select all valid customers
            return new ParameterizedQuery("SELECT c.id, c.email FROM customers c WHERE c.email IS NOT NULL AND c.is_globally_opted_out = false", new Object[0]);
        }

        List<Map<String, Object>> conditions;
        try {
            ObjectMapper mapper = new ObjectMapper();
            conditions = mapper.convertValue(filterJson.get("conditions"), new TypeReference<List<Map<String, Object>>>() {});
        } catch (Exception e) {
            return new ParameterizedQuery("SELECT c.id, c.email FROM customers c WHERE c.email IS NOT NULL AND c.is_globally_opted_out = false", new Object[0]);
        }

        if (conditions.isEmpty()) {
            return new ParameterizedQuery("SELECT c.id, c.email FROM customers c WHERE c.email IS NOT NULL AND c.is_globally_opted_out = false", new Object[0]);
        }

        StringBuilder sql = new StringBuilder("SELECT c.id, c.email FROM customers c LEFT JOIN customer_metrics cm ON c.id = cm.customer_id WHERE c.email IS NOT NULL AND c.is_globally_opted_out = false");
        List<Object> params = new ArrayList<>();

        for (Map<String, Object> condition : conditions) {
            String field = (String) condition.get("field");
            String operator = (String) condition.get("operator");
            Object value = condition.get("value");

            if (field == null || operator == null || value == null) {
                continue;
            }

            // Convert camelCase from LLM to snake_case if necessary
            String dbField = field.toLowerCase().replaceAll("([a-z])([A-Z]+)", "$1_$2");
            // Prefix with c. or cm. if not already provided
            if (!dbField.contains(".")) {
                if (dbField.equals("recency_days") || dbField.equals("frequency") || dbField.equals("monetary_total") || dbField.equals("rfm_score") || dbField.equals("total_orders_last_30d")) {
                    dbField = "cm." + dbField;
                } else {
                    dbField = "c." + dbField;
                }
            }

            // Validations
            if (!ALLOWED_FIELDS.contains(dbField)) {
                continue; // Ignore unsafe/unknown fields
            }

            String upperOp = operator.toUpperCase().trim();
            // Map natural language operators that Gemini might produce
            switch (upperOp) {
                case "GT": upperOp = ">"; break;
                case "LT": upperOp = "<"; break;
                case "EQ": upperOp = "="; break;
                case "GTE": upperOp = ">="; break;
                case "LTE": upperOp = "<="; break;
                case "NEQ": upperOp = "!="; break;
            }
            
            if (!ALLOWED_OPERATORS.contains(upperOp)) {
                continue; // Ignore unsafe operators
            }

            sql.append(" AND ").append(dbField).append(" ").append(upperOp).append(" ?");
            params.add(value);
        }

        return new ParameterizedQuery(sql.toString(), params.toArray());
    }
}
