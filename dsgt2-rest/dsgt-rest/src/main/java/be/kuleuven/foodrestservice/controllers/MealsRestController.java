package be.kuleuven.foodrestservice.controllers;

import be.kuleuven.foodrestservice.domain.Meal;
import be.kuleuven.foodrestservice.domain.MealsRepository;
import be.kuleuven.foodrestservice.exceptions.MealNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
public class MealsRestController {

    private final MealsRepository mealsRepository;

    @Autowired
    MealsRestController(MealsRepository mealsRepository) {
        this.mealsRepository = mealsRepository;
    }

    @GetMapping("/rest/meals/{id}")
    EntityModel<Meal> getMealById(@PathVariable String id) {
        Meal meal = mealsRepository.findMeal(id).orElseThrow(() -> new MealNotFoundException(id));

        return mealToEntityModel(id, meal);
    }

    @GetMapping("/rest/meals")
    CollectionModel<EntityModel<Meal>> getMeals() {
        Collection<Meal> meals = mealsRepository.getAllMeal();

        List<EntityModel<Meal>> mealEntityModels = new ArrayList<>();
        for (Meal m : meals) {
            EntityModel<Meal> em = mealToEntityModel(m.getId(), m);
            mealEntityModels.add(em);
        }
        return CollectionModel.of(mealEntityModels,
                linkTo(methodOn(MealsRestController.class).getMeals()).withSelfRel());
    }

    @GetMapping("/rest/meals/cheapest")
    EntityModel<Meal> getCheapetMeal() {
        Meal meal = mealsRepository.getAllMeal().stream().min(Comparator.comparingDouble(Meal::getPrice)).get();
        return mealToEntityModel(meal.getId(), meal);
    }

    @GetMapping("/rest/meals/largest")
    EntityModel<Meal> getLargestMeal() {
        Meal meal = mealsRepository.getAllMeal().stream().max(Comparator.comparingDouble(Meal::getKcal)).get();
        return mealToEntityModel(meal.getId(), meal);
    }

    @PostMapping("/rest/meals/add")
    String addMeal(@RequestBody Meal newMeal) {
        if (mealsRepository.addMeal(newMeal))
            return "Meal added\n";
        return "Meal with this ID already exists.\n";
    }

    @PutMapping("/rest/meals/{id}/update")
    String updateMeal(@PathVariable String id, @RequestBody Meal updatedMeal) {
        return "Updated meal " + mealsRepository.updateMeal(id, updatedMeal).getId() + "\n";
    }

    @DeleteMapping("/rest/meals/{id}")
    String deleteMeal(@PathVariable String id) {
        return "Deleted meal " + mealsRepository.deleteMeal(id) + "\n";
    }

    @GetMapping("/rest/order/{address}")
    String orderMeals(@PathVariable String address, @RequestBody() String[] mealIDs) {
        StringBuilder order = new StringBuilder("Ordering to " + address + " meals:\n");
        for (String id : mealIDs)
            order.append(mealsRepository.findMeal(id).get().getName()).append('\n');
        return order.toString();
    }

    private EntityModel<Meal> mealToEntityModel(String id, Meal meal) {
        return EntityModel.of(meal,
                linkTo(methodOn(MealsRestController.class).getMealById(id)).withSelfRel(),
                linkTo(methodOn(MealsRestController.class).getMeals()).withRel("rest/meals"));
    }

}
