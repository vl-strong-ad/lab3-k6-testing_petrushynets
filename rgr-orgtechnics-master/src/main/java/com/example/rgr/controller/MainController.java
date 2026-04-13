package com.example.rgr.controller;

import com.example.rgr.entity.Product;
import com.example.rgr.entity.Shop;
import com.example.rgr.repository.ProductRepository;
import com.example.rgr.repository.ShopRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@Slf4j // Логування
public class MainController {

    private final ShopRepository shopRepository;
    private final ProductRepository productRepository;

    // --- Сторінка входу ---
    @GetMapping("/login")
    public String login() {
        return "login";
    }

    // --- Головна сторінка (список магазинів і товарів) ---
    @GetMapping({"/", "/shops"})
    public String getAllShops(Model model) {
        log.info("Користувач переглядає список магазинів");
        model.addAttribute("shops", shopRepository.findAll());
        return "shops";
    }

    // --- АДМІН: Видалення магазину ---
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/shops/delete/{id}")
    public String deleteShop(@PathVariable Long id) {
        log.info("Адміністратор видаляє магазин з ID: {}", id);
        shopRepository.deleteById(id);
        return "redirect:/shops";
    }

    // --- АДМІН: Видалення товару ---
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/products/delete/{id}")
    public String deleteProduct(@PathVariable Long id) {
        log.info("Адміністратор видаляє товар з ID: {}", id);
        productRepository.deleteById(id);
        return "redirect:/shops";
    }

    // --- АДМІН: Сторінка додавання товару ---
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/products/new")
    public String showCreateProductForm(@RequestParam Long shopId, Model model) {
        Product product = new Product();
        Shop shop = shopRepository.findById(shopId).orElseThrow();
        product.setShop(shop);

        model.addAttribute("product", product);
        model.addAttribute("shopId", shopId);
        return "create-product";
    }

    // --- АДМІН: Збереження нового товару ---
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/admin/products/save")
    public String saveProduct(@ModelAttribute Product product, @RequestParam Long shopId) {
        log.info("Адміністратор додає новий товар: {}", product.getName());
        Shop shop = shopRepository.findById(shopId).orElseThrow();
        product.setShop(shop);
        productRepository.save(product);
        return "redirect:/shops";
    }
}