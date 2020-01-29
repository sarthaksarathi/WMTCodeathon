package com.ms.wmadv.modelrecommend;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ModelRecommendController {

	@Autowired
	ModelRecommendService modelRecommendService;
	
	@ResponseBody
	@RequestMapping(path = "/products",method=RequestMethod.GET,produces="application/json")
	public Response getProducts(@RequestParam(value="query",required=false) String query) {
		if(StringUtils.isEmpty(query)) {
			return modelRecommendService.getAllProducts();
		}else {
			return modelRecommendService.fetchProductsBySymbol(query);
		}
	}
	
	@ResponseBody
	@RequestMapping(path = "/products/{productId}",method=RequestMethod.GET,produces="application/json")
	public Response getProductById(@PathVariable("productId") String productId) {
		return modelRecommendService.fetchProductsById(productId);
	}
	
	
	@ResponseBody
	@RequestMapping(path = "/products/recommendation/{riskScore}",method=RequestMethod.GET,produces="application/json")
	public Response getProductRecommendation(@PathVariable("riskScore") Double riskScore) {
		return modelRecommendService.fetchProductRecommendations(riskScore);
	}
	
	@ResponseBody
	@RequestMapping(path = "/model/riskscore",method=RequestMethod.POST,produces="application/json")
	public Response saveModelRisk(@RequestBody Model model) {
		return modelRecommendService.calculateRiskAndSaveModel(model);
	}
	
	@ResponseBody
	@RequestMapping(path = "/model",method=RequestMethod.POST,produces="application/json")
	public Response saveModel(@RequestBody Model model) {
		return modelRecommendService.saveModel(model);
	}

	@ResponseBody
	@RequestMapping(path = "/models",method=RequestMethod.GET,produces="application/json")
	public Response getModels(@RequestParam(value="query",required=false) String query) {
		if(StringUtils.isEmpty(query)) {
			return modelRecommendService.getAllModels();
		}else {
			return modelRecommendService.fetchModelsByName(query);
		}
	}
	
	@ResponseBody
	@RequestMapping(path = "/models/{modelId}",method=RequestMethod.GET,produces="application/json")
	public Response getModelById(@PathVariable("modelId") Integer modelId) {
		return modelRecommendService.fetchModelById(modelId);
	}
	
	@ResponseBody
	@RequestMapping(path = "/modelHoldings/{modelId}",method=RequestMethod.GET,produces="application/json")
	public Response getModelHoldingsById(@PathVariable("modelId") Integer modelId) {
		return modelRecommendService.fetchModelHoldingsById(modelId);
	}
	
}
