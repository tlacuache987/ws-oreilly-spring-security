package com.oreilly.security.web.controllers;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestWrapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.oreilly.security.domain.entities.Appointment;
import com.oreilly.security.domain.entities.AutoUser;
import com.oreilly.security.domain.entities.Automobile;
import com.oreilly.security.domain.repositories.AppointmentRepository;
import com.oreilly.security.domain.repositories.AutoUserRepository;
import com.oreilly.security.spelutils.SpelExpressionUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/appointments")
public class AppointmentController {

	@Autowired
	private AppointmentRepository appointmentRepository;

	@Autowired
	private AutoUserRepository autoUserRepository;

	@ModelAttribute("isUser")
	public boolean isUser(HttpServletRequest request) {
		/*
		 * return authentication != null && authentication.getAuthorities().contains(
		 * AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_USER").get(0));
		 */
		return new SecurityContextHolderAwareRequestWrapper(request, "").isUserInRole("ROLE_USER");
	}

	@ModelAttribute("isValidUser")
	public boolean isValidUser() {
		return SpelExpressionUtils.evalate("@customSecurityVoter.isValidUser()", Boolean.class);
	}

	@ModelAttribute
	public Appointment getAppointment() {
		Appointment appointment = new Appointment();
		appointment.setAppointmentDt(LocalDate.now());
		appointment.setAutomobile(new Automobile());
		appointment.getAutomobile().setMake("BMW");
		appointment.getAutomobile().setModel("580i");
		appointment.getAutomobile().setYear((short) 2018);
		return appointment;
	}

	@RequestMapping(value = { "", "/" }, method = RequestMethod.GET)
	public String getAppointmentPage() {
		return "appointments";
	}

	@ResponseBody
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public List<Appointment> saveAppointment(@AuthenticationPrincipal UserDetails principal,
			@ModelAttribute Appointment appointment) {

		// User principal = (User)
		// SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		log.info("principal: {}", principal.getClass().getSimpleName());

		Optional<AutoUser> autoUserOptional = autoUserRepository.findByUsername(principal.getUsername());

		if (!autoUserOptional.isPresent()) {
			throw new UsernameNotFoundException("Username not found");
		}

		appointment.setUser(autoUserOptional.get());
		appointment.setStatus("Initial");
		appointmentRepository.save(appointment);

		return this.appointmentRepository.findAll();
	}

	@ResponseBody
	@RequestMapping("/all")
	public List<Appointment> getAppointments() {
		return this.appointmentRepository.findAll();
	}

	@RequestMapping("/{appointmentId}")
	public String getAppointment(@PathVariable("appointmentId") Long appointmentId, Model model) {
		Appointment appointment = appointmentRepository.findOne(appointmentId);
		model.addAttribute("appointment", appointment);
		return "appointment";
	}

	@RequestMapping("/confirm")
	@PreAuthorize("hasRole('ADMIN')")
	public @ResponseBody String confirm() {
		return "confirmed";
	}

	@RequestMapping("/cancel")
	@PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
	public @ResponseBody String cancel() {
		return "canceled";
	}

	@RequestMapping("/complete")
	@PreAuthorize("hasRole('ADMIN') and principal.username == 'ivgarcia'")
	public @ResponseBody String complete() {
		return "completed";
	}

}
