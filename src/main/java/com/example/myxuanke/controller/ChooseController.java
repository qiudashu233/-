package com.example.myxuanke.controller;

import com.example.myxuanke.dto.ListDTO;
import com.example.myxuanke.dto.ResultDTO;
import com.example.myxuanke.entiy.ClassdataEntity;
import com.example.myxuanke.service.ChooseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/choose")
public class ChooseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ChooseController.class);

    @Autowired
    private ChooseService chooseService;

    //默认从第0页开始，一页10条数据
    @GetMapping("/list")
    public String getClassEntityList(Model model, @RequestParam(value = "pageNum", defaultValue = "0")Integer pageNum,
                                    @RequestParam(value = "size", defaultValue = "6") Integer size) {

        ListDTO<ClassdataEntity> listDTO = chooseService.getClassListPage(pageNum, size);
        model.addAttribute("planDto", listDTO);
        return "choose";
    }

    @GetMapping("/listbycname")
    public String getClassByCnameEntityList(Model model, @RequestParam(value = "pageNum", defaultValue = "0")Integer pageNum,
                                     @RequestParam(value = "size", defaultValue = "6") Integer size,@RequestParam(value = "cname", defaultValue = "")String cname) {

        ListDTO<ClassdataEntity> listDTO = chooseService.getNeedClassListPageByCname(pageNum, size,cname);
        model.addAttribute("planDto", listDTO);
        return "mohu";
    }

    @GetMapping("/listbycid")
    public String getClassByCidEntityList(Model model, @RequestParam(value = "pageNum", defaultValue = "0")Integer pageNum,
                                            @RequestParam(value = "size", defaultValue = "6") Integer size,@RequestParam(value = "cid", defaultValue = "")String cid) {

        ListDTO<ClassdataEntity> listDTO = chooseService.getNeedClassListPageByCname(pageNum, size,cid);
        model.addAttribute("planDto", listDTO);
        return "mohu";
    }

    @GetMapping("/listbyteaname")
    public String getClassByTeanameEntityList(Model model, @RequestParam(value = "pageNum", defaultValue = "0")Integer pageNum,
                                          @RequestParam(value = "size", defaultValue = "6") Integer size,@RequestParam(value = "teaname", defaultValue = "")String teaname) {

        ListDTO<ClassdataEntity> listDTO = chooseService.getNeedClassListPageByCname(pageNum, size,teaname);
        model.addAttribute("planDto", listDTO);
        return "mohu";
    }


    //选课
    @PostMapping("/{pno}/confirm")
    @ResponseBody
    public ResultDTO<String> doChoose(@PathVariable(value = "pno")Integer pno) {

        try{
            return chooseService.doChoose(pno);
        }catch (Exception e){
            LOGGER.error(e.getMessage(), e);
            return new ResultDTO<>(-1, e.getMessage());
        }

    }


}
