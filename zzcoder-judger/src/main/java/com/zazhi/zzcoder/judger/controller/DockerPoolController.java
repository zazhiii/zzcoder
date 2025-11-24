package com.zazhi.zzcoder.judger.controller;

import com.zazhi.zzcoder.common.pojo.vo.DockerContainerInfoVO;
import com.zazhi.zzcoder.judger.common.pojo.Result;
import com.zazhi.zzcoder.judger.service.DockerPoolService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * @author zazhi
 * @since 2025/7/20 17:21
 */
@Controller
@RequestMapping("/api/docker/pool")
@RequiredArgsConstructor
public class DockerPoolController {
    private final DockerPoolService dockerPoolService;

    @GetMapping
    public String listContainers(Model model) {
        model.addAttribute("containers", dockerPoolService.listAllContainers());
        return "container/list";
    }

    public Result<List<DockerContainerInfoVO>> list(){
        List<DockerContainerInfoVO> containers = dockerPoolService.list();
        return Result.success(containers);

    }

}
