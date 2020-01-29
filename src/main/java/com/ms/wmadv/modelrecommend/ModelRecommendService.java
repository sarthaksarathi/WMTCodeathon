package com.ms.wmadv.modelrecommend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("modelRecommendService")
public class ModelRecommendService {

	@Autowired
	ModelRecommendRepository modelRecommendRepository;
	
	public Response getAllProducts() {
		Response resp = null;
		try {
			resp = new Response("Success", modelRecommendRepository.fetchAllProducts());
		}
		catch(Exception e) {
			resp = new Response("Error: " + e.getMessage());
		}
		return resp;
	}
	
	public Response fetchProductsById(String id) {
		Response resp = null;
		try {
			resp = new Response("Success", modelRecommendRepository.fetchProductById(id));
		}
		catch(Exception e) {
			resp = new Response("Error: " + e.getMessage());
		}
		return resp;
	}
	
	public Response fetchProductsBySymbol(String symbol) {
		Response resp = null;
		try {
			resp = new Response("Success", modelRecommendRepository.fetchProductsBySymbol(symbol));
		}
		catch(Exception e) {
			resp = new Response("Error: " + e.getMessage());
		}
		return resp;
	}

	public Response fetchProductRecommendations(Double riskScore) {
		Response resp = null;
		try {
			resp = new Response("Success", modelRecommendRepository.fetchProductRecommendations(riskScore));
		}
		catch(Exception e) {
			resp = new Response("Error: " + e.getMessage());
		}
		return resp;
	}

	public Response calculateRiskAndSaveModel(Model model) {
		Response resp = null;
		Integer mImp = null;
		Integer vImp = null;
		Integer rTol = null;
		if((mImp=model.getInv_obj_most()) == null
				|| (vImp=model.getInv_obj_imp()) == null
				|| (rTol=model.getRisk_tolerance()) == null) {
			System.out.println("Insufficient information for risk calculation: " + mImp + " : " + vImp + " : " + rTol + " : ");
			resp = new Response("Error: Insufficient information for risk calculation");
		}
		else {
			try {
				Integer riskProf = (mImp * 5 / 4) + (vImp * 1 / 4) + (rTol * 2 / 3);
				model.setRisk_profile(riskProf);
				modelRecommendRepository.saveModel(model);
				resp = new Response("Success", model);
			}
			catch(Exception e) {
				resp = new Response("Error: " + e.getMessage());
			}
		}
		return resp;
	}

	public Response saveModel(Model model) {
		Response resp = null;
		try {
			modelRecommendRepository.saveModel(model);
			resp = new Response("Success", model);
		}
		catch(Exception e) {
			resp = new Response("Error: " + e.getMessage());
		}
		return resp;
	}
	
	public Response getAllModels() {
		Response resp = null;
		try {
			resp = new Response("Success", modelRecommendRepository.fetchAllModels());
		}
		catch(Exception e) {
			resp = new Response("Error: " + e.getMessage());
		}
		return resp;
	}

	public Response fetchModelsByName(String query) {
		Response resp = null;
		try {
			resp = new Response("Success", modelRecommendRepository.fetchModelsByName(query));
		}
		catch(Exception e) {
			resp = new Response("Error: " + e.getMessage());
		}
		return resp;
	}

	public Response fetchModelById(Integer modelId) {
		Response resp = null;
		try {
			resp = new Response("Success", modelRecommendRepository.getModel(modelId));
		}
		catch(Exception e) {
			resp = new Response("Error: " + e.getMessage());
		}
		return resp;
	}

	public Response fetchModelHoldingsById(Integer modelId) {
		Response resp = null;
		try {
			resp = new Response("Success", modelRecommendRepository.getModelHoldings(modelId));
		}
		catch(Exception e) {
			resp = new Response("Error: " + e.getMessage());
		}
		return resp;
	}

}
