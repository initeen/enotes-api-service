package com.enotes.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.enotes.dto.ActiveCategoryResponse;
import com.enotes.dto.CategoryDto;
import com.enotes.model.Category;
import com.enotes.repository.CategoryRepository;
import com.enotes.service.CategoryService;

@Service
public class CategoryServiceImpl implements CategoryService {

	@Autowired
	private ModelMapper mapper;

	@Autowired
	private CategoryRepository categoryRepository;

	@Override
	public Boolean saveCategory(CategoryDto categoryDto) {

		/*
		 * Category category = new Category(); category.setName(categoryDto.getName());
		 * category.setDescription(categoryDto.getDescription());
		 * category.setIsActive(categoryDto.getIsActive());
		 */

		Category category = mapper.map(categoryDto, Category.class);

		if(ObjectUtils.isEmpty(category.getId())) {
		
			category.setIsDeleted(false);
			category.setCreatedBy(1);
			category.setCreatedOn(new Date());
		}else {
			updateCategory(category);
		}
		
		
		
		Category savCategory = categoryRepository.save(category);

		if (ObjectUtils.isEmpty(savCategory)) {

			return false;
		}

		return true;
	}

	private void updateCategory(Category category) {
		
		Optional<Category> findById = categoryRepository.findById(category.getId());
		if(findById.isPresent()) {
			Category existCategory = findById.get();
			category.setCreatedBy(existCategory.getCreatedBy());
			category.setCreatedOn(existCategory.getCreatedOn());
			category.setIsDeleted(existCategory.getIsDeleted());
			
			category.setUpdatedBy(1);
			category.setUpdatedOn(new Date());
		}
		
	}

	@Override
	public List<CategoryDto> getAllCategory() {
		List<Category> categories = categoryRepository.findByIsDeletedFalse();
		List<CategoryDto> categoryDtoList = categories.stream().map(cat -> mapper.map(cat, CategoryDto.class)).toList();
		return categoryDtoList;
	}

	@Override
	public List<ActiveCategoryResponse> getActiveCategory() {
		List<Category> categories = categoryRepository.findByIsActiveTrueAndIsDeletedFalse();
		List<ActiveCategoryResponse> categoryList = categories.stream()
				.map(cat -> mapper.map(cat, ActiveCategoryResponse.class)).toList();
		return categoryList;
	}

	@Override
	public CategoryDto getCategoryById(Integer id) {
		Optional<Category> findByCategory = categoryRepository.findByIdAndIsDeletedFalse(id);
		
		if(findByCategory.isPresent()) {
			
			Category category = findByCategory.get();
			return mapper.map(category, CategoryDto.class);
		}
		return null;
	}

	@Override
	public Boolean deleteCategory(Integer id) {
		Optional<Category> findByCategory = categoryRepository.findById(id);
		
		if(findByCategory.isPresent()) {
			
			Category category = findByCategory.get();
			category.setIsDeleted(true);
			categoryRepository.save(category);
			return true;
		}
		return false;
	}
	
	

}
