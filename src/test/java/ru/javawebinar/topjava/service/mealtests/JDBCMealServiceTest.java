package ru.javawebinar.topjava.service.mealtests;

import org.springframework.test.annotation.IfProfileValue;
import ru.javawebinar.topjava.Profiles;

@IfProfileValue(name = "spring.profiles.active", value = Profiles.JDBC)
public class JDBCMealServiceTest extends MealServiceTest {
}
