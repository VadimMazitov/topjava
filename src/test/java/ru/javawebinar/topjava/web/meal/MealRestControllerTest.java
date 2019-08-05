package ru.javawebinar.topjava.web.meal;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.web.AbstractControllerTest;
import ru.javawebinar.topjava.web.json.JsonUtil;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.TestUtil.readFromJson;
import static ru.javawebinar.topjava.UserTestData.USER_ID;


public class MealRestControllerTest extends AbstractControllerTest {

    private static final String REST_URL = MealRestController.REST_URL + '/';

    @Test
    void testGetAll() throws Exception {
        mockMvc.perform(get(REST_URL))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(contentJson(MEAL_TO6, MEAL_TO5, MEAL_TO4, MEAL_TO3, MEAL_TO2, MEAL_TO1));

    }

    @Test
    void testDelete() throws Exception {
        mockMvc.perform(delete(REST_URL + MEAL1_ID))
                .andDo(print())
                .andExpect(status().isNoContent());
        assertMatch(mealService.getAll(USER_ID), MEAL6, MEAL5, MEAL4, MEAL3, MEAL2);
    }

    @Test
    void testUpdate() throws Exception {
        Meal updated = new Meal(MEAL2);
        updated.setDescription("updated");
        updated.setCalories(777);
        int updatedId = MEAL1_ID + 1;
        mockMvc.perform(put(REST_URL + updatedId)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(JsonUtil.writeValue(updated)))
                .andExpect(status().isNoContent());
        assertMatch(mealService.getAll(USER_ID), MEAL6, MEAL5, MEAL4, MEAL3, updated, MEAL1);
    }

    @Test
    void testCreate() throws Exception {
        Meal expected = new Meal(null, LocalDateTime.now(), "created", 777);
        ResultActions result = mockMvc.perform(post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(expected)))
                .andDo(print())
                .andExpect(status().isCreated());
        System.out.println("-----------------------------");
        System.out.println(result);
        Meal returned = readFromJson(result, Meal.class);
        expected.setId(returned.getId());
        assertMatch(returned, expected);
        assertMatch(mealService.getAll(USER_ID), returned, MEAL6, MEAL5, MEAL4, MEAL3, MEAL2, MEAL1);
    }

    @Test
    void getAllBy() throws Exception {
        mockMvc.perform(get(REST_URL + "/by")
        .param("startDate", "2015-05-30T10:15:30")
        .param("endDate", "2015-05-30T10:15:30")
        .param("startTime", "2015-05-30T09:15:30")
        .param("endTime", "2015-05-30T14:15:30"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(contentJson(MEAL_TO2, MEAL_TO1));
    }

}
