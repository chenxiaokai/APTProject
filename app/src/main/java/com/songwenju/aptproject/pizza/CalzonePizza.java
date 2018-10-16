package com.songwenju.aptproject.pizza;

import com.example.Factory;

@Factory(type = Meal.class, id = "Calzone")
public class CalzonePizza implements Meal{
    @Override
    public float getPrice() {
        return 8.5f;
    }
}
