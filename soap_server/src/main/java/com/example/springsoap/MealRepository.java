package com.example.springsoap;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.stream.Collectors;

import io.foodmenu.gt.webservice.*;


import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component
public class MealRepository {
    private static final Map<String, Meal> meals = new HashMap<String, Meal>();

    @PostConstruct
    public void initData() {

        Meal a = new Meal();
        a.setName("Steak");
        a.setDescription("Steak with fries");
        a.setMealtype(Mealtype.MEAT);
        a.setKcal(1100);
        a.setPrice(17.30);


        meals.put(a.getName(), a);

        Meal b = new Meal();
        b.setName("Portabello");
        b.setDescription("Portabello Mushroom Burger");
        b.setMealtype(Mealtype.VEGAN);
        b.setKcal(637);
        b.setPrice(10.30);



        meals.put(b.getName(), b);

        Meal c = new Meal();
        c.setName("Fish and Chips");
        c.setDescription("Fried fish with chips");
        c.setMealtype(Mealtype.FISH);
        c.setKcal(950);
        c.setPrice(15.50);


        meals.put(c.getName(), c);
    }

    public Meal findMeal(String name) {
        Assert.notNull(name, "The meal's code must not be null");
        return meals.get(name);
    }

    public Meal findBiggestMeal() {

        if (meals.size() == 0) return null;
        var values = meals.values();
        return values.stream().max(Comparator.comparing(Meal::getKcal)).orElseThrow(NoSuchElementException::new);

    }
    public Meal findCheapestMeal() {

        if (meals.size() == 0) return null;
        var values = meals.values();
        return values.stream().min(Comparator.comparing(Meal::getPrice)).orElseThrow(NoSuchElementException::new);

    }

    public ClientOrder registerClientOrders( List<String> orderNames,String clientName, String address, int amount){


        ClientOrder clientOrder = new ClientOrder();

        for (int i = 0; i < amount; i++)
            orderNames.forEach(n->clientOrder.getMeals().add(findMeal(n)));

        clientOrder.setName(clientName);
        clientOrder.setAddress(address);

        return clientOrder;
    }


}