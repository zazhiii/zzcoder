package com.zazhi.judger.controller;

import com.zazhi.judger.service.DockerPoolService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author zazhi
 * @since 2025/7/20 17:21
 */
@Controller
@RequestMapping("docker/pool")
@RequiredArgsConstructor
public class DockerPoolController {

    private final DockerPoolService dockerPoolService;

    @GetMapping
    public String listContainers(Model model) {
        model.addAttribute("containers", dockerPoolService.listAllContainers());
        return "container/list";
    }

}
