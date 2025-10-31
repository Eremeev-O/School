package ru.hogwarts.school.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.hogwarts.school.service.InfoService;

@RestController
@RequestMapping("/info")

public class InfoController {
    @Autowired
    private InfoService infoService;

    @GetMapping("/port")
    public Integer getPort(){
        return infoService.getPort();
    }

}
