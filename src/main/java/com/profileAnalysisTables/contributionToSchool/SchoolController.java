package com.profileAnalysisTables.contributionToSchool;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.profileAnalysisTables.exception.SessionExpireException;
import com.spring.spring_security_learn.model.ApplicationUser;
@RestController
@RequestMapping("/SC")
@CrossOrigin("*")
public class SchoolController {
	@Autowired
    private ServiceContribution service;

    @GetMapping("/user/contributions/All")
    public List<ContributionToSchoolTable> getAllContributions() {
        return service.getAllContributions();
    }

    @GetMapping("/user/contributions/{indx}")
    public ContributionToSchoolTable getContributionByIdJWt(@PathVariable Long indx) {
    	String email=getEmailByJWT();
        return service.getContributionById(indx);
    }

    @GetMapping("/user/contributions/user/{userId}")
    public List<ContributionToSchoolTable> getContributionsByUserIdByJWT(@PathVariable Long userId) {
    	String email=getEmailByJWT();
        List<ContributionToSchoolTable> values=service.getContributionsByUserId(userId);
        if(values.isEmpty())  return null;
        return values;
    }

    @GetMapping("/user/contributions")
    public List<ContributionToSchoolTable> getContributionsByUserEmailBYJWT() {
    	String email=getEmailByJWT();
    	System.out.println(email);
        return service.getContributionsByUserEmail(email);
    }

    @PostMapping("/user/contributions/user/{userId}")
    public List<ContributionToSchoolTable> createContributionJWT(@PathVariable Long userId, @RequestBody ContributionToSchoolTable contribution) {
    	String email=getEmailByJWT();
        if(service.createContribution(userId, contribution)!=null) {
        	 return service.getContributionsByUserEmail(email);
        }
        return null;
    }

    @PostMapping("/user/contributions/one")
    public ContributionToSchoolTable createContributionByEmailJWT(@RequestBody ContributionToSchoolTable contribution) {
    	String email=getEmailByJWT();
    
    	return service.createContributionByEmail(email, contribution);
       	
    }
    
    @PostMapping("/user/contributions/bulk")
    public List<ContributionToSchoolTable> updateContributions(@RequestBody List<ContributionToSchoolTable> contributions) {
       // Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = getEmailByJWT();
        return service.updateContributions(contributions, username);
    }
    @DeleteMapping("/user/contributions/{indx}")
    public List<ContributionToSchoolTable> deleteContributionJWT(@PathVariable long indx) {
        // Retrieve the email from the JWT token
        String email = getEmailByJWT();
        
        if(service.CanWeDelete(email,indx)) {
        // Delete the contribution
        service.deleteContribution(indx);
        
        // Retrieve and return the updated list of contributions for the user
          return service.getContributionsByUserEmail(email);
        }
        return null;
    }

 
//    
//    @GetMapping("/{indx}")
//    public ContributionToSchoolTable getContributionById(@PathVariable long indx) {
//        return service.getContributionById(indx);
//    }
//
//    @GetMapping("/user/{userId}")
//    public List<ContributionToSchoolTable> getContributionsByUserId(@PathVariable Long userId) {
//        return service.getContributionsByUserId(userId);
//    }
//
//    @GetMapping("/email/{email}")
//    public List<ContributionToSchoolTable> getContributionsByUserEmail(@PathVariable String email) {
//        return service.getContributionsByUserEmail(email);
//    }
//
//    @PostMapping("/user/{userId}")
//    public ContributionToSchoolTable createContribution(@PathVariable Long userId, @RequestBody ContributionToSchoolTable contribution) {
//        return service.createContribution(userId, contribution);
//    }
//
//    @PostMapping("/email/{email}")
//    public ContributionToSchoolTable createContributionByEmail(@PathVariable String email, @RequestBody ContributionToSchoolTable contribution) {
//        return service.createContributionByEmail(email, contribution);
//    }
//
//    @DeleteMapping("/{indx}")
//    public void deleteContribution(@PathVariable long indx) {
//        service.deleteContribution(indx);
//    }
    private String getEmailByJWT() {
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    	if(authentication==null) {
    		throw new SessionExpireException("Pls Login Again !");
    		
    	}
    	return authentication.getName();
    	
    
    }
}