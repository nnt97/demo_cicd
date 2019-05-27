package com.nnthai1997.cicd.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nnthai1997.cicd.entity.Repos;

@RestController
public class CicdController {
	
	@GetMapping("/")
	public ModelAndView showIndex() {
		return new ModelAndView("index.html");
	}
	
	@GetMapping("/home")
	public ModelAndView showHome(@ModelAttribute("listRepos") List<Repos> listRepos) {
		System.out.println(listRepos.size());
		ModelAndView mav = new ModelAndView("home.html");
		mav.getModel().put("listRepos", listRepos.get(0));
		mav.addObject("listRepos", listRepos.get(0));
		return mav;
	}
	
	@GetMapping("/callback/github")
	public ModelAndView show(@RequestParam(required = false) String code, RedirectAttributes redir) {
		if (code==null) return new ModelAndView("/");
		String accessToken = getAccessToken(code);
		List<Repos> listRepos = getListRepos(accessToken);
		ModelAndView mav = new ModelAndView();
		mav.setViewName("redirect:/home");
		redir.addFlashAttribute("listRepos",listRepos);
	    return mav;
	}

	@PostMapping("/githook")
	public void receiveHookGit() {
		System.out.println("111111111111111111");
		System.out.println("22222222222222222222222");
	}
	
	private List<Repos> getListRepos(String accessToken) {
		String url = "https://api.github.com/user/repos";
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.AUTHORIZATION, "Bearer "+accessToken);
		HttpEntity<?> entity = new HttpEntity<>(headers);
		ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
		String listRepos = response.getBody();
		TypeToken<List<Repos>> token = new TypeToken<List<Repos>>() {};
		Gson gson = new Gson();
		
		List<Repos> list = gson.fromJson(listRepos, token.getType());
		return list;
	}

	private String getAccessToken(String code) {
		String url = "https://github.com/login/oauth/access_token";
		Map<String, String> codeRequest = new HashMap<>();
		codeRequest.put("code", code);
		codeRequest.put("client_id", "5a1740d90e0de63582f7");
		codeRequest.put("client_secret", "5d8567855bd4119b2c2ffb2e3e3d6d45bcbdcfd8");
		RestTemplate restTemplate = new RestTemplate();
		HttpEntity<Map<String, String>> request = new HttpEntity<>(codeRequest);
		ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
		return response.getBody().substring(13, 53);
	}
}
