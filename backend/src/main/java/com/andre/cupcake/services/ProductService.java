package com.andre.cupcake.services;

import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.andre.cupcake.dto.CategoryDTO;
import com.andre.cupcake.dto.ProductDTO;
import com.andre.cupcake.dto.UriDTO;
import com.andre.cupcake.entities.Category;
import com.andre.cupcake.entities.Product;
import com.andre.cupcake.repositories.CategoryRepository;
import com.andre.cupcake.repositories.ProductRepository;
import com.andre.cupcake.services.exceptions.DatabaseException;
import com.andre.cupcake.services.exceptions.ResourceNotFoundException;

@Service
public class ProductService {
	
	@Autowired
	private ProductRepository repository;
	
	@Autowired
	private CategoryRepository categoryRepository;
	
	@Autowired
	private S3Service s3Service;
	
	@Transactional(readOnly = true)
	public Page<ProductDTO> findAllPaged(Long categoryId, String name, PageRequest pageRequest ){
		List<Category> categories = (categoryId == 0) ? null : Arrays.asList(categoryRepository.getOne(categoryId));
		Page<Product>  page = repository.find(categories, name.trim(), pageRequest);
		repository.findProductsWithCategories(page.getContent());
		return page.map(x-> new ProductDTO(x, x.getCategories()));
		
	}
	
	@Transactional(readOnly = true)
	public ProductDTO findById(Long id) {
		Optional<Product> obj = repository.findById(id);
		Product entity = obj.orElseThrow(()-> new ResourceNotFoundException("Entity not found"));
		
		return new ProductDTO(entity, entity.getCategories());
	}
	
	@Transactional
	public ProductDTO insert(ProductDTO dto) {
		Product entity = new Product();
		copyDtoToEntity(dto, entity);
		if (entity.getCategories().size() == 0) {
			Category cat = categoryRepository.getOne(1L);
			entity.getCategories().add(cat);
		}
		entity = repository.save(entity);
		return new ProductDTO(entity);
	}

	@Transactional
	public ProductDTO update(Long id, ProductDTO dto) {
		try {
			Product entity = repository.getOne(id);
			copyDtoToEntity(dto, entity);
			if (entity.getCategories().size() == 0) {
				Category cat = categoryRepository.getOne(1L);
				entity.getCategories().add(cat);
			}
			entity = repository.save(entity);
			return new ProductDTO(entity);
		}
		catch (EntityNotFoundException e) {
			throw new ResourceNotFoundException("Id Not Found "+ id);
		}

	}

	public void delete(Long id) {
		try {
			repository.deleteById(id);
		}
		catch (EmptyResultDataAccessException e) {
			throw new ResourceNotFoundException("Id Not Found "+ id);
		}
		catch (DataIntegrityViolationException e) {
			throw new DatabaseException("Integrity violation");
		}
	}
	
	private void copyDtoToEntity(ProductDTO dto, Product entity) {
		
		entity.setName(dto.getName());
		entity.setDescription(dto.getDescription());
		entity.setImgUrl(dto.getImgUrl());
		entity.setDate(dto.getDate());
		entity.setPrice(dto.getPrice());
		
		entity.getCategories().clear();
		for (CategoryDTO catDto: dto.getCategories()) {
			Category category = categoryRepository.getOne(catDto.getId());
			entity.getCategories().add(category);
		}
		
	}

	public UriDTO uploadFile(MultipartFile file) {
		URL url =s3Service.uploadFile(file);
		return new UriDTO(url.toString());
	}
}