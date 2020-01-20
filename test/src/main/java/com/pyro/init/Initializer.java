package com.pyro.init;

import com.pyro.entities.Category;
import com.pyro.entities.Product;
import com.pyro.entities.Role;
import com.pyro.entities.User;
import com.pyro.repositories.CatRepository;
import com.pyro.repositories.ProductRepository;
import com.pyro.repositories.RoleRepository;
import com.pyro.service.DBFileStorageService;
import com.pyro.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

//@RequiredArgsConstructor
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class Initializer implements ApplicationListener<ApplicationReadyEvent> {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private CatRepository catRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private DBFileStorageService imageService;

    @Transactional
    public void onApplicationEvent(ApplicationReadyEvent event) {

        if (roleRepository.findAll().size() == 0) {
            System.out.println("\n______________INITIALIZATION______________");

            //roles
            Role role = new Role();
            role.setName("ROLE_ADMIN");
            roleRepository.saveAndFlush(role);
            role = new Role();
            role.setName("ROLE_USER");
            roleRepository.saveAndFlush(role);

            //users
            User user = new User();
            user.setUsername("admin");
            user.setPassword("1234");
            user.setRoles(Arrays.asList(roleRepository.findByName("ROLE_ADMIN")));
            userService.save(user);
            user = new User();
            user.setUsername("user");
            user.setPassword("1234");
            userService.save(user);

            //Categories
            Category category = new Category();
            category.setName("Памятник");
            catRepository.saveAndFlush(category);
            category = new Category();
            category.setName("Гроб");
            catRepository.saveAndFlush(category);
            category = new Category();
            category.setName("Венок");
            catRepository.saveAndFlush(category);
            category = new Category();
            category.setName("Ограда");
            catRepository.saveAndFlush(category);
            category = new Category();
            category.setName("Сервис");
            catRepository.saveAndFlush(category);

            //Images
            String coffin = loadImage("D:\\workspace\\src\\main\\resources\\static\\images\\coffin.jpg");
            String service = loadImage("D:\\workspace\\src\\main\\resources\\static\\images\\service.jpg");
            String grave = loadImage("D:\\workspace\\src\\main\\resources\\static\\images\\grave.jpg");
            String fence = loadImage("D:\\workspace\\src\\main\\resources\\static\\images\\fence.jpg");
            String gravestone = loadImage("D:\\workspace\\src\\main\\resources\\static\\images\\gravestone.jpg");
            String wreath = loadImage("D:\\workspace\\src\\main\\resources\\static\\images\\wreath.jpg");
            String haron = loadImage("D:\\workspace\\src\\main\\resources\\static\\images\\haron.jpg");
            //Products
            Product product = new Product("Гроб 1", 200.00,
                    "Обычныйы пафосный гроб с остатками дневавшго там вампира.", coffin,
                    catRepository.findByName("Гроб"));
            productRepository.saveAndFlush(product);

            product = new Product("Похороны", 1400.00,
                    "Комплекс мероприятий и товаров для похорон. Включает гроб, копку могилы.", service,
                    catRepository.findByName("Сервис"));
            productRepository.saveAndFlush(product);

            product = new Product("Копка могил", 900.00,
                    "Раскопать могилу на кладбище", grave,
                    catRepository.findByName("Сервис"));
            productRepository.saveAndFlush(product);

            product = new Product("Катафалк", 1000.00,
                    "Сам Харон переправит ваших мертвых через Стикс. Оплата по километражу.", haron,
                    catRepository.findByName("Сервис"));
            productRepository.saveAndFlush(product);

            product = new Product("Носильщики", 300.00,
                    "Перенос тяжестей.", "service.jpg",
                    catRepository.findByName("Сервис"));
            productRepository.saveAndFlush(product);

            product = new Product("Забор 1", 90.00,
                    "Кованая ограда цена за метр погонный.", fence,
                    catRepository.findByName("Ограда"));
            productRepository.saveAndFlush(product);

            product = new Product("Надгробие 1", 90.00,
                    "Гранитная плита 80Х40.", gravestone,
                    catRepository.findByName("Памятник"));
            productRepository.saveAndFlush(product);

            product = new Product("Венок 1", 45.00,
                    "Венок траурный, несъедобный.", wreath,
                    catRepository.findByName("Венок"));
            productRepository.saveAndFlush(product);
        }
    }

    public  String loadImage(String pathName) {
        Path path = Paths.get(pathName);
        byte[] content = null;
        try {
            content = Files.readAllBytes(path);
        } catch (final IOException e) {
            System.out.println("\n_____cannot read bytes_____!");
        }

        MultipartFile result = new MockMultipartFile(path.getFileName().toString(),
                path.getFileName().toString(), "image/jpeg", content);
        try {
            return String.valueOf(imageService.storeFile(result).getId());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
