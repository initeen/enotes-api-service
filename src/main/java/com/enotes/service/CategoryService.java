package com.enotes.service;

import java.util.List;

import com.enotes.dto.ActiveCategoryResponse;
import com.enotes.dto.CategoryDto;
import com.enotes.model.Category;

public interface CategoryService {

	public Boolean saveCategory(CategoryDto categoryDto);
	
	List<CategoryDto> getAllCategory();

	public List<ActiveCategoryResponse> getActiveCategory();

	public CategoryDto getCategoryById(Integer id);

	public Boolean deleteCategory(Integer id); 
}
