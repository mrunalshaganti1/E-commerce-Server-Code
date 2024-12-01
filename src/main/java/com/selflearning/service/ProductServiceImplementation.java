package com.selflearning.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.selflearning.exception.ProductException;
import com.selflearning.model.Category;
import com.selflearning.model.Product;
import com.selflearning.repository.CategoryRepository;
import com.selflearning.repository.ProductRepository;
import com.selflearning.request.CreateProductRequest;


@Service
public class ProductServiceImplementation implements ProductService{
	
	@Autowired
	private ProductRepository productRepository;
	@Autowired
	private UserService userService;
	@Autowired
	private CategoryRepository categoryRepository;

	@Override
	public Product createProduct(CreateProductRequest request) {
		// Find the top-level category
        Category topLevelCategory = categoryRepository.findByName(request.getTopLevelCategory());
        if (topLevelCategory == null) {
            topLevelCategory = new Category();
            topLevelCategory.setName(request.getTopLevelCategory());
            topLevelCategory.setLevel(1);
            topLevelCategory = categoryRepository.save(topLevelCategory);
        }

        // Find the second-level category
        Category secondLevelCategory = categoryRepository.findByNameAndParent(request.getSecondLevelCategory(), topLevelCategory.getName());
        if (secondLevelCategory == null) {
            secondLevelCategory = new Category();
            secondLevelCategory.setName(request.getSecondLevelCategory());
            secondLevelCategory.setLevel(2);
            secondLevelCategory.setParentCategory(topLevelCategory);
            secondLevelCategory = categoryRepository.save(secondLevelCategory);
        }

        // Find the third-level category
        Category thirdLevelCategory = categoryRepository.findByNameAndParent(request.getThirdLevelCategory(), secondLevelCategory.getName());
        if (thirdLevelCategory == null) {
            thirdLevelCategory = new Category();
            thirdLevelCategory.setName(request.getThirdLevelCategory());
            thirdLevelCategory.setLevel(3);
            thirdLevelCategory.setParentCategory(secondLevelCategory);
            thirdLevelCategory = categoryRepository.save(thirdLevelCategory);
        }

        // Create the product
        Product product = new Product();
        product.setTitle(request.getTitle());
        product.setBrand(request.getBrand());
        product.setColor(request.getColor());
        product.setDiscountedPrice(request.getDiscountedPrice());
        product.setDiscountPresent(request.getDiscountedPresent());
        product.setPrice(request.getPrice());
        product.setQuantity(request.getQuanity());
        product.setImageUrl(request.getImageUrl());
        product.setDescription(request.getDescription());
        product.setCategory(thirdLevelCategory); // Associate the category
        product.setCreatedAt(LocalDateTime.now());

        return productRepository.save(product);
	}

	@Override
	public String deleteProduct(Long productId) throws ProductException {
		// TODO Auto-generated method stub
		Product product = findProductById(productId);
		product.getSizes().clear();
		productRepository.delete(product);
		return "Product Deleted Successfully.";
	}

	@Override
	public Product updateProduct(Long productId, Product request) throws ProductException {
		// TODO Auto-generated method stub
		Product product = findProductById(productId);
		
		if(request.getQuantity() != 0) {
			product.setQuantity(request.getQuantity());
		}
		
		return productRepository.save(product);
	}

	@Override
	public Product findProductById(Long Id) throws ProductException {
		// TODO Auto-generated method stub
		Optional<Product> opt = productRepository.findById(Id);
		
		if(opt.isPresent()) {
			return opt.get();
		}
		throw new ProductException("Product Not Found with Id: "+Id);
	}

	@Override
	public List<Product> findProductByCategory(String category) {
		// TODO Auto-generated method stub
		
		return null;
	}

	@Override
	public Page<Product> getAllProducts(String category, List<String> colors, List<String> sizes, Integer minPrice,
			Integer maxPrice, Integer minDiscount, String sort, String stock, Integer pageNumber, Integer pageSize) {
		// TODO Auto-generated method stub
		
		Pageable pageable = PageRequest.of(pageNumber, pageSize);
		
		List<Product> products = productRepository.filterProducts(category, minPrice, maxPrice, minDiscount, sort);

		if(!colors.isEmpty()) {
			products = products.stream().filter(p -> colors.stream().anyMatch(c -> c.equalsIgnoreCase(p.getColor())))
					.collect(Collectors.toList()); 
		}
		
		if(stock != null) {
			if(stock.equals("In_Stock")) {
				products = products.stream().filter(p -> p.getQuantity() > 0).collect(Collectors.toList());
			}
			else if(stock.equals("Out_Of_Stock")) {
				products = products.stream().filter(p -> p.getQuantity() < 1).collect(Collectors.toList());
			}
		}
		
		int startIndex = (int) pageable.getOffset();
		int endIndex = Math.min(startIndex + pageable.getPageSize(), products.size());
		
		List<Product> pageContent = products.subList(startIndex, endIndex);
		
		Page<Product> filteredProducts = new PageImpl<>(pageContent, pageable, products.size());


		System.out.println("Category: " + category);
		System.out.println("Min Price: " + minPrice);
		System.out.println("Max Price: " + maxPrice);
		System.out.println("Min Discount: " + minDiscount);
		System.out.println("Color: " + colors);
		System.out.println("Stock: " + stock);
		System.out.println("Products List getting from filerProducts in getAllProducts method: "+ products.toString());
		
		return filteredProducts;
	}

//	@Override
//	public List<Product> searchProduct(String q) {
//		// TODO Auto-generated method stub
//		
//		return null;
//	}

}
