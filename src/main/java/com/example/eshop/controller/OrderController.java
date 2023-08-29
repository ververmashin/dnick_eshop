package com.example.eshop.controller;

import com.example.eshop.service.CategoryService;
import com.example.eshop.service.OrderService;
import com.example.eshop.service.ProductService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Controller
public class OrderController {

    private final ProductService productService;
    private final OrderService orderService;
    private final CategoryService categoryService;

    public OrderController(ProductService productService, OrderService orderService, CategoryService categoryService) {
        this.productService = productService;
        this.orderService = orderService;
        this.categoryService = categoryService;
    }

    @GetMapping("/order")
    public String getOrderPage(@RequestParam(name = "filter", required = false) String filter,
                               @RequestParam(defaultValue = "0") int page,
                               Model model,
                               HttpSession httpSession) {
        if (filter != null) {
            httpSession.setAttribute("filter", filter);
        }
        if (httpSession.getAttribute("filter") != null) {
            filter = (String) httpSession.getAttribute("filter");
            model.addAttribute("filter", filter);
        }
        model.addAttribute("products", productService.filterBy(filter, page, 3));
        model.addAttribute("categories", categoryService.getCategories());
        return "order";
    }

    @GetMapping("/filter/category/{id}")
    public String getOrderPageFiltered(@PathVariable(name = "id", required = false) Long id,
                                       @RequestParam(defaultValue = "0") int page,
                                       Model model) throws Exception {
        model.addAttribute("products", productService.filterByCategory(id, page, 3));
        model.addAttribute("categories", categoryService.getCategories());
        model.addAttribute("id", id);
        return "order";
    }

    @GetMapping("/shopping-cart")
    public String getShoppingCart(HttpSession httpSession, Model model) {
        try {
            model.addAttribute("products", orderService.getShoppingCartDetails(httpSession));
        } catch (Exception e) {

        }
        return "shopping_cart";
    }

    @GetMapping("/my-orders")
    public String getUserOrders(Model model) {
        model.addAttribute("orders", orderService.getUserOrders());
        return "user_orders";
    }

    @PostMapping("/add/to/shopping/cart")
    public String addProductToShoppingCart(@RequestParam(name = "id") Long id, HttpSession session) {
        orderService.addProductToShoppingCart(session, id);
        return "redirect:/order";
    }

    @GetMapping("/remove/from/shopping/cart/{id}")
    public String removeProductFromShoppingCart(@PathVariable(name = "id") Long id, HttpSession session) {
        orderService.removeProductFromShoppingCart(session, id);
        return "redirect:/shopping-cart";
    }

    @PostMapping("/payment")
    public String getPaymentPage(@RequestParam Map<String, String> allRequestParams, HttpSession session) {
        session.setAttribute("allParams",allRequestParams);
        return "payment";
    }

    @PostMapping("/order")
    public String createOrder(HttpSession session) throws Exception {
        orderService.createOrder((Map<String, String>) session.getAttribute("allParams"), session);
        return "payment_successful";
    }

    @PostMapping("/order/delete/{id}")
    public String deleteOrder(@PathVariable(name = "id") Long id) {
        orderService.deleteOrder(id);
        return "redirect:/my-orders";
    }
}
