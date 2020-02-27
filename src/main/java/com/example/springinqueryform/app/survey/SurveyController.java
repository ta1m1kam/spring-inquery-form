package com.example.springinqueryform.app.survey;

import com.example.springinqueryform.entity.Survey;
import com.example.springinqueryform.service.SurveyService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/survey")
public class SurveyController {

    private final SurveyService surveyService;

    public SurveyController(SurveyService surveyService) {
        this.surveyService = surveyService;
    }

    @GetMapping
    public String index(Model model) {
        List<Survey> list = surveyService.getAll();
        model.addAttribute("surveyList", list);
        model.addAttribute("title", "Survey Index");
        return "survey/index";
    }

    @GetMapping("/form")
    public String form(SurveyForm surveyForm, Model model, @ModelAttribute("complete") String complete) {
        model.addAttribute("title", "Survey Input");
        return "survey/form";
    }

    @PostMapping("/form")
    public String formGoBack(SurveyForm surveyForm, Model model, @ModelAttribute("complete") String complete) {
        model.addAttribute("title", "Survey Input");
        return "survey/form";
    }

    @PostMapping("/confirm")
    public String confirm(@Validated SurveyForm surveyForm, BindingResult reuslt, Model model) {
        if (reuslt.hasErrors()) {
            model.addAttribute("title", "Survey Input");
            return "survey/form";
        }

        model.addAttribute("title", "Survey confirm");
        return "survey/confirm";
    }

    @PostMapping("/complete")
    public String complete(@Validated SurveyForm surveyForm, BindingResult result, Model model, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("title", "Survay Input");
            return "survey/form";
        }

        Survey survey = new Survey();
        survey.setAge(surveyForm.getAge());
        survey.setSatisfaction(surveyForm.getSatisfaction());
        survey.setComment(surveyForm.getComment());
        survey.setCreated(LocalDateTime.now());
        surveyService.save(survey);

        redirectAttributes.addFlashAttribute("complete", "Registerd");
        return "redirect:/survey/form";
    }
}