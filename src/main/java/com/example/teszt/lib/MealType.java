package com.example.teszt.lib;

public enum MealType {
    FOOD {
        @Override
        public String toString() {
            return "Étel";
        }
    },
    BEVERAGE {
        @Override
        public String toString() {
            return "Ital";
        }
    },
    MENU {
        @Override
        public String toString() {
            return "Menü";
        }
    },
    DESSERT {
        @Override
        public String toString() {
            return "Desszert";
        }
    },
}
