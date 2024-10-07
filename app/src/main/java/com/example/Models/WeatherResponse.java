package com.example.Models;

public class WeatherResponse {
    private Current current;

    public Current getCurrent() {
        return current;
    }

    public class Current {
        private float temp_c;
        private Condition condition;

        public float getTempC() {
            return temp_c;
        }

        public Condition getCondition() {
            return condition;
        }

        public class Condition {
            private String text;
            private String icon;

            public String getText() {
                return text;
            }

            public String getIcon() {
                return icon;
            }
        }
    }
}

