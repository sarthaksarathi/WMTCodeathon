package com.ms.wmadv.modelrecommend;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloWorldController {

	@Autowired
	DBConnection dbconnHelper;
	
	@GetMapping(path = "/hello-world")
	public String helloWorld() {
		return "Hello World";
	}

	@GetMapping(path = "/hello-world/get-model/{id}")
	public Response helloWorldGetModel(@PathVariable Integer id) {
		Response resp = new Response(null);
		try {
			Model model = dbconnHelper.getModel(id);
			resp.setSuccessMessage("Model retrieved successfully.");
			resp.setResponseObject(model);
		}
		catch(Exception e) {
			resp.setErrorMessage("Model retrieval failed: " + e.getMessage());
		}
		return resp;
	}
	
	@GetMapping(path = "/hello-world/get-model-holdings/{id}")
	public Response helloWorldGetModelHoldings(@PathVariable Integer id) {
		Response resp = new Response(null);
		try {
			Model model = dbconnHelper.getModelHoldings(id);
			resp.setSuccessMessage("Model holdings retrieved successfully.");
			resp.setResponseObject(model);
		}
		catch(Exception e) {
			resp.setErrorMessage("Model holdings retrieval failed: " + e.getMessage());
		}
		return resp;
	}
	
	@GetMapping(path = "/hello-world/get-products")
	public Response helloWorldGetProducts() {
		Response resp = new Response(null);
		try {
			List<Product> products = dbconnHelper.getProducts();
			if(products.isEmpty()) {
				resp.setSuccessMessage("No products found.");
			}
			else {
				resp.setSuccessMessage("Products retrieved successfully.");
			}
			resp.setResponseObject(products);
		}
		catch(Exception e) {
			resp.setErrorMessage("Products retrieval failed: " + e.getMessage());
		}
		return resp;
	}
	
	
}
