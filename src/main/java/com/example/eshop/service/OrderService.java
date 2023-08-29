package com.example.eshop.service;

import com.example.eshop.entity.ProductEntity;
import com.example.eshop.entity.UserEntity;
import com.example.eshop.entity.order.OrderDetailEntity;
import com.example.eshop.entity.order.OrderEntity;
import com.example.eshop.repository.OrderDetailRepository;
import com.example.eshop.repository.OrderRepository;
import com.example.eshop.repository.ProductRepository;
import com.example.eshop.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final OrderDetailRepository orderDetailRepository;

    public OrderService(OrderRepository orderRepository, ProductRepository productRepository, UserRepository userRepository, OrderDetailRepository orderDetailRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.orderDetailRepository = orderDetailRepository;
    }

    public void addProductToShoppingCart(HttpSession session, Long id) {
        if (session.getAttribute("products") == null) {
            List<Long> arr = new ArrayList<>();
            arr.add(id);
            session.setAttribute("products", arr);

        } else {
            List<Long> arr = (List<Long>) session.getAttribute("products");
            if (!arr.contains(id)) {
                arr.add(id);
            }
        }
    }


    public List<ProductEntity> getShoppingCartDetails(HttpSession session) {
        return productRepository.findAllById((List<Long>) session.getAttribute("products"));
    }


    public void createOrder(Map<String, String> allParams, HttpSession session) throws Exception {
        session.removeAttribute("products");
        session.removeAttribute("allParams");
        OrderEntity order = new OrderEntity();
        order.setOrderDetails(new ArrayList<>());
        orderRepository.save(order);

        for (String id : allParams.keySet()) {
            ProductEntity product = productRepository.findById(Long.parseLong(id)).orElseThrow(Exception::new);
            OrderDetailEntity orderDetailEntity = new OrderDetailEntity();
            orderDetailEntity.setOrder(order);
            orderDetailEntity.setProduct(product);
            orderDetailEntity.setQuantity(Integer.parseInt(allParams.get(id)));
            orderDetailRepository.save(orderDetailEntity);
            order.getOrderDetails().add(orderDetailEntity);
        }

        order.setPrice(calculateTotalPrice(allParams));
        order.setDate(LocalDate.now());
        order.setStatus("ACTIVE");
        order.setUser(getLoggedInUser());
        orderRepository.save(order);
    }


    public List<OrderEntity> getUserOrders() {
        if (getLoggedInUser().getRole().toString().equals("USER")) {
            return orderRepository.findAllByUser(getLoggedInUser());
        } else {
            return orderRepository.findAll();
        }

    }


    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }


    public void removeProductFromShoppingCart(HttpSession session, Long id) {
        List<Long> arr = (List<Long>) session.getAttribute("products");
        arr.remove(id);
        session.setAttribute("products", arr);
    }


    public int calculateTotalPrice(Map<String, String> products) {
        int totalPrice = 0;
        for (String id : products.keySet()) {
            totalPrice += (productRepository.findById(Long.parseLong(id)).get().getPrice()) * (Long.parseLong(products.get(id)));
        }
        return totalPrice;
    }

    public UserEntity getLoggedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {

            Object principal = authentication.getPrincipal();
            if (principal instanceof UserDetails) {
                String username = ((UserDetails) principal).getUsername();
                UserEntity user = userRepository.findByEmail(username).get();
                return user;
            }
        }
        return null;
    }
}
