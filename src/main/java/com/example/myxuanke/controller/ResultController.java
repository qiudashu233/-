package com.example.myxuanke.controller;

import com.example.myxuanke.dto.ListDTO;
import com.example.myxuanke.dto.ResultDTO;
import com.example.myxuanke.entiy.XuankedataEntity;
import com.example.myxuanke.entiy.ClassdataEntity;
import com.example.myxuanke.service.ResultService;
import com.example.myxuanke.service.ChooseService;
import com.example.myxuanke.repository.ClassdataRepository;
import com.example.myxuanke.utils.StudentIDUtils;
import com.example.myxuanke.utils.TeacherIDUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/result")
public class ResultController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ResultController.class);

    @Autowired
    private ResultService resultService;

    @Autowired
    private ClassdataRepository planRepositroy;

    @GetMapping("/listbysno")
    public String chooseResultListBySno(Model model, @RequestParam(value = "pageNum", defaultValue = "0")Integer pageNum,
                                   @RequestParam(value = "size", defaultValue = "6") Integer size) {

        //在这要根据学号查询选课结果，不然直接查表会拿到其他学生的选课结果
        String sno = StudentIDUtils.getStudentIDFromMap();
        LOGGER.info("取得学生学号 {}", sno);
        ListDTO<XuankedataEntity> page = resultService.getChooseClassListPageBySno(pageNum, size, sno);
        model.addAttribute("Result", page);

        return "resultbystu";

    }
    @GetMapping("/listbytno")
    public String chooseResultListByTno(Model model, @RequestParam(value = "pageNum", defaultValue = "0")Integer pageNum,
                                   @RequestParam(value = "size", defaultValue = "6") Integer size) {

        //在这要根据学号查询选课结果，不然直接查表会拿到其他学生的选课结果
        String  tno = TeacherIDUtils.getStudentIDFromMap();
        LOGGER.info("取得老师学号 {}", tno);
        List<ClassdataEntity> list = planRepositroy.findClassdataEntitiesByTno(String.valueOf(tno));
        ListDTO<XuankedataEntity> page = new ListDTO<>();
        for(ClassdataEntity classn:list){
            ListDTO<XuankedataEntity> newpage = resultService.getChooseClassListPageByCidAndCno(pageNum,size,classn.getCid(),classn.getCno());
            page.add(newpage);
        }
        model.addAttribute("Result", page);

        return "resultbytea";

    }

    @PostMapping("/{pno}/noChoose")
    @ResponseBody
    public ResultDTO<String> noChoose(@PathVariable(name = "pno") Integer pno) {

        try{
            return resultService.noChoose(pno);
        }catch (Exception e){
            LOGGER.error(e.getMessage(), e);
            return new ResultDTO<>(-1, e.getMessage());
        }

    }

}