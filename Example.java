package com.sparta.mg.springmvcsql.controllers;

import com.sparta.mg.springmvcsql.entities.CustomersEntity;
import com.sparta.mg.springmvcsql.repositories.CustomersRepository;
import com.sparta.mg.springmvcsql.services.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class CustomerController {

	private final CustomersRepository customersRepository;
	private final CustomerService customerService = new CustomerService();

	@Autowired
	public CustomerController(CustomersRepository customersRepository) {
		this.customersRepository = customersRepository;
	}

	@GetMapping("/welcome")
	public String getWelcomePage() {
		return "index";
	}

	@GetMapping("/search-customer")
	public String searchForCustomer() {
		return "search-customer";
	}

	@GetMapping("/customers")
	public String getAllCustomers(Model model) {
		model.addAttribute("customers", customersRepository.findAll());
		return "customers";
	}

	@GetMapping("/add-customer")
	public String addCustomer(Model model) {
		CustomersEntity customersEntity = new CustomersEntity();
		model.addAttribute("customer", customersEntity);
		return "add-customer";
	}

	@PostMapping("/save-customer")
	public String saveCustomer(@ModelAttribute("customer") CustomersEntity customersEntity) {
		customersRepository.save(customersEntity);
		return "saved-customer";
	}

	@PostMapping("/search-results")
	public String getSearchResults(@ModelAttribute("customerName") String customerName, Model model) {
		List<CustomersEntity> foundCustomers = new ArrayList<>();
		for (CustomersEntity customerEntity : customersRepository.findAll()) {
			if (customerEntity.getContactName().contains(customerName)) {
				foundCustomers.add(customerEntity);
			}
		}
		model.addAttribute("searchResults", foundCustomers);
		return "search-results";
	}

	@GetMapping("/delete/{id}")
	public String deleteCustomer(@PathVariable("id") String id) {
		CustomersEntity customersEntity = customersRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid Customer ID" + id));
		customersRepository.delete(customersEntity);
		return "index";
	}

	@GetMapping("/edit/{id}")
	public String editCustomer(@PathVariable("id") String id, Model model) {
		CustomersEntity customersEntity = customersRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid Customer ID" + id));
		model.addAttribute("customer", customersEntity);
		return "edit-customer";
	}

	@PostMapping("/update-customer/{id}")
	public String updateCustomer(@ModelAttribute("customer") CustomersEntity updatedCustomer,
								 @PathVariable("id") String id) {
		CustomersEntity customer = customersRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invalid Customer ID" + updatedCustomer.getId()));
		customerService.update(updatedCustomer, customer);
		customersRepository.save(customer);

		return "index";
	}

	@GetMapping("/accessDenied")
	public String getAccessDeniedPage() {
		return "accessDenied";
	}

	@GetMapping("/login")
	public String getLoginPage() {
		return "login";
	}

}
