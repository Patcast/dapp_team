package be.kuleuven.foodrestservice.controllers;

import be.kuleuven.foodrestservice.domain.Meal;
import be.kuleuven.foodrestservice.domain.MealsRepository;
import be.kuleuven.foodrestservice.exceptions.MealNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Comparator;
import java.util.Optional;

import java.lang.Integer;

@RestController
public class MealsRestRpcStyleController {

    private final MealsRepository mealsRepository;

    @Autowired
    MealsRestRpcStyleController(MealsRepository mealsRepository) {
        this.mealsRepository = mealsRepository;
    }

    @GetMapping("/restrpc/meals/{id}")
    Meal getMealById(@PathVariable String id) {
        Optional<Meal> meal = mealsRepository.findMeal(id);

        return meal.orElseThrow(() -> new MealNotFoundException(id));
    }

    @GetMapping("/restrpc/meals")
    Collection<Meal> getMeals() {
        return mealsRepository.getAllMeal();
    }

    @GetMapping("/restrpc/meals/cheapest")
    Optional<Meal> getCheapestMeal() {
        return mealsRepository.getAllMeal().stream().min(Comparator.comparingDouble(Meal::getPrice));
    }

    @GetMapping("/restrpc/meals/largest")
    Optional<Meal> getLargestMeal() {
        return mealsRepository.getAllMeal().stream().max(Comparator.comparingInt(Meal::getKcal));
    }

    @PostMapping("/restrpc/meals/add")
    String addMeal(@RequestBody Meal newMeal) {
        if (mealsRepository.addMeal(newMeal))
            return "Meal added\n";
        return "Meal with this ID already exists.\n";
    }

    @PutMapping("/restrpc/meals/{id}/update")
    String updateMeal(@PathVariable String id, @RequestBody Meal updatedMeal) {
        return "Updated meal " + mealsRepository.updateMeal(id, updatedMeal).getId() + "\n";
    }

    @DeleteMapping("/restrpc/meals/{id}")
    String deleteMeal(@PathVariable String id) {
        return "Deleted meal " + mealsRepository.deleteMeal(id) + "\n";
    }

    @GetMapping("/restrpc/order/{address}")
    String orderMeals(@PathVariable String address, @RequestBody() String[] mealIDs) {
        StringBuilder order = new StringBuilder("Ordering to " + address + " meals:\n");
        for (String id : mealIDs)
            order.append(mealsRepository.findMeal(id).get().getName()).append('\n');
        return order.toString();
    }
}
