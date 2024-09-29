package com.example.Models;

import java.util.List;

public class Weather {
    private Current current;
    private Forecast forecast;

    public Current getCurrent() {
        return current;
    }

    public void setCurrent(Current current) {
        this.current = current;
    }

    public Forecast getForecast() {
        return forecast;
    }

    public void setForecast(Forecast forecast) {
        this.forecast = forecast;
    }

    public static class Current {
        private Condition condition;
        private double temp_c;

        public Condition getCondition() {
            return condition;
        }

        public void setCondition(Condition condition) {
            this.condition = condition;
        }

        public double getTemp_c() {
            return temp_c;
        }

        public void setTemp_c(double temp_c) {
            this.temp_c = temp_c;
        }

        public static class Condition {
            private String icon;

            public String getIcon() {
                return icon;
            }

            public void setIcon(String icon) {
                this.icon = icon;
            }
        }
    }

    public static class Forecast {
        private List<Forecastday> forecastday;

        public List<Forecastday> getForecastday() {
            return forecastday;
        }

        public void setForecastday(List<Forecastday> forecastday) {
            this.forecastday = forecastday;
        }

        public static class Forecastday {
            private List<Hour> hour;
            private Day day;

            public List<Hour> getHour() {
                return hour;
            }

            public void setHour(List<Hour> hour) {
                this.hour = hour;
            }

            public Day getDay() {
                return day;
            }

            public void setDay(Day day) {
                this.day = day;
            }

            public static class Hour {
                private double temp_c;
                private String time;
                private Condition condition;

                public double getTemp_c() {
                    return temp_c;
                }

                public void setTemp_c(double temp_c) {
                    this.temp_c = temp_c;
                }

                public String getTime() {
                    return time;
                }

                public void setTime(String time) {
                    this.time = time;
                }

                public Condition getCondition() {
                    return condition;
                }

                public void setCondition(Condition condition) {
                    this.condition = condition;
                }
            }

            public class Condition {
                private String icon;

                public String getIcon() {
                    return icon;
                }
            }


            public static class Day {
                private double totalprecip_mm;

                public double getTotalprecip_mm() {
                    return totalprecip_mm;
                }

                public void setTotalprecip_mm(double totalprecip_mm) {
                    this.totalprecip_mm = totalprecip_mm;
                }
            }
        }
    }
}
