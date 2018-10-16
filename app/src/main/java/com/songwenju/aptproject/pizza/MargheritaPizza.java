package com.songwenju.aptproject.pizza;

import com.example.Factory;

@Factory(type = Meal.class, id = "Margherita")
public class MargheritaPizza implements Meal{
    @Override
    public float getPrice() {
        return 6.0f;
    }
}




