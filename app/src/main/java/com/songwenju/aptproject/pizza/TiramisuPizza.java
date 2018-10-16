package com.songwenju.aptproject.pizza;
import com.example.Factory;

@Factory(type = Meal.class, id = "Tiramisu")
public class TiramisuPizza implements Meal{
    @Override
    public float getPrice() {
        return 4.5f;
    }
}
