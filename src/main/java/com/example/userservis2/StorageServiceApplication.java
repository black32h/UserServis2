package com.example.userservis2;


import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;
import java.io.*;
import java.util.*;

@SpringBootApplication
public class StorageServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(StorageServiceApplication.class, args);
    }
}

@RestController
@RequestMapping("/storage/users")
class StorageController {

    private final String FILE_PATH = "\"C:\\Users\\Админ\\Desktop\\Новий Текстовий документ.txt\""; // Файл для збереження даних
    private Map<Long, User> users = new HashMap<>(); // Локальна пам'ять

    // Завантаження даних із файлу при старті
    @PostConstruct
    public void init() {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                User user = new User(Long.parseLong(parts[0]), parts[1], parts[2]);
                users.put(user.getId(), user);
            }
        } catch (IOException e) {
            System.out.println("Файл не знайдено, починаємо з порожньої бази.");
        }
    }

    // Збереження даних у файл
    private void saveToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (User user : users.values()) {
                writer.write(user.getId() + "," + user.getName() + "," + user.getEmail());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Створити нового користувача
    @PostMapping
    public User createUser(@RequestBody User user) {
        user.setId(users.size() + 1L);
        users.put(user.getId(), user);
        saveToFile();
        return user;
    }

    // Отримати інформацію про користувача за ID
    @GetMapping("/{id}")
    public User getUser(@PathVariable Long id) {
        return users.get(id);
    }

    // Оновити інформацію про користувача
    @PutMapping("/{id}")
    public User updateUser(@PathVariable Long id, @RequestBody User updatedUser) {
        if (users.containsKey(id)) {
            updatedUser.setId(id);
            users.put(id, updatedUser);
            saveToFile();
            return updatedUser;
        } else {
            return null;
        }
    }

    // Видалити користувача
    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable Long id) {
        if (users.remove(id) != null) {
            saveToFile();
            return "Користувача видалено.";
        } else {
            return "Користувача з таким ID не знайдено.";
        }
    }
}
