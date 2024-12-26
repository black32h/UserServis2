

package User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/users")
public class UserController {

    private final RestTemplate restTemplate; // Для взаємодії з сервісом-зберіганням

    private final String STORAGE_SERVICE_URL = "http://localhost:8085/storage/users"; // URL сервісу-зберігання

    @Autowired
    public UserController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * Створити нового користувача.
     * @param user Користувач, що створюється.
     * @return Створений користувач.
     */
    @PostMapping
    public User createUser(@RequestBody User user) {
        return restTemplate.postForObject(STORAGE_SERVICE_URL, user, User.class); // Відправка даних до сервісу-зберігання
    }


    @GetMapping("/{id}")
    public User getUser(@PathVariable Long id) {
        return restTemplate.getForObject(STORAGE_SERVICE_URL + "/" + id, User.class); // Отримання даних з сервісу-зберігання
    }

    /**
     * Оновити інформацію про користувача.
     * @param id ID користувача.
     * @param updatedUser Оновлені дані користувача.
     * @return Оновлений користувач.
     */
    @PutMapping("/{id}")
    public User updateUser(@PathVariable Long id, @RequestBody User updatedUser) {
        restTemplate.put(STORAGE_SERVICE_URL + "/" + id, updatedUser); // Оновлення даних у сервісі-зберіганні
        return updatedUser;
    }



    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable Long id) {
        restTemplate.delete(STORAGE_SERVICE_URL + "/" + id); // Видалення даних із сервісу-зберігання
        return "Користувача видалено.";
    }
}
