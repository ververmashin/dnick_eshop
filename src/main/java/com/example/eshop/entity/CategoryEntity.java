package com.example.eshop.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "category")
public class CategoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id", nullable = true)
    private CategoryEntity category;

    public CategoryEntity() {
    }


    public CategoryEntity(String name, CategoryEntity category) {
        this.name = name;
        this.category = category;
    }


    public CategoryEntity(String name) {
        this.name = name;
    }

    public CategoryEntity(Long id, String name, CategoryEntity category) {
        this.id = id;
        this.name = name;
        this.category = category;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CategoryEntity getCategory() {
        return category;
    }

    public void setCategory(CategoryEntity category) {
        this.category = category;
    }
}
